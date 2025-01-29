import 'dart:async';
import 'dart:convert';

import 'package:client/custom_widgets/message_response.dart';
import 'package:client/repo/DBRepo.dart';
import 'package:client/screens/top_section.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:web_socket_channel/io.dart';
import 'package:web_socket_channel/web_socket_channel.dart';

import 'main_section.dart';
import 'progress_section.dart';

class MainPage extends StatefulWidget {
  final IOWebSocketChannel channel =
  IOWebSocketChannel.connect("ws://10.0.2.2:2404");
  MainPage();
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
      CustomMessageResponse(context, "Web Socket: " + d['type'] + ' was added!');
    });
  }

  @override
  void initState() {
    super.initState();

    //subscription to stream of events of connectivity
    subscription = Connectivity()
        .onConnectivityChanged
        .listen((ConnectivityResult result) {
      if (result == ConnectivityResult.wifi ||
          result == ConnectivityResult.mobile) {
        online = true;
      } else {
        online = false;
        CustomMessageResponse(
            context, "Main: No internet. Local data will be shown.");
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text("home page")),
        body: Align(
            alignment: Alignment.center,
            child: Column(
              children: [
                TextButton(
                    onPressed: () {
                      Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (_) =>
                                  HomePage("Main Section", online)));
                    },
                    child: const Text('Main Section')),
                TextButton(
                    onPressed: () {
                      Navigator.push(context,
                          MaterialPageRoute(builder: (_) => ProgressSection(online)));
                    },
                    child: const Text('Release Year Section')),
                TextButton(
                    onPressed: () {
                      Navigator.push(context,
                          MaterialPageRoute(builder: (_) => TopSection(online)));
                    },
                    child: const Text('Release Year Section')),
              ],
            )));
  }

  @override
  void dispose() {
    DBRepo.instance.close();
    super.dispose();
    subscription.cancel();
  }
}