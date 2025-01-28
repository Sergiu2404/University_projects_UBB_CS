import 'dart:async';
import 'dart:convert';

import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:fitnessclient/repo/db_repo.dart';
import 'package:fitnessclient/screens/all_entries_offline.dart';
import 'package:fitnessclient/screens/all_entries_and_reservations_page.dart';
import 'package:fitnessclient/screens/dates_main_section.dart';
import 'package:fitnessclient/screens/type_screen.dart';
import 'package:fitnessclient/screens/week_calories.dart';
import 'package:flutter/material.dart';
import 'package:web_socket_channel/io.dart';
import 'package:web_socket_channel/web_socket_channel.dart';

import 'custom_widgets/custom_request_message.dart';
import 'custom_widgets/loading_page.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Fitness Tracker App',
      initialRoute: '/',
      routes: {
        '/': (context) => MainPage("Fitness Tracker App"),
        '/loading': (context) => LoadingPage(),
        '/dates': (context) => DatesMainSection("Set of dates section", true),
        '/weekly': (context) => WeekPage(true),
        '/type': (context) => TypePage(true),
        '/entries': (context) => ReservationPage(true),
        '/allEntries': (context) => AllEntriesPage()
      },
    );
  }
}

class MainPage extends StatefulWidget {
  final String _title;
  final IOWebSocketChannel channel =
  IOWebSocketChannel.connect("ws://10.0.2.2:7496");

  MainPage(this._title);

  @override
  State<StatefulWidget> createState() => _MainPage(channel: channel);
}

class _MainPage extends State<MainPage> {
  final WebSocketChannel channel;
  bool online = true;

  late StreamSubscription<ConnectivityResult> subscription;

  _MainPage({required this.channel}) {
    channel.stream.listen((data) {
      var d = jsonDecode(data);
      CustomRequestMessage(context, "From socket: " + d['type'] + ' was added!');
    });
  }

  @override
  void initState() {
    super.initState();

    subscription = Connectivity().onConnectivityChanged.listen((ConnectivityResult result) {
      if (result == ConnectivityResult.wifi || result == ConnectivityResult.mobile) {
        online = true;
      } else {
        online = false;
        CustomRequestMessage(context, "Main: No internet. Local data will be shown.");
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(widget._title)),
      body: Align(
        alignment: Alignment.center,
        child: Column(
          children: [
            TextButton(
              onPressed: () {
                Navigator.pushNamed(context, '/loading');
              },
              child: const Text('Loading...'),
            ),
            TextButton(
              onPressed: () {
                Navigator.pushNamed(context, '/dates');
              },
              child: const Text('Set of dates section'),
            ),
            TextButton(
              onPressed: () {
                Navigator.pushNamed(context, '/weekly');
              },
              child: const Text('Weekly Section'),
            ),
            TextButton(
              onPressed: () {
                Navigator.pushNamed(context, '/type');
              },
              child: const Text('Type Section'),
            ),
            TextButton(
              onPressed: () {
                Navigator.pushNamed(context, '/entries');
              },
              child: const Text('Reservation Section'),
            ),
            TextButton(
              onPressed: () {
                Navigator.pushNamed(context, '/allEntries');
              },
              child: const Text('All Entries Section'),
            ),
          ],
        ),
      ),
    );
  }

  @override
  void dispose() {
    DB_Repo.instance.close();
    super.dispose();
    subscription.cancel();
  }
}
