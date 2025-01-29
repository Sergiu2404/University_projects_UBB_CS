import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:client/repo/APIRepository.dart';
import 'package:client/repo/DBRepository.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../model/entity.dart';
import '../util/message_response.dart';
import 'add_screen.dart';

List<String> locationSaved = [];

class EntityByLocationPage extends StatefulWidget {
  final String location;
  final bool isOnline;

  EntityByLocationPage(this.location, this.isOnline);

  @override
  State<StatefulWidget> createState() => _EntityByLocationPage();
}

class _EntityByLocationPage extends State<EntityByLocationPage> {
  bool online = true;

  late String location;

  bool isLoading = false;
  List<Entry> recordsPerLocation = [];
  bool saved = false;

  late StreamSubscription<ConnectivityResult> subscription;

  @override
  void initState() {
    super.initState();
    online = widget.isOnline;
    location = widget.location;
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
    final locationsSet = recordsPerLocation.where((element) => element == widget.location);
    return locationsSet.isEmpty;
  }

  getData() async {
    setState(() => isLoading = true);
    if (online == true) {
      try {
        recordsPerLocation = await APIRepository.instance.getEntriesByLocation(location);
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Back at it!"),
        ));
        syncData();
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server get entries for location failed"),
        ));
      }
    } else {
      if (checkSaved() == false) {
        recordsPerLocation = await DBRepository.instance.getByLocation(location);
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Offline entries are currently being displayed"),
        ));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Offline entries have not been saved yet to db"),
        ));
      }
    }
    setState(() => isLoading = false);
  }

  syncData() async {
    log("Syncing >> ${checkSaved()}");
    if (checkSaved()) {
      DBRepository.instance.deleteAllEntriesByLocation(location);
      // sync data and add category to list
      for (var record in recordsPerLocation) {
        DBRepository.instance.addEntry(record);
      }
      locationSaved.add(location);
    }
  }

  void addEntry(Entry entry) async {
    setState(() => isLoading = true);
    if (online) {
      try {
        final Entry newEntry = await APIRepository.instance.addEntry(entry);
        setState(() {
          recordsPerLocation.add(newEntry);
        });
        DBRepository.instance.addEntry(newEntry);
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Add failed"),
        ));
      }
    }
    setState(() => isLoading = false);
  }

  void deleteEntryBack(Entry entry) async {
    setState(() => isLoading = true);
    if (online) {
      try {
        APIRepository.instance.deleteEntry(entry.id!);
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server delete failed"),
        ));
      }
      setState(() {
        recordsPerLocation.remove(entry);
        Navigator.pop(context);
        DBRepository.instance.deleteEntry(entry.id!);
      });
    }
    setState(() => isLoading = false);
  }

  void deleteEntry(BuildContext context, Entry entry) {
    showDialog(
        context: context,
        builder: (_) => AlertDialog(
          title: Text("Delete Entry"),
          content: Text(
              "Are you sure you want to delete this entry with name: ${entry.name}?"),
          actions: [
            TextButton(
                onPressed: () {
                  deleteEntryBack(entry);
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
        title: Text("ALl entries for $location"),
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
          itemCount: recordsPerLocation.length,
          itemBuilder: (context, index) {
            return ListTile(
              title: Text(recordsPerLocation[index].name),
              subtitle: Text(getEntryDetails(recordsPerLocation[index])),
              onTap: () {},
              trailing: IconButton(
                icon: const Icon(Icons.delete),
                color: Colors.amber,
                onPressed: () {
                  if (online) {
                    deleteEntry(context, recordsPerLocation[index]);
                    //(context, tipsPerCategory[index]);
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
            Navigator.push(context,
                MaterialPageRoute(builder: (_) => AddPage()))
                .then((newEntry) {
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

  String getEntryDetails(Entry e) {
    return e.toString();
  }

  @override
  void dispose() {
    super.dispose();
    subscription.cancel();
  }
}