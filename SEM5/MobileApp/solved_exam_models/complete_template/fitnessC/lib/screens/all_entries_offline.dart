import 'dart:async';
import 'dart:developer';

import 'package:fitnessclient/screens/update_screen.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../repo/api_repo.dart';
import '../repo/db_repo.dart';
import '../custom_widgets/custom_circular_progress.dart';
import '../model/entry.dart';
import '../custom_widgets/custom_request_message.dart';
import 'add_screen.dart';

class AllEntriesPage extends StatefulWidget {
  const AllEntriesPage({super.key});

  @override
  State<AllEntriesPage> createState() => _AllEntriesPageState();
}

class _AllEntriesPageState extends State<AllEntriesPage> {
  bool online = true;
  bool isLoading = false;
  List<Entry> allEntries = [];
  late StreamSubscription<ConnectivityResult> subscription;

  @override
  void initState() {
    super.initState();
    subscription = Connectivity()
        .onConnectivityChanged
        .listen((ConnectivityResult result) {
      setState(() {
        online = (result == ConnectivityResult.wifi ||
            result == ConnectivityResult.mobile);
      });
      getData();
    });

    Future.delayed(Duration.zero, getData);  // Fetch data when screen loads
  }

  // Get data (either from DB or API)
  getData() async {
    setState(() => isLoading = true);

    try {
      // First, try to load data from local DB
      allEntries = await DB_Repo.instance.getAllEntries();

      if (online && allEntries.isEmpty) {
        // If online and no local entries, fetch from API
        allEntries = await API_Repo.instance.getAllEntries();

        // Clear and replace local entries
        DB_Repo.instance.deleteAllEntries();
        for (var entry in allEntries) {
          await DB_Repo.instance.addEntry(entry);
        }

        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("Entries synced online")),
        );
      } else if (allEntries.isNotEmpty) {
        // Show message for local DB data
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

  // Add a new entry (either to API or local DB)
  void addEntry(Entry entry) async {
    setState(() => isLoading = true);
    if (online) {
      try {
        final newEntry = await API_Repo.instance.addEntry(entry);
        await DB_Repo.instance.addEntry(newEntry);
        setState(() {
          allEntries.add(newEntry);
        });
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("Add entry failed")),
        );
      }
    } else {
      // Save entry to local DB if offline
      try {
        final localEntry = await DB_Repo.instance.addEntry(entry);
        setState(() {
          allEntries.add(localEntry);
        });
      } catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("Unable to save entry locally")),
        );
      }
    }
    setState(() => isLoading = false);
  }

  // Delete an entry (either from API or local DB)
  void deleteEntryBack(Entry entry) async {
    setState(() => isLoading = true);
    if (online) {
      try {
        // Call delete without awaiting (async operation)
        API_Repo.instance.deleteEntry(entry.id!);
        await DB_Repo.instance.deleteEntry(entry.id!);
        setState(() {
          allEntries.remove(entry);
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

  // Show confirmation dialog for deleting entry
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
        title: const Text("All Entries"),
      ),
      body: isLoading
          ? Center(
          child: CustomCircularProgress(
              size: 40,
              color: Colors.blue,
              strokeWidth: 5))
          : allEntries.isEmpty
          ? const Center(child: Text("No entries"))
          : ListView.builder(
        itemCount: allEntries.length,
        itemBuilder: (context, index) {
          return ListTile(
            title: Text(allEntries[index].type),
            subtitle: Text(getEntryDetails(allEntries[index])),
            trailing: IconButton(
              icon: const Icon(Icons.update),
              color: Colors.amber,
                onPressed: () async {
                  if (!online) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text("You are offline. Please try again when online.")),
                    );
                  } else {
                    // Pass the entry to the UpdatePage and await the result
                    Entry? updatedEntry = await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => UpdatePage(allEntries[index]),
                      ),
                    );

                    if (updatedEntry != null) {
                      // If updated entry is returned, update the list
                      setState(() {
                        // Update the entry in the list
                        allEntries[index] = updatedEntry;

                        // Optionally, update the entry in the DB as well
                        DB_Repo.instance.updateEntry(updatedEntry); // Assuming you have an `updateEntry` method in DB_Repo
                        if (online) {
                          API_Repo.instance.updateEntry(updatedEntry); // Update on the server
                        }
                      });
                    }
                  }
                }
              //deleteEntry(context, allEntries[index]),
            ),
          );
        },
      ),
      // floatingActionButton: FloatingActionButton(
      //   onPressed: () {
      //     if (!online) {
      //       ScaffoldMessenger.of(context).showSnackBar(
      //         const SnackBar(content: Text("Entries will be saved locally")),
      //       );
      //     }
      //
      //     Navigator.push(
      //       context,
      //       MaterialPageRoute(builder: (_) => AddPage('', online)),
      //     ).then((newEntry) {
      //       if (newEntry != null) {
      //         addEntry(newEntry);
      //       }
      //     });
      //   },
      //   tooltip: "Add Entry",
      //   child: const Icon(Icons.add),
      // ),
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
