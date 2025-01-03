
import 'package:sqflite/sqflite.dart';
import 'dart:async';
import 'dart:io';
import 'package:path_provider/path_provider.dart';
import 'package:expense_tracker_db/models/Expense.dart';

class DatabaseContext {
  static Database? _database;
  static DatabaseContext? _databaseContext;

  String expensesTable = 'expenses';
  String colId = 'id';
  String colAmount = 'amount';
  String colType = 'type';
  String colCategory = 'category';
  String colDate = 'date';
  String colNote = 'note';

  DatabaseContext._createInstance();

  factory DatabaseContext() {
    _databaseContext ??= DatabaseContext._createInstance();
    return _databaseContext!;
  }

  Future<Database> initializeDatabase() async {
    if (_database != null) return _database!;

    Directory directory = await getApplicationDocumentsDirectory();
    String path = '${directory.path}/expenses.db';

    _database = await openDatabase(path, version: 1, onCreate: _createDatabase);
    return _database!;
  }

  Future<Database> get database async {
    _database ??= await initializeDatabase();
    return _database!; // Ensure we return a non-null value
  }

  void _createDatabase(Database database, int newVersion) async {
    await database.execute(
      'CREATE TABLE $expensesTable('
          '$colId INTEGER PRIMARY KEY AUTOINCREMENT, '
          '$colAmount REAL, '
          '$colType TEXT, '
          '$colCategory TEXT, '
          '$colDate TEXT, '
          '$colNote TEXT)',
    );
  }

  Future<List<Map<String, dynamic>>> getAllExpensesMapList() async {
    Database db = await database;
    var result = await db.rawQuery('SELECT * FROM $expensesTable ORDER BY $colAmount DESC');
    return result;
  }

  Future<int> insertExpense(Expense expense) async {
    Database db = await database;
    var result = await db.insert(expensesTable, expense.toMap()); // Updated method name
    return result;
  }

  Future<int> updateExpense(Expense expense) async {
    Database db = await database;
    var result = await db.update(expensesTable, expense.toMap(), where: '$colId = ?', whereArgs: [expense.id]);
    return result;
  }

  Future<int> deleteExpense(int id) async {
    var db = await database;
    int result = await db.delete(expensesTable, where: '$colId = ?', whereArgs: [id]);
    return result;
  }

  Future<int?> getSize() async {
    Database db = await database;
    List<Map<String, dynamic>> elements = await db.rawQuery('SELECT COUNT(*) FROM $expensesTable'); // Fixed count query
    int? result = Sqflite.firstIntValue(elements);
    return result;
  }

  Future<List<Expense>> getExpensesList() async {
    var expensesMapList = await getAllExpensesMapList(); // Fetch the map list from the database

    List<Expense> expenses = [];
    for (int i = 0; i < expensesMapList.length; i++) {
      var expenseMap = expensesMapList[i];
      // Create an Expense object from the map
      var expense = Expense.withId(
        expenseMap['id'],
        expenseMap['amount'],
        expenseMap['type'],
        expenseMap['category'],
        expenseMap['date'],
        expenseMap['note'],
      );
      expenses.add(expense);
    }

    return expenses;
  }
}
