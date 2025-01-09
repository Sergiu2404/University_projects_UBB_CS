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

List<int> idSaved = [];

class SearchMorePage extends StatefulWidget {
  final String type;
  final String bedroom;
  final String price;

  SearchMorePage(this.type, this.bedroom, this.price);

  @override
  State<StatefulWidget> createState() => _SearchMorePage();
}

class _SearchMorePage extends State<SearchMorePage> {
  bool online = true;

  late int id;

  bool isLoading = false;
  List<Property> allProperties = [];
  List<Property> filteredProps = [];
  bool saved = false;

  late StreamSubscription<ConnectivityResult> subscription;

  @override
  void initState() {
    super.initState();
    subscription = Connectivity()
        .onConnectivityChanged
        .listen((ConnectivityResult result) {
      if (result == ConnectivityResult.wifi ||
          result == ConnectivityResult.mobile) {
        online = true;
        getSearchData();
      } else {
        online = false;
        getSearchData();
      }
    });

    Future.delayed(Duration.zero, () {
      getSearchData();
    });
  }

// The list should
// present the properties descending by date, and ascending by price
  getSearchData() async {
    setState(() => isLoading = true);
    if (online == true) {
      try {
        //SEARCH EXACTLY BY WANTED STRINGS
        // allProperties = await ApiService.instance.getAllEntries();
        // if (widget.type.isNotEmpty) {
        //   filteredProps = allProperties
        //       .where((element) => element.type == widget.type)
        //       .toList();
        // }
        // if (double.tryParse(widget.price) != null) {
        //   filteredProps = allProperties
        //       .where((element) => element.price == double.parse(widget.price))
        //       .toList();
        // }
        // if (int.tryParse(widget.bedroom) != null) {
        //   filteredProps = allProperties
        //       .where((element) => element.bedrooms == int.parse(widget.bedroom))
        //       .toList();
        // }

        //SEARCH ALSO SUBSTRINGS
        allProperties = await ApiService.instance.getAllEntries();

        // Filter properties by type (substring match)
        if (widget.type.isNotEmpty) {
          filteredProps = allProperties
              .where((element) => element.type.toLowerCase().contains(widget.type.toLowerCase()))
              .toList();
        } else {
          filteredProps = allProperties;
        }

        // Filter properties by price (exact match)
        if (double.tryParse(widget.price) != null) {
          filteredProps = filteredProps
              .where((element) => element.price == double.parse(widget.price))
              .toList();
        }

        // Filter properties by bedrooms (exact match)
        if (int.tryParse(widget.bedroom) != null) {
          filteredProps = filteredProps
              .where((element) => element.bedrooms == int.parse(widget.bedroom))
              .toList();
        }
        /**
         * pollsList.sort((a, b) {
            int cmp = b.actualStartDatetime.compareTo(a.actualStartDatetime);
            if (cmp != 0) return cmp;
            return b.active.compareTo(a.active);
            });
         */
        filteredProps.sort((a, b) {
          int cmp = b.date.compareTo(a.date);
          if (cmp == 0) {
            return a.price.compareTo(b.price);
          }
          return cmp;
        });
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
        title: Text("Properties search"),
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
          itemCount: filteredProps.length,
          itemBuilder: (context, index) {
            return ListTile(
              title: Text(filteredProps[index].id.toString()),
              subtitle: Text(getPropertyDetails(filteredProps[index])),
            );
          }),
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