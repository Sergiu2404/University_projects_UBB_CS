import 'dart:async';
import 'dart:convert';

import 'package:client/repo/DBRepository.dart';
import 'package:client/screens/top_section.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:client/screens/top_section.dart';
import 'package:web_socket_channel/io.dart';
import 'package:web_socket_channel/web_socket_channel.dart';

import '../util/message_response.dart';
import 'get_all_section.dart';
import 'manage_section_locations.dart';
import 'progress_section.dart';

class MainPage extends StatefulWidget {
  final String _title;
  final IOWebSocketChannel channel =
  IOWebSocketChannel.connect("ws://10.0.2.2:2404");
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
      messageResponse(context, "From socket: " + d['name'] + ' was added!');
    });
  }

  @override
  void initState() {
    super.initState();

    subscription = Connectivity()
        .onConnectivityChanged
        .listen((ConnectivityResult result) {
      if (result == ConnectivityResult.wifi ||
          result == ConnectivityResult.mobile) {
        online = true;
      } else {
        online = false;
        messageResponse(
            context, "Main: No internet. Local data will be shown.");
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
                      Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (_) =>
                                  ManageSection("Manage Section", online)));
                    },
                    child: const Text('Manage Section', style: TextStyle(fontSize: 20, color: Colors.black),)),

                TextButton(
                    onPressed: () {
                      Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (_) =>
                                  GetAllSection("Record Section", online)));
                    },
                    child: const Text('Record Section', style: TextStyle(fontSize: 20, color: Colors.black),)),
                // TextButton(
                //     onPressed: () {
                //       Navigator.push(context,
                //           MaterialPageRoute(builder: (_) => MonthPage(online)));
                //     },
                //     child: const Text('Progress Section')),
                // TextButton(
                //     onPressed: () {
                //       Navigator.push(
                //           context,
                //           MaterialPageRoute(
                //               builder: (_) => DoctorPage(online)));
                //     },
                //     child: const Text('Top Section'))
              ],
            )));
  }

  @override
  void dispose() {
    DBRepository.instance.close();
    super.dispose();
    subscription.cancel();
  }
}