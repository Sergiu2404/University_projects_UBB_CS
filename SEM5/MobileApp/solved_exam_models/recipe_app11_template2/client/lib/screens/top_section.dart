// import 'dart:async';
// import 'dart:convert';
// import 'dart:developer';
//
// import 'package:flutter/material.dart';
// import 'package:connectivity_plus/connectivity_plus.dart';
// import 'package:client/model/statistics.dart';
//
// import '../api/api.dart';
// import '../model/entity.dart';
// import '../util/message_response.dart';
//
// class DoctorPage extends StatefulWidget {
//   final bool isOnline;
//
//   DoctorPage(this.isOnline);
//
//   @override
//   State<StatefulWidget> createState() => _DoctorPage();
// }
//
// class _DoctorPage extends State<DoctorPage> {
//   bool online = true;
//   bool isLoading = false;
//
//   late StreamSubscription<ConnectivityResult> subscription;
//
//   List<Entry> allEntries = [];
//   List<Doctor> topDoctors = [];
//
//   @override
//   void initState() {
//     super.initState();
//
//     online = widget.isOnline;
//
//     subscription = Connectivity()
//         .onConnectivityChanged
//         .listen((ConnectivityResult result) {
//       if (result == ConnectivityResult.wifi ||
//           result == ConnectivityResult.mobile) {
//         online = true;
//         getData();
//         // syncData();
//       } else {
//         online = false;
//
//         // getData();
//       }
//     });
//
//     Future.delayed(Duration.zero, () {
//       getData();
//     });
//   }
//
//   getTopDoctors() async {
//     var doctors = allEntries.map((element) => element.doctor).toList();
//     var doctorCount = Map();
//     doctors.forEach((element) {
//       if (!doctorCount.containsKey(element)) {
//         doctorCount[element] = 1;
//       } else {
//         doctorCount[element] += 1;
//       }
//     });
//
//     doctorCount.forEach((key, value) {
//       topDoctors.add(Doctor(doctor: key, symptomCount: value));
//     });
//
//     topDoctors.sort((b, a) => a.symptomCount.compareTo(b.symptomCount));
//     topDoctors = topDoctors.take(3).toList();
//   }
//
//   getData() async {
//     setState(() => isLoading = true);
//
//     if (online == true) {
//       try {
//         allEntries = await ApiService.instance.getAllEntries();
//         getTopDoctors();
//       } on Exception catch (_) {
//         ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
//           content: Text("Server get failed"),
//         ));
//       }
//     } else {
//       messageResponse(context, "Not available offline");
//     }
//
//     setState(() => isLoading = false);
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       appBar: AppBar(
//         title: Text("Top Section"),
//       ),
//       body: isLoading
//           ? const Center(child: CircularProgressIndicator())
//           : ListView.builder(
//           itemCount: topDoctors.length,
//           itemBuilder: (context, index) {
//             return ListTile(
//               title: Text(topDoctors[index].doctor),
//               subtitle:
//               Text("Symptom count: ${topDoctors[index].symptomCount}"),
//             );
//           }),
//     );
//   }
//
//   @override
//   void dispose() {
//     super.dispose();
//     subscription.cancel();
//   }
// }