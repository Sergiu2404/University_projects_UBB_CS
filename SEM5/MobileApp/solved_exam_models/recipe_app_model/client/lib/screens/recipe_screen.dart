import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:client/db/db.dart';
import 'package:client/model/recipe.dart';
import 'package:client/screens/main_section.dart';

import '../api/api.dart';
import '../util/messageResponse.dart';
import 'add_recipe.dart';

List<String> savedCategories = [];

class RecipePage extends StatefulWidget {
  final String category;
  final bool isOnline;

  RecipePage(this.category, this.isOnline);

  @override
  State<StatefulWidget> createState() => _RecipePage();
}

class _RecipePage extends State<RecipePage> {
  bool online = true;

  late String category;

  bool isLoading = false;
  List<Recipe> recipesPerCategory = [];
  bool saved = false;

  late StreamSubscription<ConnectivityResult> subscription;

  @override
  void initState() {
    super.initState();
    online = widget.isOnline;
    category = widget.category;
    subscription = Connectivity()
        .onConnectivityChanged
        .listen((ConnectivityResult result) {
      if (result == ConnectivityResult.wifi ||
          result == ConnectivityResult.mobile) {
        online = true;
        getData();
      } else {
        online = false;
        getData();
      }
    });

    //connection();
    Future.delayed(Duration.zero, () {
      getData();
    });
  }

  bool checkSaved() {
    final genres =
    savedCategories.where((element) => element == widget.category);
    return genres.isEmpty;
  }

  getData() async {
    setState(() => isLoading = true);
    if (online == true) {
      recipesPerCategory =
      await ApiService.instance.getRecipesByCategory(category);
      // ignore: use_build_context_synchronously
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text("Back at it!"),
      ));
      syncData();
    } else {
      if (checkSaved() == false) {
        recipesPerCategory = await RecipeDB.instance.getByCategory(category);
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Offline recipes are being currently displayed"),
        ));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Recipes have not been saved yet to db"),
        ));
      }
    }
    setState(() => isLoading = false);
  }

  syncData() async {
    log("Syncing >> ${checkSaved()}");
    if (checkSaved()) {
      RecipeDB.instance.deleteAllRecipesByCategory(category);
      // sync data and add category to list
      for (var recipe in recipesPerCategory) {
        RecipeDB.instance.addRecipe(recipe);
      }
      savedCategories.add(category);
    }
  }

  void addRecipe(Recipe recipe) async {
    setState(() => isLoading = true);
    if (online) {
      final Recipe newRecipe = await ApiService.instance.addRecipe(recipe);
      setState(() {
        if (newRecipe.category == widget.category) {
          recipesPerCategory.add(newRecipe);
        }
      });
      RecipeDB.instance.addRecipe(newRecipe);
    }
    setState(() => isLoading = false);
  }

  void deleteRecipeBack(Recipe recipe) async {
    setState(() => isLoading = true);
    if (online) {
      setState(() {
        ApiService.instance.deleteRecipe(recipe.id!);
        recipesPerCategory.remove(recipe);
        Navigator.pop(context);
        RecipeDB.instance.deleteRecipe(recipe.id!);
      });
    }
    setState(() => isLoading = false);
  }

  void deleteRecipe(BuildContext context, Recipe recipe) {
    showDialog(
        context: context,
        builder: (_) => AlertDialog(
          title: Text("Delete Recipe"),
          content: Text(
              "Are you sure you want to delete recipe: ${recipe.name}?"),
          actions: [
            TextButton(
                onPressed: () {
                  deleteRecipeBack(recipe);
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
        title: Text("Recipes for $category"),
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
          itemCount: recipesPerCategory.length,
          itemBuilder: (context, index) {
            return ListTile(
              title: Text(recipesPerCategory[index].name),
              subtitle: Text(getRecipeDetails(recipesPerCategory[index])),
              onTap: () {},
              trailing: IconButton(
                icon: const Icon(Icons.delete),
                color: Colors.amber,
                onPressed: () {
                  if (online) {
                    deleteRecipe(context, recipesPerCategory[index]);
                    //(context, tipsPerCategory[index]);
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
            Navigator.push(context,
                MaterialPageRoute(builder: (_) => AddPage(widget.category)))
                .then((newRecipe) {
              if (newRecipe != null) {
                setState(() {
                  addRecipe(newRecipe);
                });
              }
            });
          } else {
            ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
              content: Text("Cannot add while offline"),
            ));
          }
        },
        tooltip: "Add Recipe",
        child: Icon(Icons.add),
      ),
    );
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