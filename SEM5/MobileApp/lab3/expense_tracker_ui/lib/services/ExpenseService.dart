import 'package:expense_tracker_ui/domain/Expense.dart';

class ExpenseService {
  final List<Expense> _expenses = [
    Expense(id: 1, amount: 3725.00, category: "Salary", date: "10.10.2024", type: "RECEIVING", notes: ""),
    Expense(id: 2, amount: 660.00, category: "Meal Tickets", date: "05.10.2024", type: "RECEIVING", notes: "Meal tickets received as benefit for job"),
    Expense(id: 3, amount: 180.00, category: "Gym", date: "18.10.2024", type: "SPENDING", notes: "gym subscription paid automatically for September"),
  ];

  void addExpense(Expense expense) {
    _expenses.add(expense);
  }

  void removeExpense(int id) {
    _expenses.removeWhere((expense) => expense.id == id);
  }

  Expense? getExpenseById(int id) {
    return _expenses.firstWhere((expense) => expense.id == id, orElse: () => Expense(id: -1, amount: 0, category: "", date: "", type: "", notes: ""));
  }

  void updateExpense(Expense updatedExpense) {
    final index = _expenses.indexWhere((expense) => expense.id == updatedExpense.id);
    if (index != -1) {
      _expenses[index] = updatedExpense;
    }
  }

  List<Expense> getAllExpenses() {
    return List.unmodifiable(_expenses);
  }
}
