import 'package:flutter/material.dart';

import '../model/entry.dart';

class UpdatePage extends StatefulWidget {
  final Entry entry;

  UpdatePage(this.entry);

  @override
  _UpdatePageState createState() => _UpdatePageState();
}

class _UpdatePageState extends State<UpdatePage> {
  late TextEditingController typeController;
  late TextEditingController durationController;
  late TextEditingController distanceController;
  late TextEditingController caloriesController;
  late TextEditingController rateController;

  @override
  void initState() {
    super.initState();
    typeController = TextEditingController(text: widget.entry.type);
    durationController = TextEditingController(text: widget.entry.duration.toString());
    distanceController = TextEditingController(text: widget.entry.distance.toString());
    caloriesController = TextEditingController(text: widget.entry.calories.toString());
    rateController = TextEditingController(text: widget.entry.rate.toString());
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Update Entry")),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(controller: typeController, decoration: InputDecoration(labelText: 'Type')),
            TextField(controller: durationController, keyboardType: TextInputType.number, decoration: InputDecoration(labelText: 'Duration')),
            TextField(controller: distanceController, keyboardType: TextInputType.number, decoration: InputDecoration(labelText: 'Distance')),
            TextField(controller: caloriesController, keyboardType: TextInputType.number, decoration: InputDecoration(labelText: 'Calories')),
            TextField(controller: rateController, keyboardType: TextInputType.number, decoration: InputDecoration(labelText: 'Rate')),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                Entry updatedEntry = widget.entry.copy(
                  type: typeController.text,
                  duration: int.parse(durationController.text),
                  distance: int.parse(distanceController.text),
                  calories: int.parse(caloriesController.text),
                  rate: int.parse(rateController.text),
                );
                Navigator.pop(context, updatedEntry);
              },
              child: Text("Save Changes"),
            ),
          ],
        ),
      ),
    );
  }
}
