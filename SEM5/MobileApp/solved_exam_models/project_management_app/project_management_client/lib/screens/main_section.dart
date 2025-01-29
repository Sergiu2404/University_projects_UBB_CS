import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:project_management_client/model/entity.dart';

import '../custom_widgets/custom_circular_progress.dart';
import '../repo/APIRepo.dart';
import '../repo/DBRepo.dart';
import 'add_screen.dart';
import 'entity_by_id_page.dart';

bool flag = false;

class ProjectManagerSection extends StatefulWidget {
  final String _title;
  final bool isOnline;
  ProjectManagerSection(this._title, this.isOnline);

  @override
  State<StatefulWidget> createState() => _ProjectManagerSection();
}

class _ProjectManagerSection extends State<ProjectManagerSection> {
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
          content: Text("Get offline genres - need to sync. Hit the refresh button"),
        ));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline genres - synced"),
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
                      trailing: Icon(Icons.arrow_forward_ios, size: 16),
                      onTap: () {
                        Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (_) => GetEntityDetailsPage(
                                    allEntries[index].id!,
                                    online
                                )
                            )
                        );
                      },
                    ),
                  );
                }
            ),
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
        allEntries.remove(entry);
        Navigator.pop(context);
        DBRepo.instance.deleteEntry(entry.id!);
      });
    }
    setState(() => isLoading = false);
  }

  @override
  void dispose() {
    subscription.cancel();
    super.dispose();
  }
}