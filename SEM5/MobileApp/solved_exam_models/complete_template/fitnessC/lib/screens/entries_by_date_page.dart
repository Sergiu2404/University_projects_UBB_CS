// ignore_for_file: use_build_context_synchronously

import 'dart:async';
import 'dart:developer';

import 'package:fitnessclient/repo/api_repo.dart';
import 'package:fitnessclient/repo/db_repo.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../custom_widgets/custom_circular_progress.dart';
import '../model/entry.dart';
import '../custom_widgets/custom_request_message.dart';
import 'add_screen.dart';

class EntriesByDatePage extends StatefulWidget {
  final String date;
  final bool isOnline;

  EntriesByDatePage(this.date, this.isOnline);

  @override
  State<StatefulWidget> createState() => _EntriesByDatePage();
}

class _EntriesByDatePage extends State<EntriesByDatePage> {
  bool online = true;
  late String date;
  bool isLoading = false;
  List<Entry> entriesPerdate = [];

  late StreamSubscription<ConnectivityResult> subscription;

  @override
  void initState() {
    super.initState();
    online = widget.isOnline;
    date = widget.date;
    subscription = Connectivity()
        .onConnectivityChanged
        .listen((ConnectivityResult result) {
      setState(() {
        online = (result == ConnectivityResult.wifi ||
            result == ConnectivityResult.mobile);
      });
      getData();
    });

    Future.delayed(Duration.zero, getData);
  }

  getData() async {
    setState(() => isLoading = true);
    try {
      // Always try to fetch local entries first
      entriesPerdate = await DB_Repo.instance.getBydate(date);

      if (online && entriesPerdate.isEmpty) {
        // If online and no local entries, fetch from API
        entriesPerdate = await API_Repo.instance.getEntriesByDate(date);

        // Clear and replace local entries for this date
        DB_Repo.instance.deleteAllEntriesBydate(date);
        for (var entry in entriesPerdate) {
          await DB_Repo.instance.addEntry(entry);
        }

        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("Entries synced online")),
        );
      } else if (entriesPerdate.isNotEmpty) {
        // Show a message if entries are from local DB
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("Displaying saved entries")),
        );
      } else {
        // No entries found
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("No entries found")),
        );
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text("Error loading entries: $e")),
      );
    }
    setState(() => isLoading = false);
  }

  void addEntry(Entry entry) async {
    setState(() => isLoading = true);
    if (online) {
      try {
        final newEntry = await API_Repo.instance.addEntry(entry);
        await DB_Repo.instance.addEntry(newEntry);
        setState(() {
          entriesPerdate.add(newEntry);
        });
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("Add entry failed")),
        );
      }
    } else {
      // Add to local DB when offline
      try {
        final localEntry = await DB_Repo.instance.addEntry(entry);
        setState(() {
          entriesPerdate.add(localEntry);
        });
      } catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("Unable to save entry locally")),
        );
      }
    }
    setState(() => isLoading = false);
  }

  void deleteEntryBack(Entry entry) async {
    setState(() => isLoading = true);
    if (online) {
      try {
        // Call delete without awaiting
        API_Repo.instance.deleteEntry(entry.id!);
        await DB_Repo.instance.deleteEntry(entry.id!);
        setState(() {
          entriesPerdate.remove(entry);
        });
        Navigator.pop(context);
      } catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("Delete failed")),
        );
      }
    }
    setState(() => isLoading = false);
  }

  void deleteEntry(BuildContext context, Entry entry) {
    if (!online) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("Cannot delete while offline")),
      );
      return;
    }

    showDialog(
      context: context,
      builder: (_) => AlertDialog(
        title: Text("Delete Entry"),
        content: Text("Are you sure you want to delete entry: ${entry.type}?"),
        actions: [
          TextButton(
            onPressed: () => deleteEntryBack(entry),
            child: Text("Yes", style: TextStyle(color: Colors.red)),
          ),
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: Text("No", style: TextStyle(color: Colors.green)),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Entries for $date"),
      ),
      body: isLoading
          ? Center(child: CustomCircularProgress(
          size: 40,
          color: Colors.blue,
          strokeWidth: 5))
          : entriesPerdate.isEmpty
          ? const Center(child: Text("No entries"))
          : ListView.builder(
        itemCount: entriesPerdate.length,
        itemBuilder: (context, index) {
          return ListTile(
            title: Text(entriesPerdate[index].type),
            subtitle: Text(getEntryDetails(entriesPerdate[index])),
            trailing: IconButton(
              icon: const Icon(Icons.delete),
              color: Colors.amber,
              onPressed: () => deleteEntry(context, entriesPerdate[index]),
            ),
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          if (!online) {
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(content: Text("Entries will be saved locally")),
            );
          }

          Navigator.push(
            context,
            MaterialPageRoute(builder: (_) => AddPage(widget.date, online)),
          ).then((newEntry) {
            if (newEntry != null) {
              addEntry(newEntry);
            }
          });
        },
        tooltip: "Add Entry",
        child: const Icon(Icons.add),
      ),
    );
  }

  String getEntryDetails(Entry e) {
    return "Duration: ${e.duration}\nDistance: ${e.distance}\nCalories: ${e.calories}\nRate: ${e.rate}";
  }

  @override
  void dispose() {
    subscription.cancel();
    super.dispose();
  }
}