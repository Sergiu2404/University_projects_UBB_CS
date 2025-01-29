import 'dart:convert';

class EntryFields {
  static final List<String> values = [
    id,
    name,
    team,
    details,
    status,
    members,
    type
  ];
  static const String id = '_id';
  static const String name = 'name';
  static const String team = 'team';
  static const String details = 'details';
  static const String status = 'status';
  static const String members = 'members';
  static const String type = 'type';
}

class Entry {
  int? id;
  String name;
  String team;
  String details;
  String status;
  int members;
  String type;

  Entry({
    this.id,
    required this.name,
    required this.team,
    required this.details,
    required this.status,
    required this.members,
    required this.type
  });

  Entry copy({
    int? id,
    String? name,
    String? team,
    String? details,
    String? status,
    int? members,
    String? type
  }) =>
      Entry(
        id: id ?? this.id,
        name: name ?? this.name,
        team: team ?? this.team,
        details: details ?? this.details,
        status: status ?? this.status,
        members: members ?? this.members,
          type: type ?? this.type
      );

  Map<String, Object?> toJson() => {
    EntryFields.id: id,
    EntryFields.name: name,
    EntryFields.team: team,
    EntryFields.details: details,
    EntryFields.status: status,
    EntryFields.members: members,
    EntryFields.type: type
  };

  static Entry fromJson(Map<String, Object?> json) => Entry(
    id: json[EntryFields.id] as int?,
    name: json[EntryFields.name] as String,
    team: json[EntryFields.team] as String,
    details: json[EntryFields.details] as String,
    status: json[EntryFields.status] as String,
    members: json[EntryFields.members] as int,
      type: json[EntryFields.type] as String
  );

  Map<String, dynamic> toJsonApi() => {
    "id": id,
    "name": name,
    "team": team,
    "details": details,
    "status": status,
    "members": members,
    "type": type
  };

  factory Entry.fromJsonApi(Map<String, dynamic> json) => Entry(
    id: json['id'],
    name: json['name'],
    team: json['team'],
    details: json['details'],
    status: json['status'],
    members: json['members'],
      type: json['type']
  );

  List<Entry> entriesFromJsonApi(String str) =>
      List<Entry>.from(json.decode(str).map((x) => Entry.fromJsonApi(x)));

  String entryToJsonApi(List<Entry> entries) =>
      json.encode(List<dynamic>.from(entries.map((x) => x.toJsonApi())));


  String toString(){
    return "name: " + name + ", team: " + team + ", details: " + details.toString() + ", status: " + status + ", members: " + members.toString() + "\n";
  }
}