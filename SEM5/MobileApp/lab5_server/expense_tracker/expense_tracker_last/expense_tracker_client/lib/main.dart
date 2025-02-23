import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../services/expenses_service.dart';
import '../views/home_page.dart';

void main() async {

  var service = await ExpensesService.init();

  runApp(
    ChangeNotifierProvider(
      create: (_) => service,
      child: const MyApp(),
    ),
  );
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      title: 'My Financial Tracker',
      home: MyMainPage(),
    );
  }
}

class MyMainPage extends StatelessWidget {
  const MyMainPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFF32A146),
        title: const Text(
          'My Financial Tracker',
          style: TextStyle(color: Colors.black, fontSize: 17),
        ),
      ),
      body: const Column(
        children: [
          ConnectionStatusWidget(),
          Expanded(
            child: HomePageWidget(),
          ),
        ],
      ),
    );
  }
}

class ConnectionStatusWidget extends StatelessWidget {
  const ConnectionStatusWidget({super.key});

  @override
  Widget build(BuildContext context) {
    return Consumer<ExpensesService>(
      builder: (context, service, child) {
        return Container(
          color: service.is_online ? Colors.lightGreen : Colors.red,
          width: double.infinity,
          padding: const EdgeInsets.all(8),
          child: Text(
            service.is_online ? 'Online' : 'Offline',
            style: const TextStyle(color: Colors.white),
            textAlign: TextAlign.center,
          ),
        );
      },
    );
  }
}