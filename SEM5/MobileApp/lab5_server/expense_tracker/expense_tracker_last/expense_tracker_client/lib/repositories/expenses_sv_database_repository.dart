import 'dart:async';
import 'dart:collection';

import '../../models/expense.dart';
import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';
import 'package:web_socket_channel/web_socket_channel.dart';
import 'dart:convert';
import 'dart:developer';
import 'package:http/http.dart' as http;

import '../utils/pair.dart';


class ExpensesSVDatabaseRepository {
  final WebSocketChannel _channel;
  bool _online;
  bool get is_online => _online;
  Function(bool)? onOnlineStatusChanged;

  List<Expense> _expenses;
  late List<Expense> _deleted_db_expenses;
  late List<Expense> _updated_db_expenses;

  late StreamSubscription _streamSubscription;
  Function(bool)? onDataUpdated;


  final Database _database;
  static const String tableName = 'Expenses';

  static const String idColumn = 'id';
  static const String amountColumn = 'amount';
  static const String typeColumn = 'type';
  static const String dateColumn = 'date';
  static const String noteColumn = 'note';


  static const String ipAddress = '10.0.2.2';

  ExpensesSVDatabaseRepository(this._database, this._channel, this._expenses, this._online) {
    _deleted_db_expenses = [];
    _updated_db_expenses = [];
    _initializeWebSocketListener();
    Timer.periodic(const Duration(seconds: 1), (Timer t) => checkOnline());
  }

  void _initializeWebSocketListener() {
    _streamSubscription = _channel.stream.listen(
          (data) {
        log("ws data $data");
        _listenToServerHandler(data);
      },
      onDone: _reconnect, // Attempt to reconnect on disconnection
      onError: (error) {
        log('ws error $error');
        _reconnect(); // Attempt to reconnect on error
      },
    );
  }

  void _reconnect() {
    print('ws attempting to reconnect...');
    _channel.sink.close();
    _streamSubscription.cancel();
    _initializeWebSocketListener();
  }

  static Future<ExpensesSVDatabaseRepository> init() async {
    try {
      var databasesPath = await getDatabasesPath();
      var path = join(databasesPath, 'expenses.db');

      var database = await openDatabase(path, version: 1, onCreate: (Database db, int version) async {
        await db.execute(
            '''
              CREATE TABLE $tableName(
              $idColumn INTEGER PRIMARY KEY AUTOINCREMENT,
              $amountColumn REAL,
              $typeColumn TEXT,
              $dateColumn TEXT,
              $noteColumn TEXT)
            '''
        );
      });
      final channel = WebSocketChannel.connect(Uri.parse("ws://$ipAddress:8765"));
      return ExpensesSVDatabaseRepository(database, channel, [], false);
    } on Exception catch (e) {
      log('ERROR: $e');
      rethrow;
    }
  }

  Future<void> _listenToServerHandler(String data) async {
    var listData = data.split('\$');

    if (listData[0] == "ADD") {
      var expenseJson = jsonDecode(listData[1]);
      var expense = Expense(expenseJson['amount'], expenseJson['type'], expenseJson['date'], expenseJson['note']);

      await _database.insert(tableName, expense.toMap());
    } else if (listData[0] == "UPDATE") {
      var expense = jsonDecode(listData[1]);

      await updateLocally(expense['id'], expense['amount'], expense['type'], expense['date'], expense['note']);
    } else if (listData[0] == "DELETE") {
      await removeLocally(int.parse(listData[1]));
    }

    onDataUpdated?.call(true);
  }

