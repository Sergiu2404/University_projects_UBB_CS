import 'dart:async';
import 'dart:convert';

import 'package:client/api/network.dart';
import 'package:client/difficultyPage.dart';
import 'package:client/homePage.dart';
import 'package:flutter/material.dart';

import 'package:connectivity_plus/connectivity_plus.dart';

import 'db/tip_db.dart';
import 'messageResponse.dart';

import 'package:web_socket_channel/io.dart';
import 'package:web_socket_channel/web_socket_channel.dart';

class MainPage extends StatefulWidget {
  final String _title;
  final IOWebSocketChannel channel =
  IOWebSocketChannel.connect("ws://10.0.2.2:2302");
  MainPage(this._title);
  @override
  State<StatefulWidget> createState() => _MainPage(channel: channel);
}

class _MainPage extends State<MainPage> {
  // Map _source = {ConnectivityResult.none: false};
  // final NetworkConnectivity _networkConnectivity = NetworkConnectivity.instance;
  // String string = '';
  final WebSocketChannel channel;
  bool online = true;

  late StreamSubscription<ConnectivityResult> subscription;

  _MainPage({required this.channel}) {
    channel.stream.listen((data) {
      var d = jsonDecode(data);
      print("Socket: >> " + d.toString());
      messageResponse(context, "From socket: " + d['name'] + ' was added!');
    });
  }

  @override
  void initState() {
    super.initState();

    subscription = Connectivity()
        .onConnectivityChanged
        .listen((ConnectivityResult result) {
      print("Something new - main page ? " + result.toString());
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
                Spacer(),
                TextButton(
                    onPressed: () {
                      Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (_) => HomePage("Gardening", online)));
                    },
                    child: const Text('Main Section')),
                TextButton(
                    onPressed: () {
                      Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (_) => DifficultyPage(online)));
                    },
                    child: const Text('Difficulty Section'))
              ],
            )));
  }

// void connection() {
//     _networkConnectivity.initialise();
//     _networkConnectivity.myStream.listen((source) {
//       _source = source;
//       print('source $_source');
//       var newStatus = true;
//       // Store connection type and status
//       switch (_source.keys.toList()[0]) {
//         case ConnectivityResult.mobile:
//           string =
//           _source.values.toList()[0] ? 'Mobile: FOnline' : 'Mobile: Offline';
//           break;
//         case ConnectivityResult.wifi:
//           string =
//           _source.values.toList()[0] ? 'WiFi: Online' : 'WiFi: Offline';
//           newStatus = _source.values.toList()[0] ? true : false;
//           break;
//         case ConnectivityResult.none:
//         default:
//           string = 'Offline';
//           newStatus = false;
//       }

//       // Refresh state to update connection status
//       // setState(() {});
//       if (newStatus != online) {
//         online = newStatus;
//         if (newStatus) {
//           // messageResponse(context, "Connection reestablished");
//           // getData();
//         } else {
//           // messageResponse(context, "No internet. Local data will be shown.");
//         }
//       }
//     });
//   }

  @override
  void dispose() {
    // _networkConnectivity.disposeStream();
    // TipDB.instance.close();
    super.dispose();
    subscription.cancel();
  }
}