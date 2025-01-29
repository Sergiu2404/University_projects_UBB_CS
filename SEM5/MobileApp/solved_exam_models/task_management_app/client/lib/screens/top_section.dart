import 'dart:async';
import 'package:client/repo/APIRepo.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../model/entity.dart';

class CategoryTaskPair {
  String category;
  int taskCount;

  CategoryTaskPair(this.category, this.taskCount);
}

class TopSection extends StatefulWidget {
  final bool isOnline;

  const TopSection(this.isOnline, {Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _TopSectionState();
}

class _TopSectionState extends State<TopSection> {
  bool online = true;
  bool isLoading = false;
  late StreamSubscription<ConnectivityResult> subscription;

  List<CategoryTaskPair> topCategories = [];

  @override
  void initState() {
    super.initState();
    online = widget.isOnline;

    subscription = Connectivity()
        .onConnectivityChanged
        .listen((ConnectivityResult result) {
      setState(() {
        online = (result == ConnectivityResult.wifi ||
            result == ConnectivityResult.mobile);
      });
      if (online) {
        getData();
      }
    });

    Future.delayed(Duration.zero, getData);
  }

  void computeTopCategories(List<Entry> allEntries) {
    final categoryCounts = <String, int>{};

    for (var entry in allEntries) {
      categoryCounts[entry.category] = (categoryCounts[entry.category] ?? 0) + 1;
    }

    topCategories = categoryCounts.entries
        .map((e) => CategoryTaskPair(e.key, e.value))
        .toList()
      ..sort((b, a) => a.taskCount.compareTo(b.taskCount));

    // Take only top 3 categories
    topCategories = topCategories.take(3).toList();
  }

  Future<void> getData() async {
    setState(() => isLoading = true);

    if (online) {
      try {
        final allEntries = await APIRepo.instance.getAllEntries();
        computeTopCategories(allEntries);
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text("Error loading data: $e")),
        );
      }
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("Not available offline")),
      );
    }

    setState(() => isLoading = false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Top Categories"),
      ),
      body: isLoading
          ? const Center(
        child: CircularProgressIndicator(),
      )
          : Container(
        color: Colors.lightGreen[50],
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Padding(
              padding: EdgeInsets.all(16.0),
              child: Text(
                "Top 3 Categories",
                style: TextStyle(
                  fontSize: 22,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
            Expanded(
              child: ListView.builder(
                itemCount: topCategories.length,
                itemBuilder: (context, index) {
                  final category = topCategories[index];
                  return Card(
                    margin: const EdgeInsets.symmetric(
                        horizontal: 16.0, vertical: 8.0),
                    child: ListTile(
                      leading: CircleAvatar(
                        backgroundColor: Colors.green,
                        child: Text('${index + 1}'),
                      ),
                      title: Text(
                        category.category,
                        style: const TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 18,
                        ),
                      ),
                      subtitle: Text(
                        "Number of Tasks: ${category.taskCount}",
                        style: const TextStyle(fontSize: 16),
                      ),
                    ),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  @override
  void dispose() {
    subscription.cancel();
    super.dispose();
  }
}