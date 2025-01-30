import 'dart:async';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../model/entity.dart';
import '../repo/APIRepo.dart';

class CategoryRatingPair {
  String category;
  double totalRating;

  CategoryRatingPair(this.category, this.totalRating);
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

  List<CategoryRatingPair> topCategoriesRating = [];

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

  void computeTopEntries(List<Entry> allEntries) {
    final categoriesRating = <String, double>{};

    for (var entry in allEntries) {
      categoriesRating[entry.category] =
          (categoriesRating[entry.category] ?? 0) + entry.rating;
    }

    topCategoriesRating = categoriesRating.entries
        .map((entry) => CategoryRatingPair(entry.key, entry.value))
        .toList()
      ..sort((b, a) => a.totalRating.compareTo(b.totalRating));

    topCategoriesRating = topCategoriesRating.take(3).toList();
  }

  Future<void> getData() async {
    setState(() => isLoading = true);

    if (online) {
      try {
        final allEntries = await APIRepo.instance.getAllEntries();
        computeTopEntries(allEntries);
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
        title: const Text("Insights Section"),
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
                itemCount: topCategoriesRating.length,
                itemBuilder: (context, index) {
                  final entry = topCategoriesRating[index];
                  return Card(
                    margin: const EdgeInsets.symmetric(
                        horizontal: 16.0, vertical: 8.0),
                    child: ListTile(
                      leading: CircleAvatar(
                        backgroundColor: Colors.green,
                        child: Text('${index + 1}'),
                      ),
                      title: Text(
                        entry.category,
                        style: const TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 18,
                        ),
                      ),
                      subtitle: Text(
                        "Total Rating: ${entry.totalRating.toStringAsFixed(1)}",
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
