import 'package:flutter/material.dart';
import 'package:client/screens/mainpage.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Recipe App',
      home: MainPage("Recipe App"),
    );
  }
}