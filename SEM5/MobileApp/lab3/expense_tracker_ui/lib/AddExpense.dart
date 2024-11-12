import 'package:flutter/material.dart';
import 'package:expense_tracker_ui/domain/Expense.dart';

class AddExpensePage extends StatefulWidget {
  const AddExpensePage({Key? key}) : super(key: key);

  @override
  _AddExpensePageState createState() => _AddExpensePageState();
}

class _AddExpensePageState extends State<AddExpensePage> {
  final _formKey = GlobalKey<FormState>();
  final _categoryController = TextEditingController();
  final _amountController = TextEditingController();
  final _typeController = TextEditingController();
  final _notesController = TextEditingController();
  final _dateController = TextEditingController();

  @override
  void dispose() { //clean controllers when widget is popped
    _categoryController.dispose();
    _amountController.dispose();
    _typeController.dispose();
    _notesController.dispose();
    _dateController.dispose();
    super.dispose();
  }

  void _submitForm() {
    if (_formKey.currentState!.validate()) { //check if any field is invalid
      final newExpense = Expense(
        id: DateTime.now().millisecondsSinceEpoch,
        amount: double.tryParse(_amountController.text) ?? 0.0,
        category: _categoryController.text,
        date: _dateController.text,
        type: _typeController.text,
        notes: _notesController.text,
      );
      Navigator.pop(context, newExpense);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Add Expense")),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              TextFormField(
                controller: _categoryController,
                decoration: const InputDecoration(labelText: "Category"),
                validator: (value) => value!.isEmpty ? "Enter a category" : null,
              ),
              TextFormField(
                controller: _amountController,
                decoration: const InputDecoration(labelText: "Amount"),
                keyboardType: TextInputType.number,
                validator: (value) => value!.isEmpty ? "Enter an amount" : null,
              ),
              TextFormField(
                controller: _typeController,
                decoration: const InputDecoration(labelText: "Type"),
                validator: (value) => value!.isEmpty ? "Enter type (RECEIVING/SPENDING)" : null,
              ),
              TextFormField(
                controller: _dateController, // New date input
                decoration: const InputDecoration(labelText: "Date (DD.MM.YYYY)"),
                validator: (value) => value!.isEmpty ? "Enter a date" : null,
              ),
              TextFormField(
                controller: _notesController,
                decoration: const InputDecoration(labelText: "Notes"),
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: _submitForm,
                child: const Text("Add Expense"),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
