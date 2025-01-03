
import '../models/expense.dart';
import 'expenses_repository.dart';

class ExpensesMemoryRepository implements ExpensesRepository {
  final List<Expense> elements = [];

  @override
  Future<void> add(Expense expense) async {
    elements.add(expense);
  }

  @override
  Future<void> remove(int id) async {
    Expense expense = elements.where((element) => element.id == id).first;
    elements.remove(expense);
  }

  @override
  Future<void> update(int id, Expense expense) async {
    expense.id = id;
    elements[elements.indexWhere((element) => element.id == id)] = expense;
  }

  @override
  Future<List<Expense>> getAll() async {
    return elements;
  }
}