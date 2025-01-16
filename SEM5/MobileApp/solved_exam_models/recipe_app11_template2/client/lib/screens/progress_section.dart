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

class RatePage extends StatefulWidget {
  final bool isOnline;

  RatePage(this.isOnline);

  @override
  State<StatefulWidget> createState() => _RatePage();
}

class _RatePage extends State<RatePage> {
  bool online = true;
  bool isLoading = false;

  late StreamSubscription<ConnectivityResult> subscription;

  List<Entry> allEntries = [];
  //List<Symptom> symptoms = [];
  List<Entry> topUnderratedRecipes = [];

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

  incrementRating(int id) async {
    try {
      log("Incrementing rating for recipe with id $id");
      // Call the API to increment the rating
      Entry? updatedEntry = await ApiService.instance.incrementRating(id);

      if (updatedEntry != null) {
        // Update the UI with the new rating
        setState(() {
          // Update the specific recipe in the topUnderratedRecipes list
          int index = topUnderratedRecipes.indexWhere((recipe) => recipe.id == id);
          if (index != -1) {
            topUnderratedRecipes[index] = updatedEntry;
          }

          // Update the specific recipe in the allEntries list, if applicable
          int allEntriesIndex = allEntries.indexWhere((recipe) => recipe.id == id);
          if (allEntriesIndex != -1) {
            allEntries[allEntriesIndex] = updatedEntry;
          }
        });
        log("Rating incremented for recipe: ${updatedEntry.name}, New Rating: ${updatedEntry.rating}");
      }
    } catch (e) {
      log("Error incrementing rating: $e");
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text("Failed to increment rating for recipe with id $id"),
      ));
    }
  }


  getTopUnderratedRecipes() async {
    var topRecipes = await ApiService.instance.getTopUnderrated();

    setState(() {
      topUnderratedRecipes = List<Entry>.from(topRecipes);
    });
    // var topRecipes = List<Entry>.from(allEntries);
    //
    // topRecipes.sort((a, b) => a.rating.compareTo(b.rating));
    // topUnderratedRecipes = topRecipes.take(10).toList();

    for(var recipe in topUnderratedRecipes){
      log("${recipe.name}, ${recipe.rating}");
    }
  }

  getData() async {
    setState(() => isLoading = true);

    if (online == true) {
      try {
        allEntries = await ApiService.instance.getAllEntries();
        //getWeeks();
        getTopUnderratedRecipes();
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
            // Flexible(
            //   child: Container(
            //     color: Colors.blueGrey,
            //     child: ListView.builder(
            //       itemCount: symptoms.length,
            //       itemBuilder: (context, index) => ListTile(
            //           title: Text('Month ${symptoms[index].month}'),
            //           subtitle: Text(
            //               "Symptom Count: ${symptoms[index].symptomCount}")),
            //     ),
            //   ),
            // ),
            Flexible(
              child: ListView.builder(
                  itemCount: topUnderratedRecipes.length,
                  itemBuilder: (context, index) {
                    return ListTile(
                      title: Text(topUnderratedRecipes[index].name),
                      subtitle: Text("rating: ${topUnderratedRecipes[index].rating}"),
                      trailing: IconButton(
                        icon: Icon(Icons.add),
                        onPressed: () => incrementRating(topUnderratedRecipes[index].id!),
                      ),
                    );
                  }),
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