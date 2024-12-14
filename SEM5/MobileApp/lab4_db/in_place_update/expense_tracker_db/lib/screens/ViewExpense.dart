import 'package:expense_tracker_db/models/Expense.dart';
import 'package:expense_tracker_db/utils/DatabaseContext.dart';
import 'package:flutter/material.dart';

class ViewExpense extends StatefulWidget {
  final Expense expense;

  const ViewExpense({Key? key, required this.expense}) : super(key: key);

  @override
  State<ViewExpense> createState() => _ExpenseDetailState();
}


class _ExpenseDetailState extends State<ViewExpense> {
  final TextEditingController _amountController = TextEditingController();
  final TextEditingController _typeController = TextEditingController();
  final TextEditingController _categoryController = TextEditingController();
  final TextEditingController _dateController = TextEditingController();
  final TextEditingController _noteController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _amountController.text = widget.expense.amount.toString();
    _typeController.text = widget.expense.type;
    _categoryController.text = widget.expense.category;
    _dateController.text = widget.expense.date;
    _noteController.text = widget.expense.note;
  }

  // Clean up controllers
  @override
  void dispose() {
    _amountController.dispose();
    _typeController.dispose();
    _categoryController.dispose();
    _dateController.dispose();
    _noteController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("My Financial Tracker"),
        backgroundColor: Color(0xFF32A146),
      ),
      body: WillPopScope(
        onWillPop: () async {
          _moveToLastScreen();
          return true;
        },
        child: Padding(
        padding: EdgeInsets.all(15),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            //show expense ID
            Text(
              "ID",
              style: TextStyle(fontSize: 17),
              textAlign: TextAlign.center,
            ),
            SizedBox(height: 8),

            Text(
              "Amount",
              style: TextStyle(fontSize: 17),
              textAlign: TextAlign.center,
            ),
            TextField(
              controller: _amountController,
              decoration: InputDecoration(hintText: "enetr an amount..."),
              keyboardType: TextInputType.number,
            ),
            SizedBox(height: 8),

            Text(
              "Type",
              style: TextStyle(fontSize: 17),
              textAlign: TextAlign.center,
            ),
            TextField(
              controller: _typeController,
              decoration: InputDecoration(hintText: "enetr a type..."),
              keyboardType: TextInputType.text,
            ),
            SizedBox(height: 8),

            Text(
              "Category",
              style: TextStyle(fontSize: 17),
              textAlign: TextAlign.center,
            ),
            TextField(
              controller: _categoryController,
              decoration: InputDecoration(hintText: "enetr a category..."),
              keyboardType: TextInputType.text,
            ),
            SizedBox(height: 8),

            Text(
              "Date",
              style: TextStyle(fontSize: 17),
              textAlign: TextAlign.center,
            ),
            TextField(
              controller: _dateController,
              decoration: InputDecoration(hintText: "enetr a date..."),
              keyboardType: TextInputType.text,
            ),
            SizedBox(height: 8),

            Text(
              "Notes",
              style: TextStyle(fontSize: 17),
              textAlign: TextAlign.center,
            ),
            TextField(
              controller: _noteController,
              decoration: InputDecoration(hintText: "enetr notes..."),
              keyboardType: TextInputType.text,
            ),
            SizedBox(height: 15),

            Center(
              child: ElevatedButton(
                onPressed: () async {
                  // Update the expense with new values from text fields
                  Expense updatedExpense = Expense.withId(
                    widget.expense.id,
                    double.tryParse(_amountController.text) ?? widget.expense.amount,
                    _typeController.text,
                    _categoryController.text,
                    _dateController.text,
                    _noteController.text,
                  );

                  // Update in database
                  int result = await DatabaseContext().updateExpense(updatedExpense);

                  if (result != 0) {
                    ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Expense updated successfully")));

                    // Return the updated expense object instead of 'true'
                    Navigator.pop(context, updatedExpense);
                  } else {
                    ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Error updating expense")));
                  }
                },
                child: Text(
                  "Submit",
                  style: TextStyle(fontSize: 15, fontWeight: FontWeight.bold, color: Color(0xFF32A146)),
                ),
              ),
            )

          ],
        ),
      ),
    ));
  }

  void _moveToLastScreen() {
    Navigator.pop(context);
  }
}
