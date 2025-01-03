import 'dart:developer';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../services/expenses_service.dart';
import 'package:image_picker/image_picker.dart';

class ExpensesAddPage extends StatefulWidget {
  const ExpensesAddPage({Key? key}) : super(key: key);

  @override
  _ExpensesAddPageState createState() => _ExpensesAddPageState();
}

class _ExpensesAddPageState extends State<ExpensesAddPage> {
  late final TextEditingController amountController;
  late final TextEditingController dateController;
  late final TextEditingController noteController;
  String? selectedType; // For dropdown selection

  @override
  void initState() {
    super.initState();
    amountController = TextEditingController();
    dateController = TextEditingController();
    noteController = TextEditingController();
  }

  @override
  void dispose() {
    amountController.dispose();
    dateController.dispose();
    noteController.dispose();
    super.dispose();
  }

  void showAlertDialog(BuildContext context, String message) {
    Widget okButton = TextButton(
      child: const Text("OK"),
      onPressed: () {
        Navigator.pop(context);
      },
    );

    AlertDialog alert = AlertDialog(
      title: const Text("Error"),
      content: Text(message),
      actions: [okButton],
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
      showAlertDialog(context, "Amount field is required.");
      return false;
    }
    if (selectedType == null || selectedType!.isEmpty) {
      showAlertDialog(context, "Type field is required.");
      return false;
    }
    if (dateController.text.isEmpty) {
      showAlertDialog(context, "Date field is required.");
      return false;
    }

    try {
      double.parse(amountController.text);
    } catch (e) {
      showAlertDialog(context, "Amount should be a valid number (double/integer).");
      return false;
    }
    return true;
  }

  Future<void> _pickDate(BuildContext context) async {
    DateTime? pickedDate = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1900),
      lastDate: DateTime(2100),
    );

    if (pickedDate != null) {
      String formattedDate = '${pickedDate.year}-${pickedDate.month.toString().padLeft(2, '0')}-${pickedDate.day.toString().padLeft(2, '0')}';
      setState(() {
        dateController.text = formattedDate;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: const Text('Add New Expense'),
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            ListTile(
              leading: const Icon(Icons.attach_money),
              title: TextField(
                controller: amountController,
                decoration: const InputDecoration(
                  labelText: "Amount",
                ),
                keyboardType: TextInputType.number, // to ensure numeric input
              ),
            ),
            ListTile(
              title: DropdownButton<String>(
                value: selectedType,
                hint: const Text("Select Type"),
                isExpanded: true,
                items: ["RECEIVING", "SPENDING"]
                    .map((type) => DropdownMenuItem<String>(
                  value: type,
                  child: Text(type),
                ))
                    .toList(),
                onChanged: (value) {
                  setState(() {
                    selectedType = value;
                  });
                },
              ),
            ),
            ListTile(
              title: TextField(
                controller: dateController,
                readOnly: true,
                onTap: () => _pickDate(context),
                decoration: const InputDecoration(
                  labelText: "Date",
                ),
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
                  backgroundColor: Colors.lightBlue,
                ),
                onPressed: () {
                  if (_validateInputs()) {
                    var amount = double.parse(amountController.text);

                    Provider.of<ExpensesService>(context, listen: false).add(
                      amount,
                      selectedType!,
                      dateController.text,
                      noteController.text,
                    );

                    Navigator.pop(context);
                  }
                },
                child: const Text(
                  "Add Expense",
                  style: TextStyle(color: Colors.white),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}