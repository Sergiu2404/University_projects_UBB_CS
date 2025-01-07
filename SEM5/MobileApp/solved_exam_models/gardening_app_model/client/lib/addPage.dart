import 'dart:async';

import 'package:client/api/network.dart';
import 'package:client/homePage.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'package:connectivity_plus/connectivity_plus.dart';

import 'db/tip_db.dart';
import 'messageResponse.dart';

import 'model/tip.dart';
import 'textBox.dart';

class AddPage extends StatefulWidget {
  final String _category;
  AddPage(this._category);
  @override
  State<StatefulWidget> createState() => _AddPage();
}

class _AddPage extends State<AddPage> {
  late TextEditingController controllerName;
  late TextEditingController controllerDescription;
  late TextEditingController controllerMaterials;
  late TextEditingController controllerSteps;
  late TextEditingController controllerDifficulty;

  @override
  void initState() {
    controllerName = new TextEditingController();
    controllerDescription = new TextEditingController();
    controllerMaterials = new TextEditingController();
    controllerSteps = new TextEditingController();
    controllerDifficulty = new TextEditingController();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text("Add a tip")),
        body: ListView(children: [
          TextBox(controllerName, "Name"),
          TextBox(controllerDescription, "Description"),
          TextBox(controllerMaterials, "Materials"),
          TextBox(controllerSteps, "Steps"),
          TextBox(controllerDifficulty, "Difficulty"),
          ElevatedButton(
              onPressed: () {
                String name = controllerName.text;
                String description = controllerDescription.text;
                String materials = controllerMaterials.text;
                String steps = controllerSteps.text;
                String difficulty = controllerDifficulty.text;

                String invalidMessage = "";

                if (name.isEmpty) {
                  invalidMessage += "\nEmpty name";
                }
                if (description.isEmpty) {
                  invalidMessage += "\nEmpty description";
                }
                if (materials.isEmpty) {
                  invalidMessage += "\nEmpty materials";
                }
                if (steps.isEmpty) {
                  invalidMessage += "\nEmpty steps";
                }
                if (difficulty.isEmpty) {
                  invalidMessage += "\nEmpty difficulty";
                }

                if (invalidMessage == "") {
                  Navigator.pop(
                      context,
                      Tip(
                          name: name,
                          description: description,
                          difficulty: difficulty,
                          steps: steps,
                          materials: materials,
                          category: widget._category,
                          isLocal: false));
                } else {
                  showDialog(
                      context: context,
                      builder: (_) => AlertDialog(
                        title: Text("Invalid tip"),
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
              child: Text("Add tip"))
        ]));
  }
}