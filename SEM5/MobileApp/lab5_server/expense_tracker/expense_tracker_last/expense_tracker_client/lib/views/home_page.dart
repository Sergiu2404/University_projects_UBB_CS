
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../services/expenses_service.dart';
import '../utils/pair.dart';
import 'add_page.dart';
import 'expense_widget.dart';

class HomePageWidget extends StatelessWidget {
  const HomePageWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Navigator.of(context).push(
              MaterialPageRoute<void>(builder: (context) => const ExpensesAddPage())
          );
        },
        backgroundColor: Colors.green,
        child: const Icon(Icons.add, color: Colors.white),
      ),
      body: Consumer<ExpensesService>( //rebuild widget when state of the provider(service) changes
        builder: (context, expensesService, child) {
          return FutureBuilder<Pair>( //build widget based on latest snapshot of get all expenses future
              future: expensesService.getAllExpenses(),
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return const CircularProgressIndicator();
                }
                if (!snapshot.hasData || snapshot.data!.left.isEmpty) {
                  return const Center(child: Text("No items available"));
                }

                if (snapshot.hasError) {
                  return Center(child: Text("Error: ${snapshot.error}"));
                }

                var expenses = snapshot.data!.left;
                return ListView.builder(
                    itemCount: expenses.length,
                    itemBuilder: (context, index) {
                      return ExpenseWidget(expense: expenses[index]);
                    }
                );
              }
          );
        },
      ),
    );
  }
}