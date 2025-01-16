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
  late TextEditingController
  controllerSymptom; // medication, dosage, doctor, notes
  late TextEditingController controllerMedication;
  late TextEditingController controllerDosage;
  late TextEditingController controllerDoctor;
  late TextEditingController controllerNotes;

  @override
  void initState() {
    controllerSymptom = TextEditingController();
    controllerMedication = TextEditingController();
    controllerDosage = TextEditingController();
    controllerDoctor = TextEditingController();
    controllerNotes = TextEditingController();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text("Add a health entry")),
        body: ListView(children: [
          TextBox(controllerSymptom, "Symptom"),
          TextBox(controllerMedication, "Medication"),
          TextBox(controllerDosage, "Dosage"),
          TextBox(controllerDoctor, "Doctor"),
          TextBox(controllerNotes, "Notes"),
          ElevatedButton(
              onPressed: () {
                String symptom = controllerSymptom.text;
                String medication = controllerMedication.text;
                String dosage = controllerDosage.text;
                String doctor = controllerDoctor.text;
                String notes = controllerNotes.text;

                String invalidMessage = "";

                if (symptom.isEmpty) {
                  invalidMessage += "\nEmpty symptom field";
                }
                if (medication.isEmpty) {
                  invalidMessage += "\nEmpty medication field";
                }
                if (dosage.isEmpty) {
                  invalidMessage += "\nEmpty dosage field";
                }
                if (doctor.isEmpty) {
                  invalidMessage += "\nEmpty doctor field";
                }
                if (notes.isEmpty) {
                  invalidMessage += "\nEmpty notes field";
                }

                if (invalidMessage == "") {
                  Navigator.pop(
                      context,
                      Entry(
                          date: widget.date,
                          symptom: symptom,
                          medication: medication,
                          dosage: dosage,
                          doctor: doctor,
                          notes: notes));
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
              child: Text("Add health entry"))
        ]));
  }
}