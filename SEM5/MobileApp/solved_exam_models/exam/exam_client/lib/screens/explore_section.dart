import 'dart:async';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../custom_widgets/custom_circular_progress.dart';
import '../model/entity.dart';
import '../repo/APIRepo.dart';

class ExploreSection extends StatefulWidget {
  final String _title;
  final bool isOnline;

  ExploreSection(this._title, this.isOnline);

  @override
  State<StatefulWidget> createState() => _ExploreSectionState();
}

class _ExploreSectionState extends State<ExploreSection> {
  bool online = true;
  bool isLoading = false;
  Map<String, double> monthlyAverageRatings = {};
  late StreamSubscription<ConnectivityResult> subscription;

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

  void computeMonthlyAverageRatings(List<Entry> allEntries) {
    final monthlyRatings = <String, List<double>>{};

    for (var entry in allEntries) {
      final month = entry.date.toString().substring(0, 7);
      monthlyRatings[month] = (monthlyRatings[month] ?? [])..add(entry.rating);
    }

    // avg each month
    monthlyAverageRatings = monthlyRatings.map((month, ratings) {
      final average = ratings.reduce((a, b) => a + b) / ratings.length;
      return MapEntry(month, average);
    });

    // sort monthly in desc order
    final sortedEntries = monthlyAverageRatings.entries.toList()
      ..sort((b, a) => a.value.compareTo(b.value));
    monthlyAverageRatings = Map.fromEntries(sortedEntries);
  }

  Future<void> getData() async {
    setState(() => isLoading = true);

    if (online) {
      try {
        final allEntries = await APIRepo.instance.getAllEntries();
        computeMonthlyAverageRatings(allEntries);
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Data retrieved successfully."),
        ));
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text("Error retrieving data: $e")),
        );
      }
    } else {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text("Offline mode - unable to retrieve data."),
      ));
    }

    setState(() => isLoading = false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget._title),
      ),
      body: isLoading
          ? Center(
        child: AnimatedCircularProgress(size: 70, strokeWidth: 5),
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
                  Text("Offline Mode"),
                ],
              ),
            ),
          Expanded(
            child: ListView.builder(
              itemCount: monthlyAverageRatings.length,
              itemBuilder: (context, index) {
                final entry = monthlyAverageRatings.entries.toList()[index];
                return Card(
                  margin:
                  const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                  child: ListTile(
                    leading: CircleAvatar(
                      backgroundColor: Colors.blueAccent,
                      child: Text('${index + 1}'),
                    ),
                    title: Text(
                      entry.key, // Month (YYYY-MM)
                      style: const TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 18,
                      ),
                    ),
                    subtitle: Text(
                      "Average Rating: ${entry.value.toStringAsFixed(2)}",
                      style: const TextStyle(fontSize: 16),
                    ),
                  ),
                );
              },
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: getData,
        tooltip: 'Refresh',
        child: const Icon(Icons.refresh),
      ),
    );
  }

  @override
  void dispose() {
    subscription.cancel();
    super.dispose();
  }
}
