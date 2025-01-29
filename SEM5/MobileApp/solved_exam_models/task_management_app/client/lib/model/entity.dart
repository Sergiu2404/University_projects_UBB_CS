import 'dart:convert';

class EntryFields {
  static final List<String> values = [
    id,
    date,
    type,
    duration,
    priority,
    category,
    description
  ];
  static const String id = '_id';
  static const String date = 'date';
  static const String type = 'type';
  static const String duration = 'duration';
  static const String priority = 'priority';
  static const String category = 'category';
  static const String description = 'description';
}

class Entry {
  int? id;
  String date;
  String type;
  double duration;
  String priority;
  String category;
  String description;

  Entry({
    this.id,
    required this.date,
    required this.type,
    required this.duration,
    required this.priority,
    required this.category,
    required this.description
  });

  Entry copy({
    int? id,
    String? date,
    String? type,
    double? duration,
    String? priority,
    String? category,
    String? description
  }) =>
      Entry(
        id: id ?? this.id,
        date: date ?? this.date,
        type: type ?? this.type,
        duration: duration ?? this.duration,
        priority: priority ?? this.priority,
        category: category ?? this.category,
          description: description ?? this.description
      );

  Map<String, Object?> toJson() => {
    EntryFields.id: id,
    EntryFields.date: date,
    EntryFields.type: type,
    EntryFields.duration: duration,
    EntryFields.priority: priority,
    EntryFields.category: category,
    EntryFields.description: description
  };

  static Entry fromJson(Map<String, Object?> json) => Entry(
    id: json[EntryFields.id] as int?,
    date: json[EntryFields.date] as String,
    type: json[EntryFields.type] as String,
    duration: json[EntryFields.duration] as double,
    priority: json[EntryFields.priority] as String,
    category: json[EntryFields.category] as String,
      description: json[EntryFields.description] as String
  );

  Map<String, dynamic> toJsonApi() => {
    "id": id,
    "date": date,
    "type": type,
    "duration": duration,
    "priority": priority,
    "category": category,
    "description": description
  };

  factory Entry.fromJsonApi(Map<String, dynamic> json) => Entry(
    id: json['id'],
    date: json['date'],
    type: json['type'],
    duration: json['duration'].toDouble(),
    priority: json['priority'],
    category: json['category'],
      description: json['description']
  );

  List<Entry> entriesFromJsonApi(String str) =>
      List<Entry>.from(json.decode(str).map((x) => Entry.fromJsonApi(x)));

  String entryToJsonApi(List<Entry> entries) =>
      json.encode(List<dynamic>.from(entries.map((x) => x.toJsonApi())));


  String toString(){
    return "date: " + date + ", type: " + type + ", duration: " + duration.toString() + ", priority: " + priority + ", category: " + category.toString() + "\n";
  }
}