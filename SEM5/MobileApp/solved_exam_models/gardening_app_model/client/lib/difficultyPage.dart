import 'dart:async';
import 'dart:isolate';

import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:client/api/api.dart';
import 'package:client/updatePage.dart';
import 'package:flutter/material.dart';

import 'messageResponse.dart';
import 'model/tip.dart';

class DifficultyPage extends StatefulWidget {
  final bool isOnline;
  DifficultyPage(this.isOnline);
  @override
  State<StatefulWidget> createState() => _DifficultyPage();
}

class _DifficultyPage extends State<DifficultyPage> {
  bool online = true;
  bool isLoading = false;

  List<Tip> topTips = [];

  late StreamSubscription<ConnectivityResult> subscription;

  @override
  void initState() {
    super.initState();

    online = widget.isOnline;

    subscription = Connectivity()
        .onConnectivityChanged
        .listen((ConnectivityResult result) {
      print("Something new - categories ? " + result.toString());
      if (result == ConnectivityResult.wifi ||
          result == ConnectivityResult.mobile) {
        online = true;
        getData();
      } else {
        online = false;
      }
    });

    // connection();
    Future.delayed(Duration.zero, () {
      getData();
    });
    // getData();
  }

  getData() async {
    setState(() => isLoading = true);
    if (online) {
      topTips = await ApiService.instance.getEasiestTips();
    } else {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text("No offline support"),
      ));
    }
    setState(() => isLoading = false);
  }

  updateData(int? id, String difficulty, int index) async {
    setState(() => isLoading = true);
    if (online) {
      var newtip = ApiService.instance.updateDifficulty(id!, difficulty);
      // setState(() {
      //   topTips.removeAt(index);
      //   topTips.insert(index, newtip as Tip);
      // });
    } else {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text("No offline support"),
      ));
    }
    setState(() => isLoading = false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text("Difficulty Page")),
        body: isLoading
            ? const Center(child: CircularProgressIndicator())
            : ListView.builder(
            itemCount: topTips.length,
            itemBuilder: (context, index) {
              return ListTile(
                title: Text(topTips[index].name),
                subtitle: Text(getTipDetails(topTips[index])),
                trailing: IconButton(
                    icon: const Icon(Icons.edit),
                    color: Colors.amber,
                    onPressed: () {
                      if (online) {
                        Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (_) =>
                                    UpdatePage(topTips[index])))
                            .then((difficulty) {
                          if (difficulty != null) {
                            setState(() {
                              updateData(
                                  topTips[index].id, difficulty, index);
                              getData();

                              messageResponse(context,
                                  "Difficulty modified for ${topTips[index].name}");
                            });
                          }
                        });
                      } else {
                        ScaffoldMessenger.of(context)
                            .showSnackBar(const SnackBar(
                          content: Text("Cannot update while offline"),
                        ));
                      }
                    }),
              );
            }));
  }

  String getTipDetails(Tip t) {
    return "Category: " +
        t.category +
        "\nDescription: " +
        t.description +
        "\nSteps: " +
        t.steps +
        "\nDifficulty: " +
        t.difficulty;
  }
}