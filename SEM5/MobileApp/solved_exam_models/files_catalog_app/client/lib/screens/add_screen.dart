import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'package:connectivity_plus/connectivity_plus.dart';

import '../model/entity.dart';
import '../util/message_response.dart';
import '../util/text_box.dart';

class AddPage extends StatefulWidget {
  AddPage();
  @override
  State<StatefulWidget> createState() => _AddPage();
}

class _AddPage extends State<AddPage> {
  late TextEditingController controllerName;
  late TextEditingController controllerStatus;
  late TextEditingController controllerSize;
  late TextEditingController controllerLocation;
  late TextEditingController controllerUsage;

  @override
  void initState() {
    controllerName = TextEditingController();
    controllerStatus = TextEditingController();
    controllerSize = TextEditingController();
    controllerLocation = TextEditingController();
    controllerUsage = TextEditingController();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text("Add a new entry")),
        body: ListView(children: [
          TextBox(controllerName, "Name"),
          TextBox(controllerStatus, "Status"),
          TextBox(controllerSize, "Size"),
          TextBox(controllerLocation, "Location"),
          TextBox(controllerUsage, "Usage"),
          ElevatedButton(
              onPressed: () {
                String name = controllerName.text;
                String status = controllerStatus.text;
                String location = controllerLocation.text;

                String invalidMessage = "";

                int? size;
                int? usage;

                try {
                  // Attempt to parse size and usage
                  size = int.parse(controllerSize.text);
                } catch (e) {
                  // If parsing fails, add message
                  invalidMessage += "\nInvalid size field";
                }

                try {
                  // Attempt to parse usage
                  usage = int.parse(controllerUsage.text);
                } catch (e) {
                  // If parsing fails, add message
                  invalidMessage += "\nInvalid usage field";
                }
                
                if (name.isEmpty) {
                  invalidMessage += "\nEmpty name field";
                }
                if (status.isEmpty) {
                  invalidMessage += "\nEmpty status field";
                }
                if (location.isEmpty) {
                  invalidMessage += "\nEmpty location field";
                }

                if (invalidMessage == "") {
                  Navigator.pop(
                      context,
                      Entry(
                          name: name,
                          status: status,
                          size: size ?? 0,
                          location: location,
                          usage: usage ?? 0));
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