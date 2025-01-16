import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:client/model/statistics.dart';

import '../api/api.dart';
import '../model/entity.dart';
import '../util/message_response.dart';
import 'package:week_of_year/week_of_year.dart';
import 'package:intl/intl.dart';

class MonthPage extends StatefulWidget {
  final bool isOnline;

  MonthPage(this.isOnline);

  @override
  State<StatefulWidget> createState() => _MonthPage();
}

class _MonthPage extends State<MonthPage> {
  bool online = true;
  bool isLoading = false;

  late StreamSubscription<ConnectivityResult> subscription;

  List<Entry> allEntries = [];
  List<Symptom> symptoms = [];
  List<Doctor> topDoctors = [];

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
      } else {
        online = false;
      }
    });

    Future.delayed(Duration.zero, () {
      getData();
    });
  }

  getTopDoctors() async {
    var doctors = allEntries.map((element) => element.doctor).toList();
    var doctorCount = Map();
    doctors.forEach((element) {
      if (!doctorCount.containsKey(element)) {
        doctorCount[element] = 1;
      } else {
        doctorCount[element] += 1;
      }
    });

    doctorCount.forEach((key, value) {
      topDoctors.add(Doctor(doctor: key, symptomCount: value));
    });

    topDoctors.sort((b, a) => a.symptomCount.compareTo(b.symptomCount));
    topDoctors = topDoctors.take(3).toList();
  }

  getWeeks() async {
    var monthlySymptoms = Map();
    allEntries.sort((a, b) => a.date.compareTo(b.date));
    DateFormat dateFormat = DateFormat("yyyy-MM-dd");
    for (var d in allEntries) {
      DateTime dateTime = dateFormat.parse(d.date);
      int month = dateTime.month;
      log("month of year for ${d.date} ${month}");

      if (!monthlySymptoms.containsKey(month)) {
        monthlySymptoms[month] = 1;
      } else {
        monthlySymptoms[month] += 1;
      }
    }

    monthlySymptoms.forEach((key, value) {
      symptoms.add(Symptom(month: key, symptomCount: value));
    });
  }

  getData() async {
    setState(() => isLoading = true);

    if (online == true) {
      try {
        allEntries = await ApiService.instance.getAllEntries();
        getWeeks();
        getTopDoctors();
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server get failed"),
        ));
      }
    } else {
      messageResponse(context, "Not available offline");
    }

    setState(() => isLoading = false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text("Release Year Section"),
        ),
        body: isLoading
            ? const Center(child: CircularProgressIndicator())
            : Row(
          children: [
            Flexible(
              child: Container(
                color: Colors.blueGrey,
                child: ListView.builder(
                  itemCount: symptoms.length,
                  itemBuilder: (context, index) => ListTile(
                      title: Text('Month ${symptoms[index].month}'),
                      subtitle: Text(
                          "Symptom Count: ${symptoms[index].symptomCount}")),
                ),
              ),
            ),
            Flexible(
              child: Container(
                color: Colors.lightGreen,
                child: ListView.builder(
                    itemCount: topDoctors.length,
                    itemBuilder: (context, index) {
                      return ListTile(
                        title: Text(topDoctors[index].doctor),
                        subtitle: Text(
                            "Symptom count: ${topDoctors[index].symptomCount}"),
                      );
                    }),
              ),
            ),
          ],
        )

      // ListView.builder(
      //     itemCount: symptoms.length,
      //  itemBuilder: (context, index) {
      //   return ListTile(
      //     title: Text('Month ${symptoms[index].month}'),
      //     subtitle:
      //         Text("Symptom Count: ${symptoms[index].symptomCount}"),
      //   );
      //     }),
    );
  }

  @override
  void dispose() {
    super.dispose();
    subscription.cancel();
  }
}