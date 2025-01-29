import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:client/repo/APIRepo.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import 'package:intl/intl.dart';

import '../model/entity.dart';

// Class for storing month and total duration pairs
class MonthDurationPair {
  String month;
  double totalDuration;

  MonthDurationPair(this.month, this.totalDuration);
}

// Class for storing category and task count pairs
class CategoryTaskPair {
  String category;
  int taskCount;

  CategoryTaskPair(this.category, this.taskCount);
}

class ProgressSection extends StatefulWidget {
  final bool isOnline;

  const ProgressSection(this.isOnline, {Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _ProgressSection();
}

class _ProgressSection extends State<ProgressSection> {
  bool online = true;
  bool isLoading = false;
  late StreamSubscription<ConnectivityResult> subscription;

  List<MonthDurationPair> monthlyDurations = [];
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

  void computeMonthlyDurations(List<Entry> allEntries) {
    final monthDurations = <String, double>{};

    for (var entry in allEntries) {
      // Parse the date string to get month
      final date = DateTime.parse(entry.date);
      final month = DateFormat('MMMM yyyy').format(date);

      monthDurations[month] = ((monthDurations[month] ?? 0) + entry.duration);
    }

    monthlyDurations = monthDurations.entries
        .map((e) => MonthDurationPair(e.key, e.value))
        .toList()
      ..sort((b, a) => a.totalDuration.compareTo(b.totalDuration));
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
        computeMonthlyDurations(allEntries);
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
        title: const Text("Task Analysis"),
      ),
      body: isLoading
          ? const Center(
        child: CircularProgressIndicator(),
      )
          : Row(
        children: [
          Flexible(
            child: Container(
              color: Colors.blueGrey[50],
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Padding(
                    padding: EdgeInsets.all(8.0),
                    child: Text(
                      "Monthly Task Durations",
                      style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                  Expanded(
                    child: ListView.builder(
                      itemCount: monthlyDurations.length,
                      itemBuilder: (context, index) {
                        final monthDuration = monthlyDurations[index];
                        return ListTile(
                          title: Text(monthDuration.month),
                          subtitle: Text(
                            "Total Duration: ${monthDuration.totalDuration} minutes",
                          ),
                        );
                      },
                    ),
                  ),
                ],
              ),
            ),
          ),
          Flexible(
            child: Container(
              color: Colors.lightGreen[50],
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Padding(
                    padding: EdgeInsets.all(8.0),
                    child: Text(
                      "Top 3 Categories",
                      style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                  Expanded(
                    child: ListView.builder(
                      itemCount: topCategories.length,
                      itemBuilder: (context, index) {
                        final category = topCategories[index];
                        return ListTile(
                          title: Text(category.category),
                          subtitle: Text(
                            "Number of Tasks: ${category.taskCount}",
                          ),
                        );
                      },
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  @override
  void dispose() {
    subscription.cancel();
    super.dispose();
  }
}