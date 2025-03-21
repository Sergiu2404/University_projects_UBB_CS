import 'package:expense_tracker_db/screens/MyHomePage.dart';
import 'package:flutter/material.dart';

void main()
{
  runApp(MyApp());
}

MaterialColor createMaterialColor(Color color) {
  Map<int, Color> colorSwatch = {
    50: Color.fromRGBO(color.red, color.green, color.blue, 0.1),
    100: Color.fromRGBO(color.red, color.green, color.blue, 0.2),
    200: Color.fromRGBO(color.red, color.green, color.blue, 0.3),
    300: Color.fromRGBO(color.red, color.green, color.blue, 0.4),
    400: Color.fromRGBO(color.red, color.green, color.blue, 0.5),
    500: Color.fromRGBO(color.red, color.green, color.blue, 0.6),
    600: Color.fromRGBO(color.red, color.green, color.blue, 0.7),
    700: Color.fromRGBO(color.red, color.green, color.blue, 0.8),
    800: Color.fromRGBO(color.red, color.green, color.blue, 0.9),
    900: Color.fromRGBO(color.red, color.green, color.blue, 1.0),
  };

  return MaterialColor(color.value, colorSwatch);
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: "My Financial Tracker",
        theme: ThemeData(
            primarySwatch: createMaterialColor(Color(0xFF32A146))
        ),
        debugShowCheckedModeBanner: false,
        home: MyHomePage()
    );
  }
}
