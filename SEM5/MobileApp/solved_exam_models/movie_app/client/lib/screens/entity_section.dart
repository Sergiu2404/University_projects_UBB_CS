import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:client/repo/APIRepo.dart';
import 'package:client/repo/DBRepo.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../api/api.dart';
import '../db/db.dart';
import '../model/entity.dart';
import '../util/message_response.dart';
import 'add_screen.dart';

List<String> genresSaved = [];

class MoviesPage extends StatefulWidget {
  final String genre;
  final bool isOnline;

  MoviesPage(this.genre, this.isOnline);

  @override
  State<StatefulWidget> createState() => _MoviesPage();
}

class _MoviesPage extends State<MoviesPage> {
  bool online = true;

  late String genre;

  bool isLoading = false;
  List<Entry> recordsPerGenre = [];
  bool saved = false;

  late StreamSubscription<ConnectivityResult> subscription;

  @override
  void initState() {
    super.initState();
    online = widget.isOnline;
    genre = widget.genre;
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
    final genres = recordsPerGenre.where((element) => element == widget.genre);
    return genres.isEmpty;
  }

  getData() async {
    setState(() => isLoading = true);
    if (online == true) {
      try {
        recordsPerGenre = await APIRepo.instance.getEntriesByGenre(genre);
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
        recordsPerGenre = await DBRepo.instance.getByGenre(genre);
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
      DBRepo.instance.deleteAllEntriesByGenre(genre);
      // sync data and add category to list
      for (var record in recordsPerGenre) {
        DBRepo.instance.addEntry(record);
      }
      genresSaved.add(genre);
    }
  }

  void addEntry(Entry entry) async {
    setState(() => isLoading = true);
    if (online) {
      try {
        final Entry newEntry = await APIRepo.instance.addEntry(entry);
        setState(() {
          recordsPerGenre.add(newEntry);
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
        recordsPerGenre.remove(entry);
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
              "Are you sure you want to delete entry: ${entry.name}?"),
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
        title: Text("Movies from $genre"),
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
          itemCount: recordsPerGenre.length,
          itemBuilder: (context, index) {
            return ListTile(
              title: Text(recordsPerGenre[index].name),
              subtitle: Text(getEntryDetails(recordsPerGenre[index])),
              onTap: () {},
              trailing: IconButton(
                icon: const Icon(Icons.delete),
                color: Colors.amber,
                onPressed: () {
                  if (online) {
                    deleteEntry(context, recordsPerGenre[index]);
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
                MaterialPageRoute(builder: (_) => AddPage(genre)))
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