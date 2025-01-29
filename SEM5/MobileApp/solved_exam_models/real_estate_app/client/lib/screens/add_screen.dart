import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'package:connectivity_plus/connectivity_plus.dart';

import '../models/entity.dart';
import '../util/messageResponse.dart';
import '../util/text_box.dart';

class AddPage extends StatefulWidget {
  final Address address;
  const AddPage({Key? key, required this.address}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _AddPage();
}

class _AddPage extends State<AddPage> {
  late TextEditingController controllerDate;
  late TextEditingController controllerType;
  late TextEditingController controllerBedrooms;
  late TextEditingController controllerBathrooms;
  late TextEditingController controllerPrice;
  late TextEditingController controllerArea;
  late TextEditingController controllerNotes;

  @override
  void initState() {
    controllerDate = TextEditingController();
    controllerType = TextEditingController();
    controllerBedrooms = TextEditingController();
    controllerBathrooms = TextEditingController();
    controllerPrice = TextEditingController();
    controllerArea = TextEditingController();
    controllerNotes = TextEditingController();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Add a property for ${widget.address.address}")),
      body: ListView(
        padding: const EdgeInsets.all(16.0),
        children: [
          TextBox(controllerDate, "Date - YYYY-MM-DD"),
          TextBox(controllerType, "Type"),
          TextBox(controllerBedrooms, "Bedrooms"),
          TextBox(controllerBathrooms, "Bathrooms"),
          TextBox(controllerPrice, "Price"),
          TextBox(controllerArea, "Area"),
          TextBox(controllerNotes, "Notes"),
          const SizedBox(height: 16),
          ElevatedButton(
            onPressed: () {
              String date = controllerDate.text;
              String type = controllerType.text;
              String address = widget.address.address; // Use the passed address
              String bedrooms = controllerBedrooms.text;
              String bathrooms = controllerBathrooms.text;
              String area = controllerArea.text;
              String price = controllerPrice.text;
              String notes = controllerNotes.text;

              String invalidMessage = "";

              if (date.isEmpty || DateTime.tryParse(date) == null) {
                invalidMessage += "\nEmpty or invalid date field";
              }
              if (type.isEmpty) {
                invalidMessage += "\nEmpty type field";
              }
              if (int.tryParse(bedrooms) == null) {
                invalidMessage += "\nEmpty or invalid bedrooms field";
              }
              if (int.tryParse(bathrooms) == null) {
                invalidMessage += "\nEmpty or invalid bathrooms field";
              }
              if (int.tryParse(area) == null) {
                invalidMessage += "\nEmpty or invalid area field";
              }
              if (double.tryParse(price) == null) {
                invalidMessage += "\nEmpty or invalid price field";
              }
              if (notes.isEmpty) {
                invalidMessage += "\nEmpty notes field";
              }

              if (invalidMessage.isEmpty) {
                Navigator.pop(
                  context,
                  Property(
                    type: type,
                    date: date,
                    address: address,
                    bedrooms: int.parse(bedrooms),
                    bathrooms: int.parse(bathrooms),
                    area: int.parse(area),
                    price: double.parse(price),
                    notes: notes,
                  ),
                );
              } else {
                showDialog(
                  context: context,
                  builder: (_) => AlertDialog(
                    title: const Text("Invalid property entry"),
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
                        child: const Text(
                          "Abort",
                          style: TextStyle(color: Colors.indigo),
                        ),
                      ),
                    ],
                  ),
                );
              }
            },
            child: const Text("Add property"),
          ),
        ],
      ),
    );
  }
}
