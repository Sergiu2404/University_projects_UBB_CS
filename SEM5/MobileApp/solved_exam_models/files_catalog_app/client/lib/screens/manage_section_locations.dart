// ignore_for_file: use_build_context_synchronously

import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:client/repo/APIRepository.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../repo/DBRepository.dart';
import 'entity_by_location_section.dart';

bool locationSaved = false;

class ManageSection extends StatefulWidget {
  final String _title;
  final bool isOnline;
  ManageSection(this._title, this.isOnline);

  @override
  State<StatefulWidget> createState() => _ManageSection();
}

class _ManageSection extends State<ManageSection> {
  bool online = true;
  bool isLoading = false;

  List<String> locations = [];

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
    if (locationSaved == false) {
      DBRepository.instance.deleteAllLocations();
      for (var location in locations) {
        DBRepository.instance.addLocation(location);
      }
      if (locations.isNotEmpty) {
        locationSaved = true;
      }
    }
    getData();
  }

  getData() async {
    setState(() => isLoading = true);
    if (online == true) {
      try{
        locations = await APIRepository.instance.getLocations();
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Locations: online"),
        ));
        if (locationSaved == false) {
          syncData();
        }
      }
      on Exception catch(_){
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server get locations failed"),
        ));
      }
    } else {
      if (locationSaved == false) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline locations - need to sync. Hit the refresh button"),
        ));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline dates - synced"),
        ));
        locations = await DBRepository.instance.getLocations();
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
          itemCount: locations.length,
          itemBuilder: (context, index) {
            return ListTile(
                title: Text(locations[index]),
                onTap: () {
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (_) =>
                              EntityByLocationPage(locations[index], online)));
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