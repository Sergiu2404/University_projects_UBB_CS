import 'dart:async';
import 'dart:isolate';

import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:flutter/material.dart';
import 'package:client/screens/update_recipe.dart';

import '../api/api.dart';
import '../db/db.dart';
import '../model/recipe.dart';
import '../util/messageResponse.dart';

class DifficultyPage extends StatefulWidget {
  final bool isOnline;
  DifficultyPage(this.isOnline);
  @override
  State<StatefulWidget> createState() => _DifficultyPage();
}

class _DifficultyPage extends State<DifficultyPage> {
  bool online = true;
  bool isLoading = false;

  List<Recipe> topRecipes = [];
  List<Recipe> allRecipes = [];

  late StreamSubscription<ConnectivityResult> subscription;

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

  getTopRecipes() async {
    topRecipes = allRecipes;
    topRecipes.sort((a, b) {
      int difficulty = a.difficulty.compareTo(b.difficulty);
      if (difficulty == 0) {
        return a.category.compareTo(b.category);
      }
      return difficulty;
    });
    // topRecipes.sort((a, b) => a.category.compareTo(b.category));
    topRecipes = topRecipes.take(10).toList();
  }

  getData() async {
    setState(() => isLoading = true);
    if (online) {
      allRecipes = await ApiService.instance.getAllRecipes();
      getTopRecipes();
    } else {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text("No offline support yet"),
      ));
    }
    setState(() => isLoading = false);
  }

  updateData(int? id, String difficulty, int index) async {
    setState(() => isLoading = true);
    if (online) {
      var newRecipe =
      await ApiService.instance.updateDifficulty(id!, difficulty);
      RecipeDB.instance.updateRecipe(newRecipe);
      // update in db too
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
        appBar: AppBar(title: const Text("Difficulty Page")),
        body: isLoading
            ? const Center(child: CircularProgressIndicator())
            : ListView.builder(
            itemCount: topRecipes.length,
            itemBuilder: (context, index) {
              return ListTile(
                title: Text(topRecipes[index].name),
                subtitle: Text(getRecipeDetails(topRecipes[index])),
                trailing: IconButton(
                    icon: const Icon(Icons.edit),
                    color: Colors.amber,
                    onPressed: () {
                      if (online) {
                        Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (_) =>
                                    UpdatePage(topRecipes[index])))
                            .then((difficulty) {
                          if (difficulty != null) {
                            setState(() {
                              updateData(
                                  topRecipes[index].id, difficulty, index);
                              getData();

                              messageResponse(context,
                                  "Difficulty modified for ${topRecipes[index].name}");
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

  String getRecipeDetails(Recipe r) {
    return "Description: ${r.description}\nDifficulty: ${r.difficulty}\nCategory: ${r.category}\nIngredients: ${r.ingredients}\nInstructions: ${r.instructions}";
  }

  @override
  void dispose() {
    super.dispose();
    subscription.cancel();
  }
}