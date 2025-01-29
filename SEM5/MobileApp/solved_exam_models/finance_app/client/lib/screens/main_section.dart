// ignore_for_file: use_build_context_synchronously

import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../api/api.dart';
import '../db/db.dart';
import 'entity_section.dart';

bool daySaved = false;

class HomePage extends StatefulWidget {
  final String _title;
  final bool isOnline;
  HomePage(this._title, this.isOnline);

  @override
  State<StatefulWidget> createState() => _HomePage();
}

class _HomePage extends State<HomePage> {
  bool online = true;
  bool isLoading = false;

  List<String> days = [];

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
    if (daySaved == false) {
      DatabaseContext.instance.deleteAllDays();
      for (var d in days) {
        DatabaseContext.instance.addDay(d);
      }
      if (days.isNotEmpty) {
        daySaved = true;
      }
    }
    getData();
  }

  getData() async {
    setState(() => isLoading = true);
    if (online == true) {
      try{
        days = await ApiService.instance.getDays();
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Days: online"),
        ));
        if (daySaved == false) {
          syncData();
        }
      }
      on Exception catch(_){
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server get days failed"),
        ));
      }
    } else {
      if (daySaved == false) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline dates - need to sync. Hit the refresh button"),
        ));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline dates - synced"),
        ));
        days = await DatabaseContext.instance.getDates();
      }
    }
    setState(() => isLoading = false);
  }

  void _refreshAction() {
    setState(() {
      getData();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget._title),
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
          itemCount: days.length,
          itemBuilder: (context, index) {
            return ListTile(
                title: Text(days[index]),
                onTap: () {
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (_) =>
                              SymptomPage(days[index], online)));
                });
          }),
      floatingActionButton: FloatingActionButton(
        onPressed: _refreshAction,
        tooltip: 'Refresh page',
        child: const Icon(Icons.refresh),
      ),
    );
  }

  @override
  void dispose() {
    super.dispose();
    subscription.cancel();
  }
}