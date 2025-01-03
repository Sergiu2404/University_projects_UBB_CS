import 'package:expense_tracker_db/models/Expense.dart';
import 'package:expense_tracker_db/utils/DatabaseContext.dart';
import 'package:flutter/material.dart';

class ViewExpense extends StatefulWidget {
  final Expense expense;

  const ViewExpense({super.key, required this.expense});

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
        title: const Text("My Financial Tracker"),
        backgroundColor: const Color(0xFF32A146),
      ),
      body: WillPopScope(
        onWillPop: () async {
          _moveToLastScreen();
          return true;
        },
        child: Padding(
        padding: const EdgeInsets.all(15),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            //show expense ID
            const Text(
              "ID",
              style: TextStyle(fontSize: 17),
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 8),

            const Text(
              "Amount",
              style: TextStyle(fontSize: 17),
              textAlign: TextAlign.center,
            ),
            TextField(
              controller: _amountController,
              decoration: const InputDecoration(hintText: "enetr an amount..."),
              keyboardType: TextInputType.number,
            ),
            const SizedBox(height: 8),

            const Text(
              "Type",
              style: TextStyle(fontSize: 17),
              textAlign: TextAlign.center,
            ),
            TextField(
              controller: _typeController,
              decoration: const InputDecoration(hintText: "enetr a type..."),
              keyboardType: TextInputType.text,
            ),
            const SizedBox(height: 8),

            const Text(
              "Category",
              style: TextStyle(fontSize: 17),
              textAlign: TextAlign.center,
            ),
            TextField(
              controller: _categoryController,
              decoration: const InputDecoration(hintText: "enetr a category..."),
              keyboardType: TextInputType.text,
            ),
            const SizedBox(height: 8),

            const Text(
              "Date",
              style: TextStyle(fontSize: 17),
              textAlign: TextAlign.center,
            ),
            TextField(
              controller: _dateController,
              readOnly: true, // Prevent manual editing
              decoration: const InputDecoration(
                hintText: "Enter a date...",
                suffixIcon: Icon(Icons.calendar_today), // Optional icon for better UX
              ),
              onTap: () async {
                // Display the date picker dialog
                DateTime? pickedDate = await showDatePicker(
                  context: context,
                  initialDate: DateTime.now(), // Default date
                  firstDate: DateTime(2000), // Earliest selectable date
                  lastDate: DateTime(2100), // Latest selectable date
                );

                if (pickedDate != null) {
                  // Format the selected date as a string
                  String formattedDate = "${pickedDate.day}/${pickedDate.month}/${pickedDate.year}";
                  setState(() {
                    _dateController.text = formattedDate; // Update the controller
                  });
                }
              },
            ),
            const SizedBox(height: 8),

            const Text(
              "Notes",
              style: TextStyle(fontSize: 17),
              textAlign: TextAlign.center,
            ),
            TextField(
              controller: _noteController,
              decoration: const InputDecoration(hintText: "enetr notes..."),
              keyboardType: TextInputType.text,
            ),
            const SizedBox(height: 15),

            Center(
              child: ElevatedButton(
                onPressed: () async {
                  Expense updatedExpense = Expense.withId(
                    widget.expense.id,
                    double.tryParse(_amountController.text) ?? widget.expense.amount,
                    _typeController.text,
                    _categoryController.text,
                    _dateController.text,
                    _noteController.text,
                  );

                  int result = await DatabaseContext().updateExpense(updatedExpense);

                  if (result != 0) {
                    ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Expense updated successfully")));

                    // Returned updated exp instead of "true"
                    Navigator.pop(context, updatedExpense);
                  } else {
                    ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Error updating expense")));
                  }
                },
                child: const Text(
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
