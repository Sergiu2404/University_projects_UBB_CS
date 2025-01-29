import 'dart:async';

import 'package:client/model/entity.dart';
import 'package:client/repo/APIRepository.dart';
import 'package:client/repo/DBRepository.dart';
import 'package:client/screens/add_screen.dart';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:flutter/material.dart';

bool flag = false;

class GetAllSection extends StatefulWidget {
  final String title;
  final bool isOnline;
  GetAllSection(this.title, this.isOnline);

  @override
  State<GetAllSection> createState() => _GetAllSectionState();
}

class _GetAllSectionState extends State<GetAllSection> {
  bool online = true;
  bool isLoading = false;

  List<Entry> entries = [];

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
      DBRepository.instance.deleteAllEntries();
      for (var elem in entries) {
        DBRepository.instance.addEntry(elem);
      }
      if (entries.isNotEmpty) {
        flag = true;
      }
    }
    getData();
  }

  getData() async {
    setState(() => isLoading = true);
    if (online == true) {
      try{
        entries = await APIRepository.instance.getAllEntries();
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Entries: online"),
        ));
        if (flag == false) {
          syncData();
        }
      }
      on Exception catch(error){
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(
          content: Text("Server get entries failed with error $error"),
        ));
      }
    } else {
      if (flag == false) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline entries - need to sync. Hit the refresh button"),
        ));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Get offline entires - synced"),
        ));
        entries = await DBRepository.instance.getAllEntries();
      }
    }
    setState(() => isLoading = false);
  }

  void _refreshAction() {
    setState(() {
      getData();
    });
  }

  void addEntry(Entry entry) async {
    setState(() => isLoading = true);
    if (online) {
      try {
        final Entry newEntry = await APIRepository.instance.addEntry(entry);
        setState(() {
          entries.add(newEntry);
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


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("All records"),
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : Column(
        children: [
          TextButton(
            onPressed: () {
              if (online) {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => AddPage(),
                  ),
                ).then((newEntry) {
                  if (newEntry != null) {
                    setState(() {
                      addEntry(newEntry);
                    });
                  }
                });
              } else {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(
                    content: Text("Cannot add while offline"),
                  ),
                );
              }
            },
            child: const Text(
              'Add new entry',
              style: TextStyle(fontSize: 20, color: Colors.black),
            ),
          ),
          Expanded(
            child: ListView.builder(
              itemCount: entries.length,
              itemBuilder: (context, index) {
                return ListTile(
                  title: Text(entries[index].name),
                );
              },
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

}
