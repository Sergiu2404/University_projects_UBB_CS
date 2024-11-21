import 'package:expense_tracker_ui/widgets/ExpenseItemWidget.dart';
import 'package:flutter/material.dart';
import 'package:expense_tracker_ui/AddExpense.dart';
import 'package:expense_tracker_ui/domain/Expense.dart';
import 'package:expense_tracker_ui/services/ExpenseService.dart';

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final ExpenseService _expenseService = ExpenseService();
  late List<Expense> _expenses;


  @override
  void initState() {
    super.initState();
    _expenses = _expenseService.getAllExpenses();
  }

  Future<void> _navigateToAddExpensePage() async {
    final newExpense = await Navigator.push<Expense>(
      context,
      MaterialPageRoute(builder: (context) => const AddExpensePage()),
    );

    if (newExpense != null) {
      setState(() {
        _expenseService.addExpense(newExpense);
        _expenses = _expenseService.getAllExpenses();
      });
    }
  }

  void _deleteExpense(int id) {
    setState(() {
      _expenseService.removeExpense(id);
      _expenses = _expenseService.getAllExpenses();
    });
  }

  void _updateExpense(Expense updatedExpense) {
    setState(() {
      _expenseService.updateExpense(updatedExpense);
      _expenses = _expenseService.getAllExpenses();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFF32A146),
        title: const Text(
          'My Financial Tracker',
          style: TextStyle(color: Colors.black, fontSize: 17),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Expenses List',
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
                color: Colors.black,
              ),
            ),
            const SizedBox(height: 16),
            Align(
              alignment: Alignment.centerRight,
              child: GestureDetector(
                onTap: _navigateToAddExpensePage,
                child: Container(
                  padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 16),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(4),
                    border: Border.all(color: Colors.grey.shade300),
                  ),
                  child: const Text(
                    'Add Expense',
                    style: TextStyle(
                      color: Colors.black,
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ),
            ),
            const SizedBox(height: 16),
            Expanded(
              child: ListView.builder(
                itemCount: _expenses.length,
                itemBuilder: (context, index) {
                  final expense = _expenses[index];
                  return ExpenseItemWidget(
                    expense: expense,
                    onDelete: _deleteExpense,
                    onUpdate: _updateExpense,
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
