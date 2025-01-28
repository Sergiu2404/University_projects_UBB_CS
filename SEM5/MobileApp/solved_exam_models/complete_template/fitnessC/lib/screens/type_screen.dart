import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:fitnessclient/model/entry.dart';
import 'package:fitnessclient/model/type.dart';
import 'package:fitnessclient/repo/api_repo.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../custom_widgets/custom_circular_progress.dart';
import '../custom_widgets/custom_request_message.dart';

class TypePage extends StatefulWidget {
  final bool isOnline;

  TypePage(this.isOnline);

  @override
  State<StatefulWidget> createState() => _TypePage();
}

class _TypePage extends State<TypePage> {
  bool online = true;
  bool isLoading = false;

  late StreamSubscription<ConnectivityResult> subscription;

  List<Entry> allEntries = [];
  List<TypeObj> topTypes = [];

  @override
  void initState() {
    super.initState();

    online = widget.isOnline;

    subscription = Connectivity()
        .onConnectivityChanged
        .listen((ConnectivityResult result) {
      if (result == ConnectivityResult.wifi ||
          result == ConnectivityResult.mobile) {
        online = true;
        getData();
        // syncData();
      } else {
        online = false;

        // getData();
      }
    });

    Future.delayed(Duration.zero, () {
      getData();
    });
  }

  getTopTips() async {
    // compute the top 3 types by total distance
    var types = allEntries
        .map((element) =>
        TypeObj(type: element.type, distance: element.distance))
        .toList();
    var typeCount = Map();
    types.forEach((element) {
      if (!typeCount.containsKey(element.type)) {
        typeCount[element.type] = element.distance;
      } else {
        typeCount[element.type] += element.distance;
      }
    });

    typeCount.forEach((key, value) {
      topTypes.add(TypeObj(type: key, distance: value));
    });

    topTypes.sort((b, a) => a.distance.compareTo(b.distance));
    topTypes = topTypes.take(3).toList();

  }

  getData() async {
    setState(() => isLoading = true);

    if (online == true) {
      allEntries = await API_Repo.instance.getAllEntries();
      getTopTips();
    } else {
      CustomRequestMessage(context, "Not available offline");
    }

    setState(() => isLoading = false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Release Year Section"),
      ),
      body: isLoading
          ?  Center(child: CustomCircularProgress(
          size: 40,
          color: Colors.blue,
          strokeWidth: 5))
          : ListView.builder(
          itemCount: topTypes.length,
          itemBuilder: (context, index) {
            return ListTile(
              title: Text(topTypes[index].type),
              subtitle:
              Text("Total distance: ${topTypes[index].distance}"),
            );
          }),
    );
  }

  @override
  void dispose() {
    super.dispose();
    subscription.cancel();
  }
}