  Future<void> _synchronizeServerAndClients() async {
    await checkOnline();

    if (_online) {
      // Create a list of locally added expenses by comparing database with last known server state
      List<Expense> locallyAddedExpenses = [];
      final List<Map<String, dynamic>> maps = await _database.query(tableName);

      for (var expenseMap in maps) {
        var expense = Expense(expenseMap['amount'], expenseMap['type'], expenseMap['date'], expenseMap['note']);
        expense.id = expenseMap['id'] as int;

        // Check if this expense exists in _expenses (server state)
        bool existsInServer = _expenses.any((serverExpense) =>
        serverExpense.amount == expense.amount &&
            serverExpense.type == expense.type &&
            serverExpense.date == expense.date &&
            serverExpense.note == expense.note
        );

        if (!existsInServer) {
          locallyAddedExpenses.add(expense);
        }
      }

      var jsonArr = jsonEncode(locallyAddedExpenses);  // Send locally added expenses
      var deletedIds = jsonEncode(_deleted_db_expenses.map((expense) => expense.id).toList());
      var updatedExpenses = jsonEncode(_updated_db_expenses.map((expense) => expense.toMapWithId()).toList());

      Map<String, String> headers = HashMap();
      headers['Accept'] = 'application/json';
      headers['Content-type'] = 'application/json';

      var response = await http.post(
          Uri.parse("http://$ipAddress:5001/expense/sync"),
          headers: headers,
          body: jsonEncode({
            'expenses': jsonArr,
            'deleted_ids': deletedIds,
            'updated_expenses': updatedExpenses,
          }),
          encoding: Encoding.getByName('utf-8')
      );

      // if (response.statusCode == 200) {
      //   var res = json.decode(response.body);
      //
      //   var expensesJson = res['expenses'] as List;
      //   _expenses = expensesJson.map((expenseJson) => Expense.fromJson(expenseJson)).toList();
      //
      //   // Update local database with server state
      //   await _database.execute("DELETE FROM $tableName");
      //   for (var expense in _expenses) {
      //     await _database.insert(tableName, expense.toMapWithId());
      //   }
      //
      //   _deleted_db_expenses.clear();
      //   _updated_db_expenses.clear();
      // }
      if (response.statusCode == 200) {
        var res = json.decode(response.body);
        var expensesJson = res['expenses'] as List;
        var serverExpenses = expensesJson.map((expenseJson) => Expense.fromJson(expenseJson)).toList();

        // Merge server data with existing local data
        for (var localExpense in _expenses) {
          if (!serverExpenses.any((e) => e.id == localExpense.id)) {
            serverExpenses.add(localExpense);
          }
        }

        _expenses = serverExpenses;

        // Update local database
        await _database.execute("DELETE FROM $tableName");
        for (var expense in _expenses) {
          await _database.insert(tableName, expense.toMapWithId());
        }

        _deleted_db_expenses.clear();
        _updated_db_expenses.clear();
      }

    }
  }

  Future<bool> checkOnline() async {
    bool previousStatus = _online;
    try {
      var response = await http.get(Uri.parse("http://$ipAddress:5001/"))
          .timeout(const Duration(seconds: 1));

      _online = response.statusCode == 200;
      if (_online && !previousStatus) {
        _initializeWebSocketListener();
        await _synchronizeServerAndClients();
      }
    } on Exception catch (e) {
      log('ERROR: $e');
      _online = false;
    }

    if (previousStatus != _online) {
      onOnlineStatusChanged?.call(_online);
    }

    log('Online status changed: $_online');
    return _online;
  }

  Future<Pair> getLocally() async {
    final List<Map<String, dynamic>> maps = await _database.query(tableName);

    _expenses.clear();

    for (var expenseMap in maps) {
      var expense = Expense(expenseMap['amount'], expenseMap['type'], expenseMap['date'], expenseMap['note']);
      expense.id = expenseMap['id'] as int;
      _expenses.add(expense);
    }

    return Pair(_expenses, _online);
  }

  Future<Pair> getAll() async {
    await checkOnline();
    try {
      var response = await http
          .get(Uri.parse("http://$ipAddress:5001/expenses"))
          .timeout(const Duration(seconds: 1));

      if (response.statusCode == 200) {
        var res = json.decode(response.body);
        var expensesJson = res['expenses'] as List;
        _expenses = expensesJson.map((expenseJson) => Expense.fromJson(expenseJson)).toList();

        return Pair(_expenses, _online);
      } else {
        throw Exception('Failed to get expenses from server');
      }
    } on Exception catch (e) {
      log('ERROR: $e');
      return getLocally();
    }
  }

