import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'package:connectivity_plus/connectivity_plus.dart';

import '../model/entity.dart';

class AddPage extends StatefulWidget {
  final String date;
  AddPage(this.date);
  @override
  State<StatefulWidget> createState() => _AddPage();
}

class _AddPage extends State<AddPage> {
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  late TextEditingController typeController;
  late TextEditingController durationController;
  late TextEditingController priorityController;
  late TextEditingController categoryController;
  late TextEditingController descriptionController;

  @override
  void initState() {
    typeController = TextEditingController();
    durationController = TextEditingController();
    priorityController = TextEditingController();
    categoryController = TextEditingController();
    descriptionController = TextEditingController();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Add a New Entry")),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: typeController,
                decoration: const InputDecoration(
                  labelText: "Type",
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Type cannot be empty";
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16),

              // Duration Field
              TextFormField(
                controller: durationController,
                decoration: const InputDecoration(
                  labelText: "Duration (in minutes)",
                  border: OutlineInputBorder(),
                ),
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Duration cannot be empty";
                  }
                  final duration = int.tryParse(value);
                  if (duration == null || duration <= 0) {
                    return "Please enter a valid positive number for duration";
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16),

              // Priority Field
              TextFormField(
                controller: priorityController,
                decoration: const InputDecoration(
                  labelText: "Priority",
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Priority cannot be empty";
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16),

              // Category Field
              TextFormField(
                controller: categoryController,
                decoration: const InputDecoration(
                  labelText: "Category",
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Category cannot be empty";
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16),

              // Description Field
              TextFormField(
                controller: descriptionController,
                decoration: const InputDecoration(
                  labelText: "Description",
                  border: OutlineInputBorder(),
                ),
                maxLines: 3,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Description cannot be empty";
                  }
                  return null;
                },
              ),
              const SizedBox(height: 24),

              // Submit Button
              ElevatedButton(
                onPressed: () {
                  if (_formKey.currentState?.validate() == true) {
                    String invalidMessage = "";
                    final duration = double.tryParse(durationController.text);

                    if (duration == null || duration < 0){
                      invalidMessage += "\nEmpty duration field or lower than 0";
                    }
                    if (typeController.text.isEmpty) {
                      invalidMessage += "\nEmpty type field";
                    }
                    if (priorityController.text.isEmpty) {
                      invalidMessage += "\nEmpty description field";
                    }
                    if (categoryController.text.isEmpty) {
                      invalidMessage += "\nEmpty genre field";
                    }
                    if (descriptionController.text.isEmpty) {
                      invalidMessage += "\nEmpty dir field";
                    }

                    final newEntry = Entry(
                      date: widget.date,
                      type: typeController.text,
                      duration: double.parse(durationController.text),
                      priority: priorityController.text,
                      category: categoryController.text,
                      description: descriptionController.text,
                    );

                    Navigator.pop(context, newEntry);
                  }
                },
                child: const Text("Add Entry"),
              ),
            ],
          ),
        ),
      ),
    );
  }

  @override
  void dispose() {
    typeController.dispose();
    durationController.dispose();
    priorityController.dispose();
    categoryController.dispose();
    descriptionController.dispose();
    super.dispose();
  }
}