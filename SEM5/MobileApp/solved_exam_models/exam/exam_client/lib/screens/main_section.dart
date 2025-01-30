import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../custom_widgets/custom_circular_progress.dart';
import '../model/entity.dart';
import '../repo/APIRepo.dart';
import '../repo/DBRepo.dart';
import 'add_screen.dart';
import 'entity_by_id_page.dart';

bool flag = false;

class MainSection extends StatefulWidget {
  final String _title;
  final bool isOnline;
  MainSection(this._title, this.isOnline);

  @override
  State<StatefulWidget> createState() => _MainSection();
}

class _MainSection extends State<MainSection> {
  bool online = true;
  bool isLoading = false;
  List<Entry> allEntries = [];
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
    if (flag == false) {
      DBRepo.instance.deleteAllEntries();
      for (var entry in allEntries) {
        DBRepo.instance.addEntry(entry);
      }
      if (allEntries.isNotEmpty) {
        flag = true;
      }
    }
    getData();
  }

  getData() async {
    setState(() => isLoading = true);
    if (online == true) {
      try {
        allEntries = await APIRepo.instance.getAllEntries();
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Entries: online"),
        ));
        if (flag == false) {
          syncData();
        }
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server get all entries failed"),
        ));
      }
    } else {
      if (flag == false) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline entries - need to sync. Hit the refresh button"),
        ));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline entries - synced"),
        ));
        allEntries = await DBRepo.instance.getAllEntries();
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
        actions: [
          // Moved add button to app bar
          IconButton(
            icon: Icon(Icons.add),
            onPressed: () {
              if (online) {
                Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => AddPage())
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
          ),
        ],
      ),
      body: isLoading
          ? Center(
          child: AnimatedCircularProgress(
              size: 70,
              strokeWidth: 5
          )
      )
          : Column(
        children: [
          if (!online)
            Container(
              padding: EdgeInsets.all(8),
              color: Colors.amber.shade100,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(Icons.offline_bolt, color: Colors.amber),
                  SizedBox(width: 8),
                  Text("Offline Mode - Showing cached data"),
                ],
              ),
            ),
          Expanded(
            child: ListView.builder(
              itemCount: allEntries.length,
              itemBuilder: (context, index) {
                return Card(
                  margin: EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                  child: ListTile(
                    title: Text(allEntries[index].toString()),
                    trailing: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        IconButton(
                          icon: const Icon(Icons.info, color: Colors.blue),
                          onPressed: () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (_) => GetEntityDetailsPage(
                                  allEntries[index].id!,
                                  online,
                                ),
                              ),
                            );
                          },
                          tooltip: "View Details",
                        ),
                        IconButton(
                          icon: const Icon(Icons.delete, color: Colors.amber),
                          onPressed: () {
                            if (online) {
                              deleteRecipe(context, allEntries[index]);
                            } else {
                              ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
                                content: Text("Cannot delete while offline"),
                              ));
                            }
                          },
                          tooltip: "Delete Entry",
                        ),
                      ],
                    ),
                  ),
                );
              },
            )
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _refreshAction,
        tooltip: 'Refresh page',
        child: const Icon(Icons.refresh),
      ),
    );
  }

  // void deleteRecipeBack(Entry recipe) async {
  //   setState(() => isLoading = true);
  //   if (online) {
  //     setState(() {
  //       APIRepo.instance.deleteEntry(recipe.id!);
  //       allEntries.remove(recipe);
  //       Navigator.pop(context);
  //       DBRepo.instance.deleteEntry(recipe.id!);
  //     });
  //   }
  //   setState(() => isLoading = false);
  // }

  void addEntry(Entry entry) async {
    setState(() => isLoading = true);
    if (online) {
      try {
        final Entry newEntry = await APIRepo.instance.addEntry(entry);
        setState(() {
          allEntries.add(newEntry);
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

  // In main section
  void deleteRecipe(BuildContext context, Entry entry) {
    showDialog(
        context: context,
        builder: (_) => AlertDialog(
          title: const Text("Delete Recipe"),
          content: Text(
              "Are you sure you want to delete this recipe from date ${entry.date}?"),
          actions: [
            TextButton(
                onPressed: () {
                  deleteRecipeBack(context, entry);
                  Navigator.pop(context);
                },
                child: const Text("Yes", style: TextStyle(color: Colors.red))),
            TextButton(
                onPressed: () {
                  Navigator.pop(context);
                },
                child: const Text("No", style: TextStyle(color: Colors.green)))
          ],
        ));
  }

  void deleteRecipeBack(BuildContext context, Entry recipe) {
    setState(() => isLoading = true);

    if (online) {
      APIRepo.instance.deleteEntry(recipe.id!).then((_) {
        setState(() {
          allEntries.remove(recipe);
        });
        return DBRepo.instance.deleteEntry(recipe.id!);
      }).catchError((e) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server delete failed"),
        ));
      }).whenComplete(() {
        setState(() => isLoading = false);
      });
    } else {
      setState(() => isLoading = false);
    }
  }



  @override
  void dispose() {
    subscription.cancel();
    super.dispose();
  }
}