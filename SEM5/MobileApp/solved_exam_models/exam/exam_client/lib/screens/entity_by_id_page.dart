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

class GetEntityDetailsPage extends StatefulWidget {
  final int recipeId;
  final bool isOnline;

  GetEntityDetailsPage(this.recipeId, this.isOnline);

  @override
  State<StatefulWidget> createState() => _GetEntityDetailsPage();
}

class _GetEntityDetailsPage extends State<GetEntityDetailsPage> {
  bool online = true;
  bool isLoading = false;
  late StreamSubscription<ConnectivityResult> subscription;

  late Entry entry = Entry(
    id: 0, // Default ID, as it will be replaced once data is fetched.
    date: '',
    title: '',
    ingredients: '',
    category: '',
    rating: 0,
  );


  @override
  void initState() {
    super.initState();
    online = widget.isOnline;

    // Listen for connectivity changes
    subscription = Connectivity()
        .onConnectivityChanged
        .listen((ConnectivityResult result) {
      setState(() {
        online = result == ConnectivityResult.wifi ||
            result == ConnectivityResult.mobile;
      });
      // Refresh data when coming back online
      if (online) {
        fetchProjectDetails();
      }
    });

    // Initial data fetch
    Future.delayed(Duration.zero, () {
      fetchProjectDetails();
    });
  }

  Future<void> fetchProjectDetails() async {
    setState(() => isLoading = true);

    try {
      if (online) {
        // Try to fetch from API when online
        final entryDetails = await APIRepo.instance.getEntryById(widget.recipeId);

        // Save to local DB for offline access
        await DBRepo.instance.addEntry(entryDetails);

        setState(() {
          entry = entryDetails;
        });

        ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text("ingredients updated from server"))
        );
      } else {
        // Load from local DB when offline
        final localEntry = await DBRepo.instance.getEntryById(widget.recipeId);

        if (localEntry != null) {
          setState(() {
            entry = localEntry;
          });

          ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(content: Text("Showing cached ingredients"))
          );
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(content: Text("No cached data available for this"))
          );
        }
      }
    } catch (e) {
      log("Error fetching ingredients: $e");
      ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("Error loading ingredients"))
      );
    } finally {
      setState(() => isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Recpie ingredients"),
        actions: [
          if (online)
            IconButton(
              icon: Icon(Icons.refresh),
              onPressed: fetchProjectDetails,
              tooltip: "Refresh ingredients",
            )
        ],
      ),
      body: isLoading
          ? Center(child: AnimatedCircularProgress(size: 70, strokeWidth: 5))
          : SingleChildScrollView(
        padding: EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            if (!online)
              Container(
                padding: EdgeInsets.all(8),
                color: Colors.amber.shade100,
                child: Row(
                  children: [
                    Icon(Icons.offline_bolt, color: Colors.amber),
                    SizedBox(width: 8),
                    Text("Offline Mode - Showing cached data"),
                  ],
                ),
              ),
            SizedBox(height: 16),
            _buildDetails(),
          ],
        ),
      ),
    );
  }

  Widget _buildDetails() {
    if (!this.mounted) return Container();

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          entry.date,
          style: Theme.of(context).textTheme.headlineSmall,
        ),
        SizedBox(height: 16),
        _buildInfoRow("category", entry.category),
        _buildInfoRow("rating", entry.rating.toString()),
        _buildInfoRow("category", entry.category),
        SizedBox(height: 24),
        Text(
          "Description",
          style: Theme.of(context).textTheme.titleLarge,
        ),
        SizedBox(height: 8),
        Text(entry.ingredients),
        // Add more project ingredients as needed
      ],
    );
  }

  Widget _buildInfoRow(String label, String value) {
    return Padding(
      padding: EdgeInsets.symmetric(vertical: 8),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: 100,
            child: Text(
              label,
              style: TextStyle(fontWeight: FontWeight.bold),
            ),
          ),
          Expanded(child: Text(value)),
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