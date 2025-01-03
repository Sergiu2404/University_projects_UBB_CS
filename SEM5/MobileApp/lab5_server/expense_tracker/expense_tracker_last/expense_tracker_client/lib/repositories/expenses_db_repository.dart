import '../../models/expense.dart';
import '../../repositories/expenses_repository.dart';
import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';

class ExpensesDatabaseRepository implements ExpensesRepository {
  final Database _database;

  static const String tableName = 'Expenses';

  static const String idColumn = 'id';
  static const String amountColumn = 'amount';
  static const String typeColumn = 'type';
  static const String dateColumn = 'date';
  static const String noteColumn = 'note';


  ExpensesDatabaseRepository(this._database);

  static Future<ExpensesRepository> init() async {
    var databasesPath = await getDatabasesPath();
    var path = join(databasesPath, 'expenses.db');

    //await deleteDatabase(path);

    // open the database
    var database = await openDatabase(path, version: 1,
        onCreate: (Database db, int version) async {
          // When creating the db, create the table
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
        }
    );

    return ExpensesDatabaseRepository(database);
  }

  @override
  Future<void> add(Expense expense) async {
    Map<String, dynamic> row = {
      amountColumn : expense.amount,
      typeColumn : expense.type,
      dateColumn: expense.date,
      noteColumn: expense.note
    };

    await _database.insert(tableName, row);
  }

  @override
  Future<void> remove(int id) async {
    await _database.delete(tableName, where: "$idColumn = ?", whereArgs: [id]);
  }

  @override
  Future<void> update(int id, Expense expense) async {
    Map<String, dynamic> row = {
      amountColumn : expense.amount,
      typeColumn : expense.type,
      dateColumn: expense.date,
      noteColumn: expense.note
    };

    await _database.update(tableName, row, where: "$idColumn = ?", whereArgs: [id]);
  }

  @override
  Future<List<Expense>> getAll() async {
    List<Map<String, dynamic>> tasks = await _database.query(tableName);
    return tasks.map((e) => Expense.fromMap(e)).toList();
  }
}