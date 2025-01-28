import 'dart:convert';
import 'package:fitnessclient/repo/db_repo.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import '../custom_widgets/custom_circular_progress.dart';
import '../model/entry.dart';

class ReservationPage extends StatefulWidget {
  final bool online;
  const ReservationPage(this.online, {Key? key}) : super(key: key);

  @override
  _ReservationPageState createState() => _ReservationPageState();
}

class _ReservationPageState extends State<ReservationPage> {
  List<Entry> _entries = [];
  List<Entry> _reservedEntries = [];
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _loadEntries();
    _loadReservedEntries();
  }

  Future<void> _loadEntries() async {
    try {
      if (widget.online) {
        final response = await http.get(Uri.parse('http://10.0.2.2:7496/entries'));

        print('Response status: ${response.statusCode}');
        print('Response body: ${response.body}');

        if (response.statusCode == 200) {
          final List<dynamic> entriesData = json.decode(response.body);
          print('Entries data: $entriesData');

          _entries = entriesData
              .map((e) => Entry.fromJsonApi(e))
              .where((entry) => entry.rate < 500)
              .toList();

          print('Filtered entries: $_entries');

          for (var entry in _entries) {
            await DB_Repo.instance.addEntry(entry);
          }
        }
      } else {
        _entries = await DB_Repo.instance.getAllEntries();
        _entries = _entries.where((entry) => entry.rate < 500).toList();
      }
    } catch (e) {
      print('Error loading entries: $e');
    }

    setState(() {
      _isLoading = false;
    });
  }

  Future<void> _loadReservedEntries() async {
    try {
      if (widget.online) {
        final response = await http.get(Uri.parse('http://10.0.2.2:7496/reserved'));
        if (response.statusCode == 200) {
          final List<dynamic> reservedData = json.decode(response.body);
          _reservedEntries = reservedData.map((e) => Entry.fromJsonApi(e)).toList();
        }
      } else {
        _reservedEntries = [];
      }
    } catch (e) {
      print('Error loading reserved entries: $e');
    }
  }

  Future<void> _reserveEntry(int? entryId) async {
    print('Attempting to reserve entry: $entryId'); // Add logging

    if (entryId == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Invalid entry')),
      );
      return;
    }

    if (!widget.online) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Reservation only available online')),
      );
      return;
    }

    try {
      final response = await http.put(
        Uri.parse('http://10.0.2.2:7496/reserve'),
        headers: {'Content-Type': 'application/json'}, // Explicitly set content type
        body: json.encode({'entryId': entryId}),
      );

      print('Reserve response status: ${response.statusCode}');
      print('Reserve response body: ${response.body}');

      if (response.statusCode == 200) {
        final reservedEntry=Entry.fromJsonApi(json.decode(response.body));
        setState(() {
          _reservedEntries.add(reservedEntry);
        });
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Entry reserved successfully')),
        );
        //await _loadReservedEntries(); // Ensure this method is called
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Failed to reserve entry: ${response.statusCode}')),
        );
      }
    } catch (e) {
      print('Error reserving entry: $e');
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error reserving entry: $e')),
      );
    }
  }

  Future<void> _useEntry(int? entryId) async {
    if (entryId == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Invalid entry')),
      );
      return;
    }

    if (!widget.online) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Using entry only available online')),
      );
      return;
    }

    try {
      final response = await http.put(
        Uri.parse('http://10.0.2.2:2305/use'),
        body: json.encode({'entryId': entryId}),
      );

      if (response.statusCode == 200) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Entry used successfully')),
        );
        _loadReservedEntries();
      }
    } catch (e) {
      print('Error using entry: $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Entries & Reservations')),
      body: _isLoading
          ? Center(child: CustomCircularProgress(
          size: 40,
          color: Colors.blue,
          strokeWidth: 5))
          : Column(
        children: [
          const Text('Available Entries', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
          Expanded(
            child: ListView.builder(
              itemCount: _entries.length,
              itemBuilder: (context, index) {
                final entry = _entries[index];
                return ListTile(
                  title: Text('Entry ID: ${entry.id ?? 'N/A'}'),
                  subtitle: Text('Rate: ${entry.rate}'),
                  trailing: widget.online
                      ? ElevatedButton(
                    onPressed: () => _reserveEntry(entry.id),
                    child: const Text('Reserve'),
                  )
                      : null,
                );
              },
            ),
          ),
          const Text('Reserved Entries', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
          Expanded(
            child: ListView.builder(
              itemCount: _reservedEntries.length,
              itemBuilder: (context, index) {
                final entry = _reservedEntries[index];
                return ListTile(
                  title: Text('Entry ID: ${entry.id ?? 'N/A'}'),
                  trailing: widget.online
                      ? ElevatedButton(
                    onPressed: () => _useEntry(entry.id),
                    child: const Text('Use'),
                  )
                      : null,
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}