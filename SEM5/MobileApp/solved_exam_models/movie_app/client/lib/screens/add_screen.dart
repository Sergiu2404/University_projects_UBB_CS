import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'package:connectivity_plus/connectivity_plus.dart';

import '../model/entity.dart';
import '../util/message_response.dart';
import '../util/text_box.dart';

class AddPage extends StatefulWidget {
  final String genre;
  AddPage(this.genre);
  @override
  State<StatefulWidget> createState() => _AddPage();
}

class _AddPage extends State<AddPage> {
  late TextEditingController controllerName;
  late TextEditingController controllerDescription;
  late TextEditingController controllerDirector;
  late TextEditingController controllerYear;

  @override
  void initState() {
    controllerName = TextEditingController();
    controllerDescription = TextEditingController();
    controllerYear = TextEditingController();
    controllerDirector = TextEditingController();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text("Add a new entry")),
        body: ListView(children: [
          TextBox(controllerName, "Name"),
          TextBox(controllerDescription, "Descr"),
          TextBox(controllerDirector, "Director"),
          TextBox(controllerYear, "Year"),
          ElevatedButton(
              onPressed: () {
                String name = controllerName.text;
                String descr = controllerDescription.text;
                String director = controllerDirector.text;

                int year = 0;
                try{
                  year = int.parse(controllerYear.text);
                } catch(err){
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(
                      content: Text("Invalid year: Please enter a valid number"),
                    ),
                  );
                }

                String invalidMessage = "";

                if (name.isEmpty) {
                  invalidMessage += "\nEmpty name field";
                }
                if (descr.isEmpty) {
                  invalidMessage += "\nEmpty description field";
                }
                if (widget.genre.isEmpty) {
                  invalidMessage += "\nEmpty genre field";
                }
                if (director.isEmpty) {
                  invalidMessage += "\nEmpty dir field";
                }

                if (invalidMessage == "") {
                  Navigator.pop(
                      context,
                      Entry(
                          name: name,
                          description: descr,
                          genre: widget.genre,
                          director: director,
                          year: year
                      ));
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
              child: Text("Add entry"))
        ]));
  }
}