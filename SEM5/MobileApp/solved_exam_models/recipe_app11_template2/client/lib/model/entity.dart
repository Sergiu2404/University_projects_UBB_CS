// id, name - str, time, type, rating, notes - all str

import 'dart:convert';

const String mainTable = 'entries';
const String auxTable = 'types';

List<Entry> entriesFromJsonApi(String str) =>
    List<Entry>.from(json.decode(str).map((x) => Entry.fromJsonApi(x)));

String entryToJsonApi(List<Entry> entries) =>
    json.encode(List<dynamic>.from(entries.map((x) => x.toJsonApi())));

class EntryFields {
  static final List<String> values = [
    id,
    name,
    details,
    time,
    type,
    rating
  ];
  static const String id = '_id';
  static const String name = 'name';
  static const String details = 'details';
  static const String time = 'time';
  static const String type = 'type';
  static const String rating = 'rating';
  static const String notes = 'notes';
}

class Entry {
  int? id;
  String name;
  int time;
  String details;
  String type;
  int rating;

  Entry({
    this.id,
    required this.name,
    required this.time,
    required this.details,
    required this.type,
    required this.rating,
  });

  Entry copy({
    int? id,
    String? name,
    int? time,
    String? details,
    String? type,
    int? rating,
  }) =>
      Entry(
        id: id ?? this.id,
        name: name ?? this.name,
        details: details ?? this.details,
        time: time ?? this.time,
        type: type ?? this.type,
        rating: rating ?? this.rating
      );

  Map<String, Object?> toJson() => {
    EntryFields.id: id,
    EntryFields.name: name,
    EntryFields.details: details,
    EntryFields.time: time,
    EntryFields.type: type,
    EntryFields.rating: rating,
  };

  static Entry fromJson(Map<String, Object?> json) => Entry(
    id: json[EntryFields.id] as int?,
    name: json[EntryFields.name] as String,
    details: json[EntryFields.details] as String,
    time: json[EntryFields.time] as int,
    type: json[EntryFields.type] as String,
    rating: json[EntryFields.rating] as int,
  );

  Map<String, dynamic> toJsonApi() => {
    "id": id,
    "name": name,
    "details": details,
    "time": time,
    "type": type,
    "rating": rating,
  };

  factory Entry.fromJsonApi(Map<String, dynamic> json) => Entry(
    id: json['id'],
    name: json['name'],
    details: json['details'],
    time: json['time'],
    type: json['type'],
    rating: json['rating'],
  );
}