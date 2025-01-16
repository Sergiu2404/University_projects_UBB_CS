import 'package:flutter/material.dart';
import 'package:client/screens/mainpage.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'CRUD App',
      home: MainPage("CRUD App"),
    );
  }
}