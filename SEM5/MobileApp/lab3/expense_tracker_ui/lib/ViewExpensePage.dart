import 'package:flutter/material.dart';
import 'package:expense_tracker_ui/domain/Expense.dart';
import 'package:expense_tracker_ui/services/ExpenseService.dart';

class ViewExpensePage extends StatefulWidget {
  final Expense expense;

  const ViewExpensePage({Key? key, required this.expense}) : super(key: key);

  @override
  _ViewExpensePageState createState() => _ViewExpensePageState();
}

class _ViewExpensePageState extends State<ViewExpensePage> {
  late TextEditingController _amountController;
  late TextEditingController _categoryController;
  late TextEditingController _dateController;
  late TextEditingController _typeController;
  late TextEditingController _notesController;

  @override
  void initState() {
    super.initState();
    _amountController = TextEditingController(text: widget.expense.amount.toString());
    _categoryController = TextEditingController(text: widget.expense.category);
    _dateController = TextEditingController(text: widget.expense.date);
    _typeController = TextEditingController(text: widget.expense.type);
    _notesController = TextEditingController(text: widget.expense.notes);
  }

  @override
  void dispose() {
    _amountController.dispose();
    _categoryController.dispose();
    _dateController.dispose();
    _typeController.dispose();
    _notesController.dispose();
    super.dispose();
  }

  void _updateExpense() {
    final updatedExpense = Expense(
      id: widget.expense.id,
      amount: double.tryParse(_amountController.text) ?? 0.0,
      category: _categoryController.text,
      date: _dateController.text,
      type: _typeController.text,
      notes: _notesController.text,
    );

    Navigator.pop(context, updatedExpense);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('View Expense'),
        backgroundColor: const Color(0xFF32A146),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            TextField(
              controller: _amountController,
              decoration: const InputDecoration(labelText: 'Amount'),
              keyboardType: TextInputType.number,
            ),
            TextField(
              controller: _categoryController,
              decoration: const InputDecoration(labelText: 'Category'),
            ),
            TextField(
              controller: _dateController,
              decoration: const InputDecoration(labelText: 'Date (DD.MM.YYYY)'),
            ),
            TextField(
              controller: _typeController,
              decoration: const InputDecoration(labelText: 'Type (RECEIVING/SPENDING)'),
            ),
            TextField(
              controller: _notesController,
              decoration: const InputDecoration(labelText: 'Notes'),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: _updateExpense,
              child: const Text('Update Expense'),
            ),
          ],
        ),
      ),
    );
  }
}
