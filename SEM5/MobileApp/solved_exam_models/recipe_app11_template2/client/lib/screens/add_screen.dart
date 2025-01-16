import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'package:connectivity_plus/connectivity_plus.dart';

import '../model/entity.dart';
import '../util/message_response.dart';
import '../util/text_box.dart';

class AddPage extends StatefulWidget {
  final String type;
  AddPage(this.type);
  @override
  State<StatefulWidget> createState() => _AddPage();
}

class _AddPage extends State<AddPage> {
  late TextEditingController controllerName;
  late TextEditingController controllerDetails;
  late TextEditingController controllerTime;
  late TextEditingController controllerType;
  late TextEditingController controllerRating;

  @override
  void initState() {
    controllerName = TextEditingController();
    controllerDetails = TextEditingController();
    controllerTime = TextEditingController();
    controllerType = TextEditingController();
    controllerRating = TextEditingController();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text("Add a health entry")),
        body: ListView(children: [
          TextBox(controllerName, "Name"),
          TextBox(controllerDetails, "Details"),
          TextBox(controllerTime, "Time"),
          TextBox(controllerType, "Type"),
          TextBox(controllerRating, "Rating"),
          ElevatedButton(
              onPressed: () {
                String Name = controllerName.text;
                String Details = controllerDetails.text;
                int Time = int.parse(controllerTime.text);
                String Type = controllerType.text;
                int Rating = int.parse(controllerRating.text);

                String invalidMessage = "";

                if (Name.isEmpty) {
                  invalidMessage += "\nEmpty Name field";
                }
                if (Details.isEmpty) {
                  invalidMessage += "\nEmpty Details field";
                }
                if (Time == 0) {
                  invalidMessage += "\nNonzero necessary for Time field";
                }
                if (Type.isEmpty) {
                  invalidMessage += "\nEmpty Type field";
                }
                if (Rating == 0) {
                  invalidMessage += "\nNonzero necessary for Rating field";
                }

                if (invalidMessage == "") {
                  Navigator.pop(
                      context,
                      Entry(
                          name: Name,
                          details: Details,
                          time: Time,
                          type: widget.type,
                          rating: Rating));
                } else {
                  showDialog(
                      context: context,
                      builder: (_) => AlertDialog(
                        title: Text("Invalid health entry"),
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