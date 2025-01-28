import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:fitnessclient/repo/api_repo.dart';
import 'package:fitnessclient/repo/db_repo.dart';
import 'package:fitnessclient/screens/entries_by_date_page.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../custom_widgets/custom_circular_progress.dart';
import '../custom_widgets/custom_request_message.dart';

bool dateSaved = false;

class DatesMainSection extends StatefulWidget {
  final String _title;
  final bool isOnline;
  DatesMainSection(this._title, this.isOnline);

  @override
  State<StatefulWidget> createState() => _DatesMainSection();
}

class _DatesMainSection extends State<DatesMainSection> {
  bool online = true;
  bool isLoading = false;

  List<String> dates = [];

  late StreamSubscription<ConnectivityResult> subscription;

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
        syncData();
      } else {
        online = false;
        getData();
      }
    });

    Future.delayed(Duration.zero, () {
      getData();
    });
  }

  syncData() async {
    if (dateSaved == false) {
      DB_Repo.instance.deleteAllDates();
      for (var d in dates) {
        DB_Repo.instance.addDate(d);
      }
      if (dates.isNotEmpty) {
        dateSaved = true;
      }
    }
    getData();
  }

  getData() async {
    setState(() => isLoading = true);
    if (online == true) {
      dates = await API_Repo.instance.getDates();
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text("Dates: online"),
      ));
      if (dateSaved == false) {
        syncData();
      }
    } else {
      if (dateSaved == false) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline dates - need to sync"),
        ));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline dates - synced"),
        ));
        dates = await DB_Repo.instance.getDates();
      }
    }
    setState(() => isLoading = false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget._title),
      ),
      body: isLoading
          ? Center(child: CustomCircularProgress(
          size: 70,
          color: Colors.red,
          strokeWidth: 8))
          : ListView.builder(
          itemCount: dates.length,
          itemBuilder: (context, index) {
            return ListTile(
                title: Text(dates[index]),
                onTap: () {
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (_) =>
                              EntriesByDatePage(dates[index], online)));
                });
          }),
    );
  }

  @override
  void dispose() {
    super.dispose();
    subscription.cancel();
  }
}