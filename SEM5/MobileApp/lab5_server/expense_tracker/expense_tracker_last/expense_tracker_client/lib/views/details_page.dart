import 'dart:developer';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../models/expense.dart';
import '../../services/expenses_service.dart';
import 'home_page.dart';

class ExpenseDetailsWidget extends StatefulWidget {
  final Expense expense;

  const ExpenseDetailsWidget(this.expense, {Key? key}) : super(key: key);

  @override
  State<ExpenseDetailsWidget> createState() => _ExpenseDetailsWidgetState();
}

class _ExpenseDetailsWidgetState extends State<ExpenseDetailsWidget> {
  late final TextEditingController amountController;
  late final TextEditingController dateController;
  late final TextEditingController noteController;
  String selectedType = "SPENDING"; // default

  @override
  void initState() {
    super.initState();
    amountController = TextEditingController(text: widget.expense.amount.toString());
    selectedType = widget.expense.type;
    dateController = TextEditingController(text: widget.expense.date);
    noteController = TextEditingController(text: widget.expense.note);
  }

  @override
  void dispose() {
    amountController.dispose();
    dateController.dispose();
    noteController.dispose();
    super.dispose();
  }

  void showAlertDialog(String message) {
    Widget okButton = TextButton(
      child: const Text("OK"),
      onPressed: () {
        Navigator.pop(context);
      },
    );

    AlertDialog alert = AlertDialog(
      title: const Text("Error"),
      content: Text(message),
      actions: [
        okButton,
      ],
    );

    showDialog(
      context: context,
      builder: (BuildContext context) {
        return alert;
      },
    );
  }

  bool _validateInputs() {
    if (amountController.text.isEmpty) {
      showAlertDialog("Amount is required.");
      return false;
    }
    if (selectedType.isEmpty) {
      showAlertDialog("Type is required.");
      return false;
    }
    if (dateController.text.isEmpty) {
      showAlertDialog("Date is required.");
      return false;
    }

    try {
      double.parse(amountController.text);
    } catch (e) {
      showAlertDialog("Amount should be a valid number.");
      return false;
    }
    return true;
  }

  void _updateExpense() {
    if (_validateInputs()) {
      var amount = double.parse(amountController.text);

      Provider.of<ExpensesService>(context, listen: false)
          .update(widget.expense.id, amount, selectedType, dateController.text, noteController.text);

      Navigator.pop(context);
    }
  }

  void _deleteExpense() {
    Provider.of<ExpensesService>(context, listen: false).remove(widget.expense.id);

    Navigator.pop(context);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text(widget.expense.amount.toString()),
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            ListTile(
              title: TextField(
                controller: amountController,
                decoration: const InputDecoration(
                  labelText: "Amount",
                ),
                keyboardType: TextInputType.number,
              ),
            ),
            ListTile(
              title: DropdownButton<String>(
                value: selectedType,
                items: const [
                  DropdownMenuItem(value: "SPENDING", child: Text("SPENDING")),
                  DropdownMenuItem(value: "RECEIVING", child: Text("RECEIVING")),
                ],
                onChanged: (value) {
                  setState(() {
                    selectedType = value!;
                  });
                },
              ),
            ),
            ListTile(
              title: TextField(
                controller: dateController,
                readOnly: true,
                decoration: const InputDecoration(
                  labelText: "Date",
                ),
                onTap: () async {
                  DateTime? pickedDate = await showDatePicker(
                    context: context,
                    initialDate: DateTime.now(),
                    firstDate: DateTime(2000),
                    lastDate: DateTime(2100),
                  );

                  if (pickedDate != null) {
                    String formattedDate =
                        '${pickedDate.year}-${pickedDate.month.toString().padLeft(2, '0')}-${pickedDate.day.toString().padLeft(2, '0')}';
                    setState(() {
                      dateController.text = formattedDate;
                    });
                  }
                },
              ),
            ),
            ListTile(
              title: TextField(
                controller: noteController,
                decoration: const InputDecoration(
                  labelText: "Note",
                ),
              ),
            ),
            Center(
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.lightGreen,
                ),
                child: const Text("Update Expense"),
                onPressed: _updateExpense,
              ),
            ),
            Center(
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.red,
                ),
                child: const Text("Delete Expense"),
                onPressed: _deleteExpense,
              ),
            ),
          ],
        ),
      ),
    );
  }
}