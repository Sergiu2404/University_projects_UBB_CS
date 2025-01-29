import 'package:client/model/tip.dart';
import 'package:flutter/material.dart';

import 'textBox.dart';

class UpdatePage extends StatefulWidget {
  Tip tip;
  UpdatePage(this.tip);
  @override
  State<StatefulWidget> createState() => _UpdatePage();
}

class _UpdatePage extends State<UpdatePage> {
  late TextEditingController controllerDifficulty;

  @override
  void initState() {
    controllerDifficulty =
    new TextEditingController(text: widget.tip.difficulty);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text("Update difficulty for ${widget.tip.name}")),
        body: ListView(
          children: [
            TextBox(controllerDifficulty, "Difficulty"),
            ElevatedButton(
                onPressed: () {
                  String difficulty = controllerDifficulty.text;
                  String invalidMessage = "";

                  if (difficulty.isEmpty) {
                    invalidMessage += "\nEmpty difficulty";
                  }

                  if (invalidMessage == "") {
                    Navigator.pop(context, difficulty);
                  }
                  else{
                    showDialog(
                        context: context,
                        builder: (_) => AlertDialog(
                          title: Text("Invalid difficulty"),
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
                child: Text("Update difficulty"))
          ],
        ));
  }
}