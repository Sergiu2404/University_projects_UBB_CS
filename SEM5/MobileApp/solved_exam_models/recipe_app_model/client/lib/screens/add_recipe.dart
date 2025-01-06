import 'package:flutter/material.dart';

import '../model/recipe.dart';
import '../util/text_box.dart';


class AddPage extends StatefulWidget {
  final String category;
  AddPage(this.category);
  @override
  State<StatefulWidget> createState() => _AddPage();
}

class _AddPage extends State<AddPage> {
  late TextEditingController controllerName;
  late TextEditingController controllerDescription;
  late TextEditingController controllerIngredients;
  late TextEditingController controllerInstructions;
  late TextEditingController controllerDifficulty;
  late TextEditingController controllerCategory;


  @override
  void initState() {
    controllerName = TextEditingController();
    controllerDescription = TextEditingController();
    controllerDifficulty = TextEditingController();
    controllerIngredients = TextEditingController();
    controllerInstructions = TextEditingController();
    controllerCategory = TextEditingController();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: const Text("Add a recipe")),
        body: ListView(children: [
          TextBox(controllerName, "Name"),
          TextBox(controllerDescription, "Description"),
          TextBox(controllerDifficulty, "Difficulty"),
          TextBox(controllerIngredients, "Ingredients"),
          TextBox(controllerInstructions, "Instructions"),
          TextBox(controllerCategory, "Category"),
          ElevatedButton(
              onPressed: () {
                String name = controllerName.text;
                String description = controllerDescription.text;
                String difficulty = controllerDifficulty.text;
                String ingredients = controllerIngredients.text;
                String instructions = controllerInstructions.text;
                String category = controllerCategory.text;

                String invalidMessage = "";

                if (name.isEmpty) {
                  invalidMessage += "\nEmpty name";
                }
                if (description.isEmpty) {
                  invalidMessage += "\nEmpty description";
                }
                if (ingredients.isEmpty) {
                  invalidMessage += "\nEmpty ingredients";
                }
                if (instructions.isEmpty) {
                  invalidMessage += "\nEmpty instructions";
                }
                if (difficulty.isEmpty) {
                  invalidMessage += "\nEmpty difficulty";
                }
                if (category.isEmpty) {
                  invalidMessage += "\nEmpty category";
                }

                if (invalidMessage == "") {
                  Navigator.pop(
                      context,
                      Recipe(
                          name: name,
                          description: description,
                          difficulty: difficulty,
                          ingredients: ingredients,
                          instructions: instructions,
                          // category: widget.category
                          category: category
                      )
                  );
                } else {
                  showDialog(
                      context: context,
                      builder: (_) => AlertDialog(
                        title: const Text("Invalid recipe"),
                        content: Text(invalidMessage),
                        actions: [
                          TextButton(
                            onPressed: () {
                              Navigator.pop(context);
                            },
                            child: const Text(
                              "Try again",
                              style: TextStyle(color: Colors.purple),
                            ),
                          ),
                          TextButton(
                              onPressed: () {
                                Navigator.pop(context);
                                Navigator.pop(context);
                              },
                              child: const Text("Abort",
                                  style: TextStyle(color: Colors.indigo)))
                        ],
                      ));
                }
              },
              child: const Text("Add category"))
        ]));
  }
}