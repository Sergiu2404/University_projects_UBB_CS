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
  int size = 0;

  void _addHardcodedExpenses() async {
    // Check if database already has expenses to prevent duplicates
    int? currentSize = await databaseContext.getSize();

    if (currentSize == 0) { // Only add if database is empty
      await databaseContext.insertExpense(
          Expense.withId(1, 3925, "RECEIVING", "Salary", "10.10.2024", "wage for the month of september")
      );
      await databaseContext.insertExpense(
          Expense.withId(2, 660, "RECEIVING", "Meal tickets", "05.10.2024", "..")
      );
    }

    // Refresh the list view to load items from the database
    refreshListView();
  }

  @override
  void initState() {
    super.initState();
    _addHardcodedExpenses();  // Test if hardcoded data appears initially
    refreshListView(); // Ensure list view refresh happens during initialization
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
                final result = await Navigator.push(context, MaterialPageRoute(builder: (context) => const AddExpense()));

                // If an expense was added, refresh the list
                if (result == true) {
                  refreshListView();
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
            itemCount: size,
            itemBuilder: (BuildContext context, int position) {
              final expense = expenses[position]; // Get the current expense
              return Card(
                color: Color(0xFFC1F7C7),
                elevation: 2.0,
                margin: const EdgeInsets.symmetric(vertical: 8, horizontal: 16),
                child: Container(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Expanded(
                            child: Text(
                              "${expense.amount} RON", // Amount
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
                                "${expense.id}", // Expense ID
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
                                              MaterialPageRoute(builder: (context) => ViewExpense(expense: expense)),
                                            );
                                            if (result == true) {
                                              refreshListView(); // Reload the list if an update was successful
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

  void _delete(BuildContext context, Expense expense) async {
    int result = await databaseContext.deleteExpense(expense.id!);
    if (result != 0) {
      _showSnackBar(context, "Expense deleted successfully");
      refreshListView();
    }
  }

  void _showSnackBar(BuildContext context, String message) {
    final snackBar = SnackBar(content: Text(message));
    ScaffoldMessenger.of(context).showSnackBar(snackBar);
  }

  void refreshListView() {
    final Future<Database> dbFuture = databaseContext.initializeDatabase();
    dbFuture.then((database) {
      Future<List<Expense>> expensesListFuture = databaseContext.getExpensesList();
      expensesListFuture.then((expensesList) {
        setState(() {
          this.expenses = expensesList;
          this.size = expensesList.length;
        });
      });
    });
  }
}