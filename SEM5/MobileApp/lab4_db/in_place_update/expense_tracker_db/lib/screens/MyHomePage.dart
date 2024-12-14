import 'package:expense_tracker_db/screens/AddExpense.dart';
import 'package:expense_tracker_db/screens/ViewExpense.dart';
import 'package:flutter/material.dart';
import 'dart:async';
import 'package:expense_tracker_db/models/Expense.dart';
import 'package:expense_tracker_db/utils/DatabaseContext.dart';
import 'package:sqflite/sqflite.dart';

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  DatabaseContext databaseContext = DatabaseContext();
  List<Expense> expenses = [];

  @override
  void initState() {
    super.initState();
    _loadExpenses();
  }


  // Load expenses only once during initialization
  Future<void> _loadExpenses() async {
    final expensesList = await databaseContext.getExpensesList();
    setState(() {
      expenses = expensesList;
    });
  }

  // Modify the delete method to update the list in-place
  void _delete(BuildContext context, Expense expense) async {
    int result = await databaseContext.deleteExpense(expense.id!);
    if (result != 0) {
      setState(() {
        // Remove the expense directly from the list
        expenses.removeWhere((e) => e.id == expense.id);
      });
      _showSnackBar(context, "Expense deleted successfully");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("My Financial Tracker"),
        backgroundColor: Color(0xFF32A146),
      ),
      body: _getExpenseListView(),
    );
  }

  Widget _getExpenseListView() {
    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.all(16.0),
          child: Text(
            "Expenses List",
            style: TextStyle(
              fontSize: 24,
              fontWeight: FontWeight.bold,
            ),
            textAlign: TextAlign.center,
          ),
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            ElevatedButton(
              onPressed: () async {
                // Await the result from the AddExpense screen
                final result = await Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => const AddExpense())
                );

                // If an expense was added, add it to the list
                if (result is Expense) {
                  setState(() {
                    expenses.add(result);
                  });
                }
              },
              style: ElevatedButton.styleFrom(
                padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 24),
                backgroundColor: Colors.transparent,
                shadowColor: Colors.transparent,
              ),
              child: Text(
                "Add Expense",
                style: TextStyle(
                  color: Colors.black,
                  fontSize: 17,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
          ],
        ),
        SizedBox(height: 16),
        Expanded(
          child: ListView.builder(
            itemCount: expenses.length,
            itemBuilder: (BuildContext context, int position) {
              final expense = expenses[position];
              return Card(
                color: Color(0xFFC1F7C7),
                elevation: 2.0,
                margin: const EdgeInsets.symmetric(vertical: 8, horizontal: 16),
                child: Container(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // ... (rest of the UI remains the same)
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Expanded(
                            child: Text(
                              "${expense.amount} RON",
                              style: TextStyle(
                                fontSize: 16,
                                fontWeight: FontWeight.bold,
                                color: Colors.black,
                              ),
                            ),
                          ),
                          Expanded(
                            child: Center(
                              child: Text(
                                "${expense.id}",
                                style: TextStyle(
                                  fontSize: 14,
                                  fontWeight: FontWeight.bold,
                                  color: Colors.black,
                                ),
                              ),
                            ),
                          ),
                          Expanded(
                            child: ElevatedButton(
                              onPressed: () {
                                showDialog(
                                  context: context,
                                  builder: (BuildContext context) {
                                    return AlertDialog(
                                      title: Text("Options"),
                                      content: Text("Choose an option"),
                                      actions: [
                                        TextButton(
                                          onPressed: () async {
                                            final result = await Navigator.push(
                                              context,
                                              MaterialPageRoute(
                                                  builder: (context) => ViewExpense(expense: expense)
                                              ),
                                            );

                                            // If the expense was updated, replace it in the list
                                            if (result is Expense) {
                                              setState(() {
                                                int index = expenses.indexWhere((e) => e.id == result.id);
                                                if (index != -1) {
                                                  expenses[index] = result;
                                                }
                                              });
                                            }
                                          },
                                          child: Text("View"),
                                        ),
                                        TextButton(
                                          onPressed: () {
                                            _delete(context, expense);
                                            Navigator.of(context).pop(); // Close the dialog
                                          },
                                          child: Text("Delete"),
                                        ),
                                      ],
                                    );
                                  },
                                );
                              },
                              style: ElevatedButton.styleFrom(
                                backgroundColor: Colors.blue,
                              ),
                              child: Text(
                                "More",
                                style: TextStyle(
                                  fontSize: 14,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                      SizedBox(height: 8),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Expanded(
                            child: Text(
                              expense.category, // Expense category
                              style: TextStyle(
                                fontSize: 14,
                                fontWeight: FontWeight.bold,
                                color: Colors.black,
                              ),
                            ),
                          ),
                          Expanded(
                            child: Center(
                              child: Text(
                                expense.date, // Expense date
                                style: TextStyle(
                                  fontSize: 14,
                                  fontWeight: FontWeight.bold,
                                  color: Colors.black,
                                ),
                              ),
                            ),
                          ),
                          Expanded(
                            child: Text(
                              expense.type, // Expense type
                              style: TextStyle(
                                fontSize: 14,
                                fontWeight: FontWeight.bold,
                                color: Colors.black,
                              ),
                              textAlign: TextAlign.end,
                            ),
                          ),
                        ],
                      ),
                      SizedBox(height: 8),
                      Text(
                        expense.note, // Description
                        style: TextStyle(
                          fontSize: 14,
                          fontWeight: FontWeight.bold,
                          color: Colors.black,
                        ),
                      ),
                      // ... (rest of the UI remains the same)
                    ],
                  ),
                ),
              );
            },
          ),
        ),
      ],
    );
  }

  void _showSnackBar(BuildContext context, String message) {
    final snackBar = SnackBar(content: Text(message));
    ScaffoldMessenger.of(context).showSnackBar(snackBar);
  }
}