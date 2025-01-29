import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:project_management_client/repo/APIRepo.dart';

import '../custom_widgets/custom_circular_progress.dart';
import '../model/entity.dart';
import '../repo/DBRepo.dart';
import 'add_screen.dart';

class GetEntityDetailsPage extends StatefulWidget {
  final int projectId;
  final bool isOnline;

  GetEntityDetailsPage(this.projectId, this.isOnline);

  @override
  State<StatefulWidget> createState() => _GetEntityDetailsPage();
}

class _GetEntityDetailsPage extends State<GetEntityDetailsPage> {
  bool online = true;
  bool isLoading = false;
  late StreamSubscription<ConnectivityResult> subscription;

  late Entry entry = Entry(
    id: 0, // Default ID, as it will be replaced once data is fetched.
    name: '',
    team: '',
    details: '',
    status: '',
    members: 0,
    type: '',
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
        final entryDetails = await APIRepo.instance.getEntryById(widget.projectId);

        // Save to local DB for offline access
        await DBRepo.instance.addEntry(entryDetails);

        setState(() {
          entry = entryDetails;
        });

        ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text("Project details updated from server"))
        );
      } else {
        // Load from local DB when offline
        final localEntry = await DBRepo.instance.getEntryById(widget.projectId);

        if (localEntry != null) {
          setState(() {
            entry = localEntry;
          });

          ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(content: Text("Showing cached project details"))
          );
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(content: Text("No cached data available for this project"))
          );
        }
      }
    } catch (e) {
      log("Error fetching project details: $e");
      ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("Error loading project details"))
      );
    } finally {
      setState(() => isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Project Details"),
        actions: [
          if (online)
            IconButton(
              icon: Icon(Icons.refresh),
              onPressed: fetchProjectDetails,
              tooltip: "Refresh project details",
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
            _buildProjectDetails(),
          ],
        ),
      ),
    );
  }

  Widget _buildProjectDetails() {
    if (!this.mounted) return Container();

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          entry.name,
          style: Theme.of(context).textTheme.headlineSmall,
        ),
        SizedBox(height: 16),
        _buildInfoRow("Status", entry.status),
        _buildInfoRow("Members", entry.members.toString()),
        _buildInfoRow("Status", entry.status),
        _buildInfoRow("Type", entry.type),
        SizedBox(height: 24),
        Text(
          "Description",
          style: Theme.of(context).textTheme.titleLarge,
        ),
        SizedBox(height: 8),
        Text(entry.details),
        // Add more project details as needed
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