import 'dart:convert';

const String tipsTable = 'tips';

List<Tip> tipsFromJsonApi(String str) =>
    List<Tip>.from(json.decode(str).map((x) => Tip.fromJsonApi(x)));

String tipToJsonApi(List<Tip> tips) =>
    json.encode(List<dynamic>.from(tips.map((x) => x.toJsonApi())));

class TipFields {
  static final List<String> values = [
    id,
    name,
    description,
    materials,
    steps,
    category,
    difficulty,
    isLocal
  ];
  static const String id = '_id';
  static const String name = 'name';
  static const String description = 'description';
  static const String materials = 'materials';
  static const String steps = 'steps';
  static const String category = 'category';
  static const String difficulty = 'difficulty';
  static const String isLocal = 'local';
}

class Tip {
  int? id;
  String name;
  String description;
  String materials;
  String steps;
  String category;
  String difficulty;
  bool isLocal;

  Tip(
      {this.id,
        required this.name,
        required this.description,
        required this.materials,
        required this.steps,
        required this.category,
        required this.difficulty,
        required this.isLocal});

  Tip copy({
    int? id,
    String? name,
    String? description,
    String? materials,
    String? steps,
    String? category,
    String? difficulty,
    bool? isLocal,
  }) =>
      Tip(
          id: id ?? this.id,
          name: name ?? this.name,
          description: description ?? this.description,
          materials: materials ?? this.materials,
          steps: steps ?? this.steps,
          category: category ?? this.category,
          difficulty: difficulty ?? this.difficulty,
          isLocal: isLocal ?? this.isLocal);

  Map<String, Object?> toJson() => {
    TipFields.id: id,
    TipFields.name: name,
    TipFields.description: description,
    TipFields.materials: materials,
    TipFields.steps: steps,
    TipFields.category: category,
    TipFields.difficulty: difficulty,
    TipFields.isLocal: isLocal ? 1 : 0
  };

  static Tip fromJson(Map<String, Object?> json) => Tip(
      id: json[TipFields.id] as int?,
      name: json[TipFields.name] as String,
      description: json[TipFields.description] as String,
      materials: json[TipFields.materials] as String,
      steps: json[TipFields.steps] as String,
      category: json[TipFields.category] as String,
      difficulty: json[TipFields.difficulty] as String,
      isLocal: json[TipFields.isLocal] == 1);

  Map<String, dynamic> toJsonApi() => {
    "id": id,
    "name": name,
    "description": description,
    "materials": materials,
    "steps": steps,
    "category": category,
    "difficulty": difficulty,
  };

  factory Tip.fromJsonApi(Map<String, dynamic> json) => Tip(
      id: json['id'],
      name: json['name'],
      description: json['description'],
      materials: json['materials'],
      steps: json['steps'],
      category: json['category'],
      difficulty: json['difficulty'],
      isLocal: false
  );
}