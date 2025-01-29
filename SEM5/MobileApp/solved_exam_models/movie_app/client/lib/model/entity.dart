// id, name - str, medication, dosage, genre, director - all str

import 'dart:convert';

List<Entry> entriesFromJsonApi(String str) =>
    List<Entry>.from(json.decode(str).map((x) => Entry.fromJsonApi(x)));

String entryToJsonApi(List<Entry> entries) =>
    json.encode(List<dynamic>.from(entries.map((x) => x.toJsonApi())));

class EntryFields {
  static final List<String> values = [
    id,
    name,
    description,
    genre,
    director,
    year
  ];
  static const String id = '_id';
  static const String name = 'name';
  static const String description = 'description';
  static const String genre = 'genre';
  static const String director = 'director';
  static const String year = 'year';
}

class Entry {
  int? id;
  String name;
  String description;
  String genre;
  String director;
  int year;

  Entry({
    this.id,
    required this.name,
    required this.description,
    required this.genre,
    required this.director,
    required this.year
  });

  Entry copy({
    int? id,
    String? name,
    String? description,
    String? genre,
    String? director,
    int? year
  }) =>
      Entry(
        id: id ?? this.id,
        name: name ?? this.name,
        description: description ?? this.description,
        genre: genre ?? this.genre,
        director: director ?? this.director,
        year: year ?? this.year
      );

  Map<String, Object?> toJson() => {
    EntryFields.id: id,
    EntryFields.name: name,
    EntryFields.description: description,
    EntryFields.genre: genre,
    EntryFields.director: director,
    EntryFields.year: year
  };

  static Entry fromJson(Map<String, Object?> json) => Entry(
    id: json[EntryFields.id] as int?,
    name: json[EntryFields.name] as String,
    description: json[EntryFields.description] as String,
    genre: json[EntryFields.genre] as String,
    director: json[EntryFields.director] as String,
    year: json[EntryFields.year] as int
  );

  Map<String, dynamic> toJsonApi() => {
    "id": id,
    "name": name,
    "description": description,
    "genre": genre,
    "director": director,
    "year": year
  };

  factory Entry.fromJsonApi(Map<String, dynamic> json) => Entry(
    id: json['id'],
    name: json['name'],
    description: json['description'],
    genre: json['genre'],
    director: json['director'],
    year: json['year']
  );

  String toString(){
    return "name: " + name + ", description: " + description + ", genre: " + genre + ", director: " + director + ", year: " + year.toString() + "\n";
  }
}