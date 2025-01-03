import 'package:expense_tracker_client/models/expense.dart';

abstract class ExpensesRepository{
  Future<void> add(Expense expense);

  Future<void> remove(int id);

  Future<void> update(int id, Expense expense);

  Future<List<Expense>> getAll();
}