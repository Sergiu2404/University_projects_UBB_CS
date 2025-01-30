import 'dart:convert';

class EntryFields {
  static final List<String> values = [
    id,
    date,
    title,
    ingredients,
    category,
    rating,
  ];
  static const String id = '_id';
  static const String date = 'date';
  static const String title = 'title';
  static const String ingredients = 'ingredients';
  static const String category = 'category';
  static const String rating = 'rating';
}

class Entry {
  int? id;
  String date;
  String title;
  String ingredients;
  String category;
  double rating;

  Entry({
    this.id,
    required this.date,
    required this.title,
    required this.ingredients,
    required this.category,
    required this.rating,
  });

  Entry copy({
    int? id,
    String? date,
    String? title,
    String? ingredients,
    String? category,
    double? rating,
  }) =>
      Entry(
        id: id ?? this.id,
        date: date ?? this.date,
        title: title ?? this.title,
        ingredients: ingredients ?? this.ingredients,
        category: category ?? this.category,
        rating: rating ?? this.rating,
      );

  Map<String, Object?> toJson() => {
    EntryFields.id: id,
    EntryFields.date: date,
    EntryFields.title: title,
    EntryFields.ingredients: ingredients,
    EntryFields.category: category,
    EntryFields.rating: rating,
  };

  static Entry fromJson(Map<String, Object?> json) => Entry(
    id: json[EntryFields.id] as int?,
    date: json[EntryFields.date] as String,
    title: json[EntryFields.title] as String,
    ingredients: json[EntryFields.ingredients] as String,
    category: json[EntryFields.category] as String,
    rating: json[EntryFields.rating] as double,
  );

  Map<String, dynamic> toJsonApi() => {
    "id": id,
    "date": date,
    "title": title,
    "ingredients": ingredients,
    "category": category,
    "rating": rating,
  };

  factory Entry.fromJsonApi(Map<String, dynamic> json) => Entry(
    id: json['id'],
    date: json['date'],
    title: json['title'],
    ingredients: json['ingredients'],
    category: json['category'],
    rating: json['rating'].toDouble(),
  );

  List<Entry> entriesFromJsonApi(String str) =>
      List<Entry>.from(json.decode(str).map((x) => Entry.fromJsonApi(x)));

  String entryToJsonApi(List<Entry> entries) =>
      json.encode(List<dynamic>.from(entries.map((x) => x.toJsonApi())));


  String toString(){
    return "date: " + date + ", title: " + title + ", ingredients: " + ingredients.toString() + ", category: " + category + ", rating: " + rating.toString() + "\n";
  }
}