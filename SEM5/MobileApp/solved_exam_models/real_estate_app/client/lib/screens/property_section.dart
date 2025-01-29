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

class PropertyPage extends StatefulWidget {
  final int id;
  final Address address;
  final bool isOnline;

  PropertyPage(this.id, this.isOnline, this.address);

  @override
  State<StatefulWidget> createState() => _PropertyPage();
}

class _PropertyPage extends State<PropertyPage> {
  bool online = true;

  late int id;

  bool isLoading = false;
  List<Property> properties = [];
  Property p = Property(
      date: '-',
      type: '-',
      address: '-',
      bedrooms: 0,
      bathrooms: 0,
      area: 0,
      price: 0,
      notes: '-');
  bool saved = false;

  late StreamSubscription<ConnectivityResult> subscription;

  @override
  void initState() {
    super.initState();
    online = widget.isOnline;
    id = widget.id;
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

  bool checkSaved() {
    final ids = idSaved.where((element) => element == widget.id);
    return ids.isEmpty;
  }

  getData() async {
    setState(() => isLoading = true);
    if (online == true) {
      try {
        p = await ApiService.instance.getPropertiesByAddress(id);
        properties = [];
        properties.add(p);
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Back at it!"),
        ));
        syncData();
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server get properties for id failed"),
        ));
      }
    } else {
      if (checkSaved() == false) {
        p = await EntityDB.instance.getById(id);
        properties = [];
        properties.add(p);
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Offline properties are currently being displayed"),
        ));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Offline properties have not been saved yet to db"),
        ));
      }
    }
    setState(() => isLoading = false);
  }

  syncData() async {
    log("Syncing >> ${checkSaved()}");
    if (checkSaved()) {
      EntityDB.instance.deleteAllPropertiesByAddress(id);
      // sync data and add category to list
      for (var p in properties) {
        EntityDB.instance.addProperty(p);
      }
      idSaved.add(id);
    }
  }

  void addEntry(Property p) async {
    setState(() => isLoading = true);
    if (online) {
      try {
        final Property newProperty = await ApiService.instance.addEntry(p);
        setState(() {
          if (p.address == widget.address.address) {
            properties.add(p);
          }
        });
        // newProperty.id = 100;
        EntityDB.instance.addProperty(newProperty);
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Add failed"),
        ));
      }
    }
    setState(() => isLoading = false);
  }

  void deleteEntryBack(Property p) async {
    setState(() => isLoading = true);
    if (online) {
      try {
        ApiService.instance.deleteEntry(p.id!);
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server delete failed"),
        ));
      }
      setState(() {
        properties.remove(p);
        Navigator.pop(context);
        EntityDB.instance.deleteEntry(p.id!);
      });
    }
    setState(() => isLoading = false);
  }

  void deleteEntry(BuildContext context, Property p) {
    showDialog(
        context: context,
        builder: (_) => AlertDialog(
          title: Text("Delete Property"),
          content: Text("Are you sure you want to delete p: ${p.type}?"),
          actions: [
            TextButton(
                onPressed: () {
                  deleteEntryBack(p);
                },
                child: Text("Yes", style: TextStyle(color: Colors.red))),
            TextButton(
                onPressed: () {
                  Navigator.pop(context);
                },
                child: Text("No", style: TextStyle(color: Colors.green)))
          ],
        ));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Properties for ${widget.address.address}"),
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
          itemCount: properties.length,
          itemBuilder: (context, index) {
            return ListTile(
              title: Text(properties[index].id.toString()),
              subtitle: Text(getPropertyDetails(properties[index])),
              onTap: () {},
              trailing: IconButton(
                icon: const Icon(Icons.delete),
                color: Colors.amber,
                onPressed: () {
                  if (online) {
                    deleteEntry(context, properties[index]);
                  } else {
                    ScaffoldMessenger.of(context)
                        .showSnackBar(const SnackBar(
                      content: Text("Cannot delete while offline"),
                    ));
                  }
                },
              ),
            );
          }),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          if (online) {
            Navigator.push(
              context,
              MaterialPageRoute(
                builder: (_) => AddPage(address: widget.address), // Pass the address here
              ),
            ).then((newEntry) {
              if (newEntry != null) {
                setState(() {
                  addEntry(newEntry);
                });
              }
            });
          } else {
            ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
              content: Text("Cannot add while offline"),
            ));
          }
        },
        tooltip: "Add Entry",
        child: Icon(Icons.add),
      ),

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