// id, name - str, size, location, location, usage - all str

import 'dart:convert';

List<Entry> entriesFromJsonApi(String str) =>
    List<Entry>.from(json.decode(str).map((x) => Entry.fromJsonApi(x)));

String entryToJsonApi(List<Entry> entries) =>
    json.encode(List<dynamic>.from(entries.map((x) => x.toJsonApi())));

class EntryFields {
  static final List<String> values = [
    id,
    name,
    status,
    size,
    location,
    usage
  ];
  static const String id = 'id';
  static const String name = 'name';
  static const String status = 'status';
  static const String size = 'size';
  static const String location = 'location';
  static const String usage = 'usage';
}

class Entry {
  int? id;
  String name;
  int size;
  String status;
  String location;
  int usage;

  Entry({
    this.id,
    required this.name,
    required this.size,
    required this.status,
    required this.location,
    required this.usage,
  });

  Entry copy({
    int? id,
    String? name,
    int? size,
    String? status,
    String? location,
    int? usage,
  }) =>
      Entry(
        id: id ?? this.id,
        name: name ?? this.name,
        status: status ?? this.status,
        size: size ?? this.size,
        location: location ?? this.location,
        usage: usage ?? this.usage,
      );

  Map<String, Object?> toJson() => {
    EntryFields.id: id,
    EntryFields.name: name,
    EntryFields.status: status,
    EntryFields.size: size,
    EntryFields.location: location,
    EntryFields.usage: usage,
  };

  static Entry fromJson(Map<String, Object?> json) => Entry(
    id: json[EntryFields.id] as int?,
    name: json[EntryFields.name] as String,
    status: json[EntryFields.status] as String,
    size: json[EntryFields.size] as int,
    location: json[EntryFields.location] as String,
    usage: json[EntryFields.usage] as int,
  );

  Map<String, dynamic> toJsonApi() => {
    EntryFields.id: id,
    EntryFields.name: name,
    EntryFields.status: status,
    EntryFields.size: size,
    EntryFields.location: location,
    EntryFields.usage: usage
  };

  factory Entry.fromJsonApi(Map<String, dynamic> json) => Entry(
    id: json['id'],
    name: json['name'],
    status: json['status'],
    size: json['size'],
    location: json['location'],
    usage: json['usage'],
  );

  @override
  String toString(){
    return "name: " + name + ", status: " + status + ", size: " + size.toString() + ", location: " + location + ", usage: " + usage.toString() + "\n";
  }
}