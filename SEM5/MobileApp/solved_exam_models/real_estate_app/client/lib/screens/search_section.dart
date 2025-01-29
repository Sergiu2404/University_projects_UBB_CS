import 'package:client/screens/search_props.dart';
import 'package:week_of_year/week_of_year.dart';
import 'package:intl/intl.dart';
import '../util/text_box.dart';

// ignore_for_file: use_build_context_synchronously

import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../api/api.dart';
import '../db/db.dart';
import '../models/entity.dart';
import '../util/messageResponse.dart';
import 'add_screen.dart';

List<String> dateSaved = [];

class SearchPage extends StatefulWidget {
  final bool isOnline;

  SearchPage(this.isOnline);

  @override
  State<StatefulWidget> createState() => _SearchPage();
}

class _SearchPage extends State<SearchPage> {
  bool online = true;

  late String date;

  bool isLoading = false;
  List<Property> allProperties = [];
  bool saved = false;

  late StreamSubscription<ConnectivityResult> subscription;

  late TextEditingController controllerPrice;
  late TextEditingController controllerType;
  late TextEditingController controllerBedroomCount;

  @override
  void initState() {
    controllerPrice = TextEditingController();
    controllerType = TextEditingController();
    controllerBedroomCount = TextEditingController();
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
        getData();
      }
    });

    Future.delayed(Duration.zero, () {
      getData();
    });
  }


  getData() async {
    setState(() => isLoading = true);
    if (online == true) {
      try {
        allProperties = await ApiService.instance.getAllEntries();
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Back at it!"),
        ));
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server get entries for date failed"),
        ));
      }
    }
    setState(() => isLoading = false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Search Properties"),
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView(children: [
        TextBox(controllerType, "Search by Type"),
        TextBox(controllerBedroomCount, "Search by bedroom"),
        TextBox(controllerPrice, "Search by price"),
        ElevatedButton(
            onPressed: () {
              String type = controllerType.text;
              String bedroom = controllerBedroomCount.text;
              String price = controllerPrice.text;

              Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (_) =>
                          SearchMorePage(type, bedroom, price)));
            },
            child: Text("Search")),
        // Flexible(child:
        // ListView.builder(
        //     scrollDirection: Axis.vertical,
        //     shrinkWrap: true,
        //     itemCount: allProperties.length,
        //     itemBuilder: (context, index) {
        //       return ListTile(
        //         title: Text(allProperties[index].id.toString()),
        //         subtitle: Text(getPropertyDetails(allProperties[index])),
        //         onTap: () {},
        //       );
        //     }),)
      ]),
    );
  }

  String getPropertyDetails(Property p) {
    return "Date: ${p.date}\nType: ${p.type}\nAddress: ${p.address}\nBedrooms: ${p.bedrooms}\nBathrooms ${p.bathrooms}\nPrice ${p.price}\nArea: ${p.area}\nNotes: ${p.notes}";
  }

  @override
  void dispose() {
    super.dispose();
    subscription.cancel();
  }
}