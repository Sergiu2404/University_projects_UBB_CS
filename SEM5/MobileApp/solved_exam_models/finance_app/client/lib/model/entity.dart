import 'dart:convert';

List<Entry> entriesFromJsonApi(String str) =>
    List<Entry>.from(json.decode(str).map((x) => Entry.fromJsonApi(x)));

String entryToJsonApi(List<Entry> entries) =>
    json.encode(List<dynamic>.from(entries.map((x) => x.toJsonApi())));

class Entry {
  int? id;
  String date;
  String type;
  int amount;
  String category;
  String description;

  Entry({
    this.id,
    required this.date,
    required this.type,
    required this.amount,
    required this.category,
    required this.description
  });

  Entry copy({
    int? id,
    String? date,
    String? type,
    int? amount,
    String? category,
    String? description,
  }) =>
      Entry(
        id: id ?? this.id,
        date: date ?? this.date,
        type: type ?? this.type,
        amount: amount ?? this.amount,
        category: category ?? this.category,
        description: description ?? this.description
      );

  Map<String, Object?> toJson() => {
    '_id': id,
    'date': date,
    'type': type,
    'amount': amount,
    'category': category,
    'description': description
  };

  static Entry fromJson(Map<String, Object?> json) => Entry(
    id: json['_id'] as int?,
    date: json['date'] as String,
    type: json['type'] as String,
    amount: json['amount'] as int,
    category: json['category'] as String,
    description: json['description'] as String
  );

  Map<String, dynamic> toJsonApi() => {
    "id": id,
    "date": date,
    "type": type,
    "amount": amount,
    "category": category,
    "description": description
  };

  factory Entry.fromJsonApi(Map<String, dynamic> json) => Entry(
    id: json['id'],
    date: json['date'],
    type: json['type'],
    amount: json['amount'],
    category: json['category'],
    description: json['description']
  );
}