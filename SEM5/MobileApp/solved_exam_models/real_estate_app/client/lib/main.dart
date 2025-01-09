import 'package:flutter/material.dart';

import 'screens/mainpage.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Exam App',
      home: MainPage("Exam App"),
    );
  }
}