import 'dart:convert';

const String fitnessTable = 'entries';
const String dateTable = 'dates';

List<Entry> entriesFromJsonApi(String str) =>
    List<Entry>.from(json.decode(str).map((x) => Entry.fromJsonApi(x)));

String entryToJsonApi(List<Entry> entries) =>
    json.encode(List<dynamic>.from(entries.map((x) => x.toJsonApi())));

class EntryFields {
  static final List<String> values = [
    id,
    date,
    type,
    duration,
    distance,
    calories,
    rate
  ];
  static const String id = '_id';
  static const String date = 'date';
  static const String type = 'type';
  static const String duration = 'duration';
  static const String distance = 'distance';
  static const String calories = 'calories';
  static const String rate = 'rate';
}

class Entry {
  int? id;
  String date;
  String type;
  int duration;
  int distance;
  int calories;
  int rate;

  Entry(
      {this.id,
        required this.date,
        required this.type,
        required this.duration,
        required this.distance,
        required this.calories,
        required this.rate,
      });

  Entry copy({
    int? id,
    String? date,
    String? type,
    int? duration,
    int? distance,
    int? calories,
    int? rate,
  }) =>
      Entry(
          id: id ?? this.id,
          date: date ?? this.date,
          type: type ?? this.type,
          duration: duration ?? this.duration,
          distance: distance ?? this.distance,
          calories: calories ?? this.calories,
          rate: rate ?? this.rate);

  Map<String, Object?> toJson() => {
    EntryFields.id: id,
    EntryFields.date: date,
    EntryFields.type: type,
    EntryFields.distance: distance,
    EntryFields.duration: duration,
    EntryFields.calories: calories,
    EntryFields.rate: rate
  };

  static Entry fromJson(Map<String, Object?> json) => Entry(
    id: json[EntryFields.id] as int?,
    date: json[EntryFields.date] as String,
    type: json[EntryFields.type] as String,
    distance: json[EntryFields.distance] as int,
    duration: json[EntryFields.duration] as int,
    calories: json[EntryFields.calories] as int,
    rate: json[EntryFields.rate] as int,
  );

  Map<String, dynamic> toJsonApi() => {
    "id": id,
    "date": date,
    "type": type,
    "duration": duration,
    "distance": distance,
    "calories": calories,
    "rate": rate,
  };

  factory Entry.fromJsonApi(Map<String, dynamic> json) => Entry(
      id: json['id'],
      date: json['date'],
      type: json['type'],
      duration: json['duration'],
      distance: json['distance'],
      calories: json['calories'],
      rate: json['rate']
  );
}