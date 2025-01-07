import 'dart:convert';

const String categoryTable = 'categories';

// List<Category> categoriesFromJsonApi(String str) =>
//     List<Category>.from(json.decode(str).map((x) => Category.fromJsonApi(x)));

String categoryToJsonApi(List<Category> categories) =>
    json.encode(List<dynamic>.from(categories.map((x) => x.toJsonApi())));

class CategoryFields {
  static final List<String> values = [id, category, saved];
  static const String id = '_id';
  static const String category = 'category';
  static const String saved = 'saved';
}

class Category {
  int? id;
  String category;
  bool saved;

  Category({
    this.id,
    required this.category,
    required this.saved,
  });

  Category copy({
    int? id,
    String? category,
    bool? saved,
  }) =>
      Category(
          id: id ?? this.id,
          category: category ?? this.category,
          saved: saved ?? this.saved
      );

  Map<String, Object?> toJson() => {
    CategoryFields.id: id,
    CategoryFields.category: category,
    CategoryFields.saved: saved?1:0
  };

  static Category fromJson(Map<String, Object?> json) => Category(
    category: json[CategoryFields.category] as String,
    saved: json[CategoryFields.saved] == 1,
  );

  Map<String, dynamic> toJsonApi() => {
    "category": category,
  };

// factory Category.fromJsonApi(Map<String, dynamic> json) => Category(
//       category: json[0],
//     );
}