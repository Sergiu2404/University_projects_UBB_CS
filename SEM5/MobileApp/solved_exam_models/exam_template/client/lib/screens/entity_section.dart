// ignore_for_file: use_build_context_synchronously

import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../api/api.dart';
import '../db/db.dart';
import '../model/entity.dart';
import '../util/message_response.dart';
import 'add_screen.dart';

List<String> dateSaved = [];

class SymptomPage extends StatefulWidget {
  final String date;
  final bool isOnline;

  SymptomPage(this.date, this.isOnline);

  @override
  State<StatefulWidget> createState() => _SymptomPage();
}

class _SymptomPage extends State<SymptomPage> {
  bool online = true;

  late String date;

  bool isLoading = false;
  List<Entry> recordsPerDate = [];
  bool saved = false;

  late StreamSubscription<ConnectivityResult> subscription;

  @override
  void initState() {
    super.initState();
    online = widget.isOnline;
    date = widget.date;
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
    final genres = recordsPerDate.where((element) => element == widget.date);
    return genres.isEmpty;
  }

  getData() async {
    setState(() => isLoading = true);
    if (online == true) {
      try {
        recordsPerDate = await ApiService.instance.getEntriesByDate(date);
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Back at it!"),
        ));
        syncData();
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server get entries for date failed"),
        ));
      }
    } else {
      if (checkSaved() == false) {
        recordsPerDate = await EntityDB.instance.getBydate(date);
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Offline health entries are currently being displayed"),
        ));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Offline health entries have not been saved yet to db"),
        ));
      }
    }
    setState(() => isLoading = false);
  }

  syncData() async {
    log("Syncing >> ${checkSaved()}");
    if (checkSaved()) {
      EntityDB.instance.deleteAllEntriesBydate(date);
      // sync data and add category to list
      for (var transaction in recordsPerDate) {
        EntityDB.instance.addEntry(transaction);
      }
      dateSaved.add(date);
    }
  }

  void addEntry(Entry entry) async {
    setState(() => isLoading = true);
    if (online) {
      try {
        final Entry newEntry = await ApiService.instance.addEntry(entry);
        setState(() {
          recordsPerDate.add(newEntry);
        });
        EntityDB.instance.addEntry(newEntry);
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
        ApiService.instance.deleteEntry(entry.id!);
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server delete failed"),
        ));
      }
      setState(() {
        recordsPerDate.remove(entry);
        Navigator.pop(context);
        EntityDB.instance.deleteEntry(entry.id!);
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
              "Are you sure you want to delete entry: ${entry.symptom}?"),
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
        title: Text("Transactions for $date"),
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
          itemCount: recordsPerDate.length,
          itemBuilder: (context, index) {
            return ListTile(
              title: Text(recordsPerDate[index].symptom),
              subtitle: Text(getEntryDetails(recordsPerDate[index])),
              onTap: () {},
              trailing: IconButton(
                icon: const Icon(Icons.delete),
                color: Colors.amber,
                onPressed: () {
                  if (online) {
                    deleteEntry(context, recordsPerDate[index]);
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
                MaterialPageRoute(builder: (_) => AddPage(widget.date)))
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
    return "Medication: ${e.medication}\nDosage: ${e.dosage}\nDoctor: ${e.doctor}\nNotes: ${e.notes}";
  }

  @override
  void dispose() {
    super.dispose();
    subscription.cancel();
  }
}