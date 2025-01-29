import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'package:connectivity_plus/connectivity_plus.dart';

import '../model/entity.dart';
import '../util/message_response.dart';
import '../util/text_box.dart';

class AddPage extends StatefulWidget {
  final String date;
  AddPage(this.date);
  @override
  State<StatefulWidget> createState() => _AddPage();
}

class _AddPage extends State<AddPage> {
  late TextEditingController controllerType; // Amount, Amount, Category, Description
  late TextEditingController controllerAmount;
  late TextEditingController controllerCategory;
  late TextEditingController controllerDescription;

  @override
  void initState() {
    controllerType = TextEditingController();
    controllerAmount = TextEditingController();
    controllerAmount = TextEditingController();
    controllerCategory = TextEditingController();
    controllerDescription = TextEditingController();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text("Add a health entry")),
        body: ListView(children: [
          TextBox(controllerType, "Type"),
          TextBox(controllerAmount, "Amount"),
          TextBox(controllerCategory, "Category"),
          TextBox(controllerDescription, "Description"),
          ElevatedButton(
              onPressed: () {
                String Type = controllerType.text;
                String Amount = controllerAmount.text;
                String Category = controllerCategory.text;
                String Description = controllerDescription.text;

                String invalidMessage = "";

                if (Type.isEmpty) {
                  invalidMessage += "\nEmpty Type field";
                }
                if (Amount.isEmpty) {
                  invalidMessage += "\nEmpty Amount field";
                }
                if (Amount.isEmpty) {
                  invalidMessage += "\nEmpty Amount field";
                }
                if (Category.isEmpty) {
                  invalidMessage += "\nEmpty Category field";
                }
                if (Description.isEmpty) {
                  invalidMessage += "\nEmpty Description field";
                }

                if (invalidMessage == "") {
                  Navigator.pop(
                      context,
                      Entry(
                          date: widget.date,
                          type: Type,
                          amount: int.tryParse(Amount.toString()) ?? 0,
                          category: Category,
                          description: Description));
                } else {
                  showDialog(
                      context: context,
                      builder: (_) => AlertDialog(
                        title: Text("Invalid entry"),
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
              child: Text("Add new entry"))
        ]));
  }
}