import 'package:client/mainPage.dart';
import 'package:flutter/material.dart';

import 'homepage.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Gardening App',
      // home: HomePage("Gardening App")
      home: MainPage("Gardening App"),
    );
  }
}