  Future<void> addLocally(double amount, String type, String date, String note) async {
    await _database.insert(tableName, Expense(amount, type, date, note).toMap());
  }
  // Future<void> addLocally(double amount, String type, String date, String note) async {
  //   await _database.insert(tableName, Expense(amount, type, date, note).toMap());
  //   // Force a refresh of the local _expenses list
  //   await getLocally();
  // }

  Future<void> add(Expense expense) async {
    await checkOnline();
    if (_online) {
      try {
        Map<String, String> headers = HashMap();
        headers['Accept'] = 'application/json';
        headers['Content-type'] = 'application/json';

        var response = await http
            .post(Uri.parse("http://$ipAddress:5001/expense"),
            headers: headers,
            body: jsonEncode({
              'amount': expense.amount,
              'type': expense.type,
              'date': expense.date,
              'note': expense.note,
            }),
            encoding: Encoding.getByName('utf-8'))
            .timeout(const Duration(seconds: 1));

        if (response.statusCode == 200) {
          await addLocally(expense.amount, expense.type, expense.date, expense.note);
          await _synchronizeServerAndClients();
          log('SUCCESS: Added expense with amount: ${expense.amount} type: ${expense.type}');
        } else {
          throw Exception('Failed to add expense to server');
        }
      } on Exception catch (e) {
        log('ERROR: $e');
        return addLocally(expense.amount, expense.type, expense.date, expense.note);
      }
    } else {
      return addLocally(expense.amount, expense.type, expense.date, expense.note);
    }
  }

  Future<void> removeLocally(int id) async {
    _expenses.removeWhere((element) => element.id == id);
    await _database.delete(tableName, where: ' id = ?', whereArgs: [id]);
  }

  Future<void> remove(int id) async {
    await checkOnline();
    if (_online) {
      try {
        var response = await http
            .delete(Uri.parse("http://$ipAddress:5001/expense/$id"))
            .timeout(const Duration(seconds: 1));

        if (response.statusCode == 200) {
          _deleted_db_expenses.add(_expenses.firstWhere((element) => element.id == id));
          await removeLocally(id);
          await _synchronizeServerAndClients();
          log('SUCCESS: Removed expense with id $id');
        } else {
          throw Exception('Failed to remove expense from the server');
        }
      } on Exception catch (e) {
        log('ERROR: $e');
        _deleted_db_expenses.add(_expenses.firstWhere((element) => element.id == id));
        await removeLocally(id);
      }
    } else {
      _deleted_db_expenses.add(_expenses.firstWhere((element) => element.id == id));

      await removeLocally(id);
    }
  }


  Future<void> updateLocally(int id, double amount, String type, String date, String note) async {
    await _database.update(tableName, Expense(amount, type, date, note).toMap(), where: "$idColumn = ?", whereArgs: [id]);
  }


  Future<void> update(int id, Expense expense) async {
    await checkOnline();
    var expense_with_id = Expense(expense.amount, expense.type, expense.date, expense.note);
    expense_with_id.id = id;
    if (_online) {
      try {
        Map<String, String> headers = HashMap();
        headers['Accept'] = 'application/json';
        headers['Content-type'] = 'application/json';

        var response = await http
            .put(Uri.parse("http://$ipAddress:5001/expense"),
            headers: headers,
            body: jsonEncode({
              'id': id,
              'amount': expense.amount,
              'type': expense.type,
              'date': expense.date,
              'note': expense.note
            }),
            encoding: Encoding.getByName('utf-8'))
            .timeout(const Duration(seconds: 2));
        if (response.statusCode == 200) {
          _updated_db_expenses.add(expense_with_id);
          await updateLocally(id, expense.amount, expense.type, expense.date, expense.note);
          await _synchronizeServerAndClients();
          log('SUCCESS: Updated expense with id $id');
        } else {
          throw Exception('Failed to update expense to server');
        }
      } on Exception catch (e) {
        log('ERROR: $e');
        _updated_db_expenses.add(expense_with_id);
        return updateLocally(id, expense.amount, expense.type, expense.date, expense.note);
      }
    } else {
      _updated_db_expenses.add(expense_with_id);
      await updateLocally(id, expense.amount, expense.type, expense.date, expense.note);
    }
  }
}