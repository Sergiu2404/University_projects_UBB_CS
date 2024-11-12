import 'package:expense_tracker_ui/MyHomePage.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Expense Tracker',
      theme: ThemeData(
        primaryColor: Colors.green,
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Expense Tracker'),
    );
  }
}
