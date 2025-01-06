import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:client/db/db.dart';
import 'package:client/screens/recipe_screen.dart';

import '../api/api.dart';
import '../util/messageResponse.dart';

bool categorySaved = false;

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

  List<String> categories = [];

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
    log("categoriesSaved: ${categorySaved}");
    if (categorySaved == false) {
      RecipeDB.instance.deleteAllcategories();
      for (var c in categories) {
        RecipeDB.instance.addCategory(c);
      }
      if (categories.isNotEmpty) {
        categorySaved = true;
      }
    }
    getData();
  }

  getData() async {
    setState(() => isLoading = true);
    if (online == true) {
      categories = await ApiService.instance.getCategories();
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text("Genres: online"),
      ));
      if (categorySaved == false) {
        syncData();
      }
    } else {
      if (categorySaved == false) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline categories - need to sync"),
        ));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline categories - synced"),
        ));
        categories = await RecipeDB.instance.getcategories();
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
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
          itemCount: categories.length,
          itemBuilder: (context, index) {
            return ListTile(
                title: Text(categories[index]),
                onTap: () {
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (_) =>
                              RecipePage(categories[index], online)));
                });
          }),
    );
  }

  @override
  void dispose() {
    // _networkConnectivity.myStream.listen((event) {}).pause();
    // _networkConnectivity.disposeStream();
    // TipDB.instance.close();
    super.dispose();
    subscription.cancel();
  }
}