import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:client/repo/APIRepo.dart';
import 'package:client/repo/DBRepo.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../api/api.dart';
import '../db/db.dart';
import 'entity_section.dart';

bool flag = false;

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

  List<String> genres = [];

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
    if (flag == false) {
      DBRepo.instance.deleteAllGenres();
      for (var genre in genres) {
        DBRepo.instance.addGenre(genre);
      }
      if (genres.isNotEmpty) {
        flag = true;
      }
    }
    getData();
  }

  getData() async {
    setState(() => isLoading = true);
    if (online == true) {
      try{
        genres = await APIRepo.instance.getGenres();
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Genres: online"),
        ));
        if (flag == false) {
          syncData();
        }
      }
      on Exception catch(_){
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server get genres failed"),
        ));
      }
    } else {
      if (flag == false) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline genres - need to sync. Hit the refresh button"),
        ));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline genres - synced"),
        ));
        genres = await DBRepo.instance.getGenres();
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
          itemCount: genres.length,
          itemBuilder: (context, index) {
            return ListTile(
                title: Text(genres[index]),
                onTap: () {
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (_) =>
                              MoviesPage(genres[index], online)));
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