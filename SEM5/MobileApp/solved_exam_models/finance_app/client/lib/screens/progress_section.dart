import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../api/api.dart';
import '../model/entity.dart';
import '../util/message_response.dart';
import 'package:week_of_year/week_of_year.dart';
import 'package:intl/intl.dart';

class WeekAmount{
  int week;
  int amount;

  WeekAmount(this.week, this.amount);
}

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
  List<WeekAmount> weekAmountList = [];


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
      } else {
        online = false;
      }
    });

    Future.delayed(Duration.zero, () {
      getData();
    });
  }

  Future<void> getWeeks() async {
    List<WeekAmount> weekAmounts = [];
    DateFormat dateFormat = DateFormat("yyyy-MM-dd");

    for (var elem in allEntries) {
      DateTime dateTime = dateFormat.parse(elem.date);
      int week = dateTime.weekOfYear;

      // flag to see if the week is found
      bool found = false;

      for (var weekAmount in weekAmounts) {
        if (weekAmount.week == week) {
          weekAmount.amount += elem.amount;
          found = true;
          break;
        }
      }

      if (!found) {
        weekAmounts.add(WeekAmount(week, elem.amount));
      }

      log("Week of year for ${elem.date}: $week");
    }

    setState(() {
      weekAmountList = weekAmounts;
    });

    for (var weekAmount in weekAmounts) {
      log("Week ${weekAmount.week}, Total Amount: ${weekAmount.amount}");
    }
  }

  getData() async {
    setState(() => isLoading = true);

    if (online == true) {
      try {
        allEntries = await ApiService.instance.getAllEntries();
        getWeeks();
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server get failed"),
        ));
      }
    } else {
      messageResponse(context, "Not available offline");
    }

    setState(() => isLoading = false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text("Weekly Section"),
        ),
        body: isLoading
            ? const Center(child: CircularProgressIndicator())
            : Column(
          children: [
            Expanded(
                child: ListView.builder(
                  itemCount: weekAmountList.length,
                  itemBuilder: (context, index) {
                    final weekData = weekAmountList[index];
                    return ListTile(
                      title: Text("Week ${weekData.week}"),
                      subtitle: Text("Total Amount: ${weekData.amount}"),
                    );
                  }
                ),
              )
          ],
        )

      // ListView.builder(
      //     itemCount: symptoms.length,
      //  itemBuilder: (context, index) {
      //   return ListTile(
      //     title: Text('Month ${symptoms[index].month}'),
      //     subtitle:
      //         Text("Symptom Count: ${symptoms[index].symptomCount}"),
      //   );
      //     }),
    );
  }

  @override
  void dispose() {
    super.dispose();
    subscription.cancel();
  }
}