import 'dart:async';

import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:client/model/category.dart';
import 'package:client/tipPage.dart';
import 'package:flutter/material.dart';

import 'api/api.dart';
import 'api/network.dart';
import '../model/tip.dart';
import 'db/tip_db.dart';
import 'messageResponse.dart';

bool categorySaved = false;

class HomePage extends StatefulWidget {
  final String _title;
  final bool isOnline;
  // final NetworkConnectivity _networkConnectivity;
  HomePage(this._title, this.isOnline);
  // HomePage(this._title);

  @override
  State<StatefulWidget> createState() => _HomePage();
}

class _HomePage extends State<HomePage> {
  // Connectivity
  Map _source = {ConnectivityResult.none: false};
  final NetworkConnectivity _networkConnectivity = NetworkConnectivity.instance;
  String string = '';
  bool online = true;

  late List<Tip> tips;
  List<Category> categories = [];
  late List<Tip> tipsPerCategory;
  bool isLoading = false;

  bool isoffline = false;

  late StreamSubscription<ConnectivityResult> subscription;

  @override
  void initState() {
    super.initState();

    online = widget.isOnline;

    subscription = Connectivity()
        .onConnectivityChanged
        .listen((ConnectivityResult result) {
      print("Something new - categories ? " + result.toString());
      if (result == ConnectivityResult.wifi ||
          result == ConnectivityResult.mobile) {
        online = true;
        if (categorySaved == false) {
          syncData();
        }
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
    if (categorySaved == false) {
      for (var c in categories) {
        TipDB.instance.addCategory(c);
      }
      categorySaved = true;
    }
    getData();
  }

  getData() async {
    print('>>> categories ' + online.toString());
    // if (!mounted) return;
    // if (mounted)
    setState(() => isLoading = true);
    if (online == true) {
      categories = await ApiService.instance.getCategories();
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text("Online"),
      ));
    } else {
      if (categorySaved == false) {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(
          content: Text("Get offline categories - need to sync"),
        ));
        // messageResponse(
        //     context, "No internet. Connect to retrieve data from server.");
      } else {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(
          content: Text("Get offline categories - synced"),
        ));
        categories = await TipDB.instance.getAllCategories();
      }
    }
    // if (mounted)
    setState(() => isLoading = false);
  }

  getTipList(String category) async {
    if (!mounted) return;
    if (mounted) setState(() => isLoading = true);
    if (online == true) {
      tipsPerCategory = await ApiService.instance.getTipsByCategory(category);
    } else {
      print("Coming soon");
    }
    if (mounted) setState(() => isLoading = false);
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
                title: Text(categories[index].category),
                onTap: () {
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (_) =>
                              TipPage(categories[index].category, online)));
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