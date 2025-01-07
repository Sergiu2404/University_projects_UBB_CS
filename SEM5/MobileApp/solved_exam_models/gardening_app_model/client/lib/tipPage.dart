import 'dart:async';

import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:client/addPage.dart';
import 'package:client/model/category.dart';
import 'package:flutter/material.dart';

import 'api/api.dart';
import 'api/network.dart';
import '../model/tip.dart';
import 'db/tip_db.dart';
import 'messageResponse.dart';

List<Category> tipsPerCategoriesSaved = [];

class TipPage extends StatefulWidget {
  final String _category;
  final bool isOnline;

  TipPage(this._category, this.isOnline);

  @override
  State<StatefulWidget> createState() => _TipPage();
}

class _TipPage extends State<TipPage> {
  Map _source = {ConnectivityResult.none: false};
  final NetworkConnectivity _networkConnectivity = NetworkConnectivity.instance;
  String string = '';
  bool online = true;

  late String category;

  bool isLoading = false;
  List<Tip> tipsPerCategory = [];
  bool saved = false;

  late StreamSubscription<ConnectivityResult> subscription;

  @override
  void initState() {
    print("Init stateeee");
    super.initState();
    online = widget.isOnline;
    category = widget._category;
    subscription = Connectivity()
        .onConnectivityChanged
        .listen((ConnectivityResult result) {
      print("Something new? " + result.toString());
      if (result == ConnectivityResult.wifi ||
          result == ConnectivityResult.mobile) {
        online = true;
        getData();
        //syncData();
      } else {
        online = false;
        getData();
        // messageResponse(
        //     context, "Tips: No internet. Local data will be shown.");
      }
    });

    //connection();
    Future.delayed(Duration.zero, () {
      getData();
    });
  }

  bool checkSaved() {
    final category = tipsPerCategoriesSaved
        .where((element) => element.category == widget._category);
    return category.isEmpty;
  }

  @override
  void dispose() {
    // _networkConnectivity.myStream.listen((event) {}).pause();
    // _networkConnectivity.disposeStream();
    super.dispose();
    subscription.cancel();
  }

  getData() async {
    // bool isConnected = await this.checkConnected();
    // if (!mounted) return;
    // if (mounted)
    setState(() => isLoading = true);
    if (online == true) {
      tipsPerCategory = await ApiService.instance.getTipsByCategory(category);
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text("Back at it!"),
      ));
      syncData();
    } else {
      if (checkSaved() == false) {
        tipsPerCategory = await TipDB.instance.getByCategory(category);
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Offline tips"),
        ));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Tips have not been saved yet to db"),
        ));
      }
    }
    // if (mounted)
    setState(() => isLoading = false);
  }

  syncData() async {
    final category = tipsPerCategoriesSaved
        .where((element) => element.category == widget._category);
    print("Syncing >> " + category.isEmpty.toString());
    if (category.isEmpty) {
      // sync data and add category to list
      for (var tip in tipsPerCategory) {
        tip.isLocal = true;
        TipDB.instance.addTip(Tip(
            category: tip.category,
            description: tip.description,
            difficulty: tip.difficulty,
            name: tip.name,
            materials: tip.materials,
            steps: tip.steps,
            isLocal: true));
        print("Add " + tip.name + " to db ");
      }
      tipsPerCategoriesSaved
          .add(Category(category: widget._category, saved: true));
      saved = true;
    }
    // for (var c in tipsPerCategory) {
    //   print(c.category + " >> " + c.isLocal.toString());
    // }
    // List<Tip> saveLocal =
    //     tipsPerCategory.where((e) => e.isLocal == false).toList();
    // print("Should sync to db: " + saveLocal.toString());
  }

  void addTip(Tip tip) async {
    setState(() => isLoading = true);
    if (online) {
      final Tip newTip = await ApiService.instance.addTip(tip);
      setState(() {
        tipsPerCategory.add(newTip);
      });
      TipDB.instance.addTip(tip);
    }
    setState(() => isLoading = false);
  }

  void deleteTipBack(Tip tip) async {
    setState(() => isLoading = true);
    if (online) {
      setState(() {
        print("Tip to delete: " + tip.name + " " + tip.id.toString());
        ApiService.instance.deleteTip(tip.id!);
        tipsPerCategory.remove(tip);
        Navigator.pop(context);
        // delete from db too
        TipDB.instance.delete(tip.name);
      });
    }
    setState(() => isLoading = false);
  }

  void deleteTip(BuildContext context, Tip tip) {
    showDialog(
        context: context,
        builder: (_) => AlertDialog(
          title: Text("Delete tip"),
          content:
          Text("Are you sure you want to delete tip: ${tip.name}?"),
          actions: [
            TextButton(
                onPressed: () {
                  deleteTipBack(tip);
                },
                child: Text("Yes", style: TextStyle(color: Colors.red))),
            TextButton(
                onPressed: () {
                  Navigator.pop(context);
                },
                child: Text("No", style: TextStyle(color: Colors.green)))
          ],
        ));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Tips for " + category),
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
          itemCount: tipsPerCategory.length,
          itemBuilder: (context, index) {
            return ListTile(
              title: Text(tipsPerCategory[index].name),
              subtitle: Text(getTipDetails(tipsPerCategory[index])),
              onTap: () {},
              trailing: IconButton(
                icon: const Icon(Icons.delete),
                color: Colors.amber,
                onPressed: () {
                  if (online) {
                    deleteTip(context, tipsPerCategory[index]);
                  } else {
                    ScaffoldMessenger.of(context)
                        .showSnackBar(const SnackBar(
                      content: Text("Cannot delete while offline"),
                    ));
                  }
                },
              ),
            );
          }),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          if (online) {
            Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (_) => AddPage(widget._category))).then((newTip) {
              if (newTip != null) {
                setState(() {
                  addTip(newTip);
                  messageResponse(context, "New tip ${newTip.name} was added");
                });
              }
            });
          } else {
            ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
              content: Text("Cannot add while offline"),
            ));
          }
        },
        tooltip: "Add Tip",
        child: Icon(Icons.add),
      ),
    );
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

