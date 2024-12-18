import 'package:flutter/material.dart';
import 'package:expense_tracker_db/models/Expense.dart';
import 'package:expense_tracker_db/utils/DatabaseContext.dart';

class AddExpense extends StatefulWidget {
  const AddExpense({super.key});

  @override
  State<AddExpense> createState() => _AddExpenseState();
}

class _AddExpenseState extends State<AddExpense> {
  final TextEditingController _amountController = TextEditingController();
  final TextEditingController _typeController = TextEditingController();
  final TextEditingController _categoryController = TextEditingController();
  final TextEditingController _dateController = TextEditingController();
  final TextEditingController _noteController = TextEditingController();

  String? _selectedType;
  DatabaseContext databaseContext = DatabaseContext();

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        _moveToLastScreen();
        return true;
      },
      child: Scaffold(
        appBar: AppBar(
          title: Text("My Financial Tracker"),
          backgroundColor: Color(0xFF32A146),
        ),
        body: Padding(
          padding: const EdgeInsets.all(25.0),
          child: Container(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Text("Amount"),
                TextField(
                  controller: _amountController,
                  keyboardType: TextInputType.number,
                  decoration: InputDecoration(hintText: "Enter amount..."),
                ),
                SizedBox(height: 8,),

                Text("Type"),
                DropdownButton<String>(
                  hint: Text("Select type"),
                  value: _selectedType,
                  items: <String>['SPENDING', 'RECEIVING'].map((String value) {
                    return DropdownMenuItem<String>(
                      value: value,
                      child: Text(value),
                    );
                  }).toList(),
                  onChanged: (String? newValue) {
                    setState(() {
                      _selectedType = newValue;
                    });
                  },
                ),
                SizedBox(height: 8,),

                Text("Category"),
                TextField(
                  controller: _categoryController,
                  keyboardType: TextInputType.text,
                  decoration: InputDecoration(hintText: "Enter category..."),
                ),
                SizedBox(height: 8,),

                Text("Date"),
                TextField(
                  controller: _dateController,
                  readOnly: true, // Prevents manual text editing
                  decoration: InputDecoration(
                    hintText: "Select date...",
                    suffixIcon: Icon(Icons.calendar_today), // Optional calendar icon
                  ),
                  onTap: () async {
                    DateTime? pickedDate = await showDatePicker(
                      context: context,
                      initialDate: DateTime.now(), // Default date to show in the picker
                      firstDate: DateTime(2000), // Minimum selectable date
                      lastDate: DateTime(2100), // Maximum selectable date
                    );

                    if (pickedDate != null) {
                      // Format the selected date as a string (e.g., "dd/MM/yyyy")
                      String formattedDate = "${pickedDate.day}/${pickedDate.month}/${pickedDate.year}";
                      setState(() {
                        _dateController.text = formattedDate; // Update the controller
                      });
                    }
                  },
                ),
                SizedBox(height: 8,),

                Text("Notes"),
                TextField(
                  controller: _noteController,
                  keyboardType: TextInputType.text,
                  decoration: InputDecoration(hintText: "Enter notes..."),
                ),
                SizedBox(height: 20,),

                Center(
                  child: ElevatedButton(
                    onPressed: () {
                      _addExpense();
                    },
                    child: Text(
                      "Submit new expense",
                      style: TextStyle(
                        fontSize: 15,
                        fontWeight: FontWeight.bold,
                        color: Colors.black,
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  void _moveToLastScreen() {
    Navigator.pop(context);
  }

  void _addExpense() async {
    if (_amountController.text.isEmpty || _selectedType == null || _categoryController.text.isEmpty || _dateController.text.isEmpty || _noteController.text.isEmpty) {
      _showSnackBar(context, "Please fill in all fields.");
      return;
    }

    double amount = double.parse(_amountController.text);
    String type = _selectedType!;
    String category = _categoryController.text;
    String date = _dateController.text;
    String note = _noteController.text;

    Expense newExpense = Expense.withId(
      null, // ID will be auto gen
      amount,
      type,
      category,
      date,
      note,
    );

    // Insert the expense and get the inserted expense with its new ID
    int newId = await databaseContext.insertExpense(newExpense);

    // Create a new Expense object with the assigned ID
    newExpense = Expense.withId(
      newId,
      amount,
      type,
      category,
      date,
      note,
    );

    _showSnackBar(context, "Expense added successfully");

    // Return the new expense back to the home page
    Navigator.pop(context, newExpense);
  }


  void _showSnackBar(BuildContext context, String message) {
    final snackBar = SnackBar(content: Text(message));
    ScaffoldMessenger.of(context).showSnackBar(snackBar);
  }
}
