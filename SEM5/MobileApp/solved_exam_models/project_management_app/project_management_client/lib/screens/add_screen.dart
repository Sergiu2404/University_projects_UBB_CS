import 'package:flutter/material.dart';
import '../model/entity.dart';

class AddPage extends StatefulWidget {
  const AddPage({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _AddPageState();
}

class _AddPageState extends State<AddPage> {
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  // TextEditingControllers for each input field
  final TextEditingController nameController = TextEditingController();
  final TextEditingController teamController = TextEditingController();
  final TextEditingController detailsController = TextEditingController();
  final TextEditingController statusController = TextEditingController();
  final TextEditingController membersController = TextEditingController();
  final TextEditingController typeController = TextEditingController();

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
              // Name Field
              _buildTextField(
                label: "Name",
                controller: nameController,
                validator: (value) =>
                value == null || value.isEmpty ? "Name cannot be empty" : null,
              ),
              const SizedBox(height: 16),

              // Team Field
              _buildTextField(
                label: "Team",
                controller: teamController,
                validator: (value) =>
                value == null || value.isEmpty ? "Team cannot be empty" : null,
              ),
              const SizedBox(height: 16),

              // Details Field
              _buildTextField(
                label: "Details",
                controller: detailsController,
                validator: (value) =>
                value == null || value.isEmpty ? "Details cannot be empty" : null,
              ),
              const SizedBox(height: 16),

              // Status Field
              _buildTextField(
                label: "Status",
                controller: statusController,
                validator: (value) =>
                value == null || value.isEmpty ? "Status cannot be empty" : null,
              ),
              const SizedBox(height: 16),

              // Members Field
              _buildTextField(
                label: "Members",
                controller: membersController,
                keyboardType: TextInputType.number,
                validator: (value) {
                  final members = int.tryParse(value ?? "");
                  if (value == null || value.isEmpty) {
                    return "Members cannot be empty";
                  } else if (members == null || members <= 0) {
                    return "Enter a valid positive number for members";
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16),

              // Type Field
              _buildTextField(
                label: "Type",
                controller: typeController,
                validator: (value) =>
                value == null || value.isEmpty ? "Type cannot be empty" : null,
              ),
              const SizedBox(height: 24),

              // Submit Button
              ElevatedButton(
                onPressed: () {
                  if (_formKey.currentState?.validate() == true) {
                    // Create a new Entry object
                    final newEntry = Entry(
                      id: 0, // ID can be generated later or handled by the backend
                      name: nameController.text,
                      team: teamController.text,
                      details: detailsController.text,
                      status: statusController.text,
                      members: int.parse(membersController.text),
                      type: typeController.text,
                    );

                    // Return the new Entry object
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

  // Helper method to build text fields
  Widget _buildTextField({
    required String label,
    required TextEditingController controller,
    TextInputType keyboardType = TextInputType.text,
    String? Function(String?)? validator,
  }) {
    return TextFormField(
      controller: controller,
      decoration: InputDecoration(
        labelText: label,
        border: const OutlineInputBorder(),
      ),
      keyboardType: keyboardType,
      validator: validator,
    );
  }

  @override
  void dispose() {
    // Dispose all controllers to free resources
    nameController.dispose();
    teamController.dispose();
    detailsController.dispose();
    statusController.dispose();
    membersController.dispose();
    typeController.dispose();
    super.dispose();
  }
}
