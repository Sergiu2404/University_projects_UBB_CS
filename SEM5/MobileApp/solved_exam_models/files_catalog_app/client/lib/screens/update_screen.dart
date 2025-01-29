// import 'package:flutter/material.dart';
// import '../model/entity.dart';
//
// class UpdateEntityEntryScreen extends StatefulWidget {
//   final String date; // The date passed to this screen (needed for the entry)
//   final Entry existingEntry; // The existing entry to be updated
//
//   const UpdateEntityEntryScreen({super.key, required this.date, required this.existingEntry});
//
//   @override
//   _UpdateEntityEntryScreenState createState() => _UpdateEntityEntryScreenState();
// }
//
// class _UpdateEntityEntryScreenState extends State<UpdateEntityEntryScreen> {
//   late TextEditingController controllerName;
//   late TextEditingController controllerCategory;
//   late TextEditingController controllerNumberInt;
//   late TextEditingController controllerNumberFloat;
//   late TextEditingController controllerNotes;
//
//   @override
//   void initState() {
//     super.initState();
//     // Initialize the controllers with the existing values
//     controllerName = TextEditingController(text: widget.existingEntry.name);
//     controllerCategory = TextEditingController(text: widget.existingEntry.category);
//     controllerNumberInt = TextEditingController(text: widget.existingEntry.number_int.toString());
//     controllerNumberFloat = TextEditingController(text: widget.existingEntry.number_float.toString());
//     controllerNotes = TextEditingController(text: widget.existingEntry.notes);
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       appBar: AppBar(title: Text("Update health entry")),
//       body: ListView(
//         padding: EdgeInsets.all(16.0),
//         children: [
//           TextBox(controllerName, "Name"),
//           TextBox(controllerCategory, "Category"),
//           TextBox(controllerNumberInt, "Number Integer", keyboardType: TextInputType.number),
//           TextBox(controllerNumberFloat, "Number Float", keyboardType: TextInputType.number),
//           TextBox(controllerNotes, "Notes"),
//           ElevatedButton(
//             onPressed: () {
//               String name = controllerName.text;
//               String category = controllerCategory.text;
//               String numberIntStr = controllerNumberInt.text;
//               String numberFloatStr = controllerNumberFloat.text;
//               String notes = controllerNotes.text;
//
//               String invalidMessage = "";
//
//               if (name.isEmpty) {
//                 invalidMessage += "\nEmpty name field";
//               }
//               if (category.isEmpty) {
//                 invalidMessage += "\nEmpty category field";
//               }
//               if (numberIntStr.isEmpty) {
//                 invalidMessage += "\nEmpty number integer field";
//               }
//               if (numberFloatStr.isEmpty) {
//                 invalidMessage += "\nEmpty number float field";
//               }
//               if (notes.isEmpty) {
//                 invalidMessage += "\nEmpty notes field";
//               }
//
//               // Check if all fields are valid
//               if (invalidMessage == "") {
//                 try {
//                   int numberInt = int.parse(numberIntStr);
//                   double numberFloat = double.parse(numberFloatStr);
//
//                   // Create the updated Entry and pass it back to the previous screen
//                   Entry updatedEntry = widget.existingEntry.copy(
//                     name: name,
//                     category: category,
//                     number_int: numberInt,
//                     number_float: numberFloat,
//                     notes: notes,
//                   );
//
//                   Navigator.pop(context, updatedEntry); // Return the updated entry
//                 } catch (e) {
//                   invalidMessage += "\nInvalid number format for integer or float.";
//                 }
//               }
//
//               // If any validation fails, show a dialog with the error message
//               if (invalidMessage.isNotEmpty) {
//                 showDialog(
//                   context: context,
//                   builder: (_) => AlertDialog(
//                     title: Text("Invalid health entry"),
//                     content: Text(invalidMessage),
//                     actions: [
//                       TextButton(
//                         onPressed: () {
//                           Navigator.pop(context);
//                         },
//                         child: const Text(
//                           "Try again",
//                           style: TextStyle(color: Colors.purple),
//                         ),
//                       ),
//                       TextButton(
//                         onPressed: () {
//                           Navigator.pop(context);
//                           Navigator.pop(context); // Pop the current screen
//                         },
//                         child: const Text(
//                           "Abort",
//                           style: TextStyle(color: Colors.indigo),
//                         ),
//                       ),
//                     ],
//                   ),
//                 );
//               }
//             },
//             child: Text("Update health entry"),
//           ),
//         ],
//       ),
//     );
//   }
// }
//
// class TextBox extends StatelessWidget {
//   final TextEditingController controller;
//   final String label;
//   final TextInputType keyboardType;
//
//   const TextBox(this.controller, this.label, {super.key, this.keyboardType = TextInputType.text});
//
//   @override
//   Widget build(BuildContext context) {
//     return Padding(
//       padding: const EdgeInsets.symmetric(vertical: 8.0),
//       child: TextField(
//         controller: controller,
//         decoration: InputDecoration(
//           labelText: label,
//           border: OutlineInputBorder(),
//         ),
//         keyboardType: keyboardType,
//       ),
//     );
//   }
// }
