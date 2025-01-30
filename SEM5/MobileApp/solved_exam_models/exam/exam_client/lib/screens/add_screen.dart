import 'package:flutter/material.dart';
import '../model/entity.dart';

class AddPage extends StatefulWidget {
  const AddPage({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _AddPageState();
}

class _AddPageState extends State<AddPage> {
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  final TextEditingController dateController = TextEditingController();
  final TextEditingController titleController = TextEditingController();
  final TextEditingController ingredientsController = TextEditingController();
  final TextEditingController categoryController = TextEditingController();
  final TextEditingController ratingController = TextEditingController();

  @override
  void dispose() {
    dateController.dispose();
    titleController.dispose();
    ingredientsController.dispose();
    categoryController.dispose();
    ratingController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Add a New Recipe")),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              _buildTextField(
                label: "Date",
                controller: dateController,
                validator: (value) =>
                value == null || value.isEmpty ? "Date cannot be empty" : null,
              ),
              const SizedBox(height: 16),
              _buildTextField(
                label: "Title",
                controller: titleController,
                validator: (value) =>
                value == null || value.isEmpty ? "title cannot be empty" : null,
              ),
              const SizedBox(height: 16),
              _buildTextField(
                label: "Ingredients",
                controller: ingredientsController,
                validator: (value) =>
                value == null || value.isEmpty ? "ingredients cannot be empty" : null,
              ),
              const SizedBox(height: 16),
              _buildTextField(
                label: "Category",
                controller: categoryController,
                validator: (value) =>
                value == null || value.isEmpty ? "category cannot be empty" : null,
              ),
              const SizedBox(height: 16),
              _buildTextField(
                label: "Rating",
                controller: ratingController,
                keyboardType: TextInputType.number,
                validator: (value) {
                  final rating = double.tryParse(value ?? "");
                  if (value == null || value.isEmpty) {
                    return "Rating cannot be empty";
                  } else if (rating == null || rating <= 0) {
                    return "Enter a valid positive number for the rating";
                  }
                  return null;
                },
              ),
              const SizedBox(height: 24),
              ElevatedButton(
                onPressed: () {
                  if (_formKey.currentState?.validate() == true) {
                    final newEntry = Entry(
                      id: 0,
                      date: dateController.text,
                      title: titleController.text,
                      ingredients: ingredientsController.text,
                      category: categoryController.text,
                      rating: double.parse(ratingController.text),
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
}
