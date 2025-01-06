import 'dart:convert';

const String recipeTable = 'recipes';
const String categoryTable = 'categories';

List<Recipe> RecipesFromJsonApi(String str) =>
    List<Recipe>.from(json.decode(str).map((x) => Recipe.fromJsonApi(x)));

String RecipeToJsonApi(List<Recipe> Recipes) =>
    json.encode(List<dynamic>.from(Recipes.map((x) => x.toJsonApi())));


class RecipeFields {
  static final List<String> values = [
    id,
    name,
    description,
    ingredients,
    instructions,
    category,
    difficulty
  ];
  static const String id = '_id';
  static const String name = 'name';
  static const String description = 'description';
  static const String ingredients = 'ingredients';
  static const String instructions = 'instructions';
  static const String category = 'category';
  static const String difficulty = 'difficulty';
}

class Recipe {
  int? id;
  String name;
  String description;
  String ingredients;
  String instructions;
  String category;
  String difficulty;


  Recipe(
      {this.id,
        required this.name,
        required this.description,
        required this.ingredients,
        required this.instructions,
        required this.category,
        required this.difficulty,
      });

  Recipe copy({
    int? id,
    String? name,
    String? description,
    String? ingredients,
    String? instructions,
    String? category,
    String? difficulty,
  }) =>
      Recipe(
        id: id ?? this.id,
        name: name ?? this.name,
        description: description ?? this.description,
        ingredients: ingredients ?? this.ingredients,
        instructions: instructions ?? this.instructions,
        category: category ?? this.category,
        difficulty: difficulty ?? this.difficulty,
      );

  Map<String, Object?> toJson() => {
    RecipeFields.id: id,
    RecipeFields.name: name,
    RecipeFields.description: description,
    RecipeFields.instructions: instructions,
    RecipeFields.difficulty: difficulty,
    RecipeFields.ingredients: ingredients,
    RecipeFields.category: category,

  };

  static Recipe fromJson(Map<String, Object?> json) => Recipe(
    id: json[RecipeFields.id] as int?,
    name: json[RecipeFields.name] as String,
    description: json[RecipeFields.description] as String,
    ingredients: json[RecipeFields.ingredients] as String,
    instructions: json[RecipeFields.instructions] as String,
    category: json[RecipeFields.category] as String,
    difficulty: json[RecipeFields.difficulty] as String,
  );

  Map<String, dynamic> toJsonApi() => {
    "id": id,
    "name": name,
    "description": description,
    "ingredients": ingredients,
    "instructions": instructions,
    "category": category,
    "difficulty": difficulty,
  };

  factory Recipe.fromJsonApi(Map<String, dynamic> json) => Recipe(
    id: json['id'],
    name: json['name'],
    description: json['description'],
    ingredients: json['ingredients'],
    instructions: json['instructions'],
    category: json['category'],
    difficulty: json['difficulty'],
  );
}