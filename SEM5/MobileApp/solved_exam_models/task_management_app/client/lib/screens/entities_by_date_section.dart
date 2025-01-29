import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:client/repo/APIRepo.dart';
import 'package:client/repo/DBRepo.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../custom_widgets/custom_circular_progress.dart';
import '../model/entity.dart';
import 'add_screen.dart';

List<String> genresSaved = [];

class EntitiesByDatePage extends StatefulWidget {
  final String date;
  final bool isOnline;

  EntitiesByDatePage(this.date, this.isOnline);

  @override
  State<StatefulWidget> createState() => _EntitiesByDatePage();
}

class _EntitiesByDatePage extends State<EntitiesByDatePage> {
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
    final dates = recordsPerDate.where((element) => element == widget.date);
    return dates.isEmpty;
  }

  getData() async {
    setState(() => isLoading = true);
    if (online == true) {
      try {
        recordsPerDate = await APIRepo.instance.getEntriesByDate(date);
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Back at it!"),
        ));
        syncData();
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server get entries for genre failed"),
        ));
      }
    } else {
      if (checkSaved() == false) {
        recordsPerDate = await DBRepo.instance.getByDate(date);
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
      DBRepo.instance.deleteAllEntriesByDate(date);
      // sync data and add category to list
      for (var record in recordsPerDate) {
        DBRepo.instance.addEntry(record);
      }
      genresSaved.add(date);
    }
  }

  void addEntry(Entry entry) async {
    setState(() => isLoading = true);
    if (online) {
      try {
        final Entry newEntry = await APIRepo.instance.addEntry(entry);
        setState(() {
          recordsPerDate.add(newEntry);
        });
        DBRepo.instance.addEntry(newEntry);
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
        APIRepo.instance.deleteEntry(entry.id!);
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server delete failed"),
        ));
      }
      setState(() {
        recordsPerDate.remove(entry);
        Navigator.pop(context);
        DBRepo.instance.deleteEntry(entry.id!);
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
              "Are you sure you want to delete entry: ${entry.type}?"),
          actions: [
            TextButton(
                onPressed: () {
                  deleteEntryBack(entry);
                },
                child: Text("Sure", style: TextStyle(color: Colors.red))),
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
        title: Text("Tasks from $date"),
      ),
      body: isLoading
          ? Center(child: AnimatedCircularProgress(
          size: 70,
          strokeWidth: 5))
          : ListView.builder(
          itemCount: recordsPerDate.length,
          itemBuilder: (context, index) {
            return ListTile(
              title: Text(recordsPerDate[index].description),
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
                MaterialPageRoute(builder: (_) => AddPage(date)))
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