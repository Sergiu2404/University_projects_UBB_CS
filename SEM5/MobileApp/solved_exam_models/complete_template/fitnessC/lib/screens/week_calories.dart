import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:fitnessclient/repo/api_repo.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../custom_widgets/custom_circular_progress.dart';
import '../custom_widgets/custom_request_message.dart';
import '../model/entry.dart';

class WeekPage extends StatefulWidget {
  final bool isOnline;

  WeekPage(this.isOnline);

  @override
  State<StatefulWidget> createState() => _WeekPage();
}

class _WeekPage extends State<WeekPage> {
  bool online = true;
  bool isLoading = false;

  late StreamSubscription<ConnectivityResult> subscription;

  List<Entry> allEntries = [];
  Map<String, int> caloriesPerWeek = Map();

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
        log("Sync");
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

  getCalsPerWeek() async {
    var caloryMap = Map();
    allEntries.sort((a, b) => a.date.compareTo(b.date));
    int startWeek =
    int.parse(allEntries[0].date.substring(allEntries[0].date.length - 2));
    log("Start day: $startWeek");
    String startWeekString = allEntries[0].date;
    caloryMap[startWeekString] = 0;

    for (var entry in allEntries) {
      int day = int.parse(entry.date.substring(entry.date.length - 2));
      if (day - startWeek < 7) {
        // if (caloriesPerWeek[startWeekString] != null)
        caloryMap[startWeekString] += entry.calories;
      } else {
        startWeek = day;
        startWeekString = entry.date;
        caloryMap[startWeekString] = entry.calories;
      }
    }

    caloryMap.forEach((key, value) {
      caloriesPerWeek[key] = value;
    });

    log("Calory map: ${caloriesPerWeek}");

  }

  getData() async {
    setState(() => isLoading = true);

    if (online == true) {
      allEntries = await API_Repo.instance.getAllEntries();
      getCalsPerWeek();
    } else {
      CustomRequestMessage(context, "Not available offline");
    }

    setState(() => isLoading = false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Weekly Calories Section"),
      ),
      body: isLoading
          ? Center(
              child: CustomCircularProgress(
                  size: 40,
                  color: Colors.blue,
                  strokeWidth: 5))
          : ListView.builder(
          itemCount: caloriesPerWeek.length,
          itemBuilder: (context, index) {
            String key = caloriesPerWeek.keys.elementAt(index);
            return ListTile(
              title: Text('$key'),
              subtitle:
              Text('${caloriesPerWeek[key]} calories '),
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