// ignore_for_file: use_build_context_synchronously

import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import '../models/entity.dart';
import '../screens/property_section.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../api/api.dart';
import '../db/db.dart';
import '../util/messageResponse.dart';

bool addressSaved = false;

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

  List<Address> addresses = [];

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
    if (addressSaved == false) {
      EntityDB.instance.deleteAllAddresses();
      for (var d in addresses) {
        EntityDB.instance.addAddress(d);
      }
      if (addresses.isNotEmpty) {
        addressSaved = true;
      }
    }
    getData();
  }

  getData() async {
    setState(() => isLoading = true);
    if (online == true) {
      try{
        addresses = await ApiService.instance.getAddresses();
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Days: online"),
        ));
        if (addressSaved == false) {
          syncData();
        }
      }
      on Exception catch(_){
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server get failed"),
        ));
      }
    } else {
      if (addressSaved == false) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline dates - need to sync. Hit the refresh button"),
        ));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline dates - synced"),
        ));
        addresses = await EntityDB.instance.getAddresses();
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
          itemCount: addresses.length,
          itemBuilder: (context, index) {
            return ListTile(
                title: Text(addresses[index].address),
                onTap: () {
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (_) =>
                              PropertyPage(addresses[index].id!, online, addresses[index])));
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