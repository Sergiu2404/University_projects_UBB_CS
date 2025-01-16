// id, date - str, medication, dosage, doctor, notes - all str

import 'dart:convert';

const String mainTable = 'entries';
const String auxTable = 'days';

List<Entry> entriesFromJsonApi(String str) =>
    List<Entry>.from(json.decode(str).map((x) => Entry.fromJsonApi(x)));

String entryToJsonApi(List<Entry> entries) =>
    json.encode(List<dynamic>.from(entries.map((x) => x.toJsonApi())));

class EntryFields {
  static final List<String> values = [
    id,
    date,
    symptom,
    medication,
    dosage,
    doctor,
    notes
  ];
  static const String id = '_id';
  static const String date = 'date';
  static const String symptom = 'symptom';
  static const String medication = 'medication';
  static const String dosage = 'dosage';
  static const String doctor = 'doctor';
  static const String notes = 'notes';
}

class Entry {
  int? id;
  String date;
  String medication;
  String symptom;
  String dosage;
  String doctor;
  String notes;

  Entry({
    this.id,
    required this.date,
    required this.medication,
    required this.symptom,
    required this.dosage,
    required this.doctor,
    required this.notes,
  });

  Entry copy({
    int? id,
    String? date,
    String? medication,
    String? symptom,
    String? dosage,
    String? doctor,
    String? notes,
  }) =>
      Entry(
        id: id ?? this.id,
        date: date ?? this.date,
        symptom: symptom ?? this.symptom,
        medication: medication ?? this.medication,
        dosage: dosage ?? this.dosage,
        doctor: doctor ?? this.doctor,
        notes: notes ?? this.notes,
      );

  Map<String, Object?> toJson() => {
    EntryFields.id: id,
    EntryFields.date: date,
    EntryFields.symptom: symptom,
    EntryFields.medication: medication,
    EntryFields.dosage: dosage,
    EntryFields.doctor: doctor,
    EntryFields.notes: notes,
  };

  static Entry fromJson(Map<String, Object?> json) => Entry(
    id: json[EntryFields.id] as int?,
    date: json[EntryFields.date] as String,
    symptom: json[EntryFields.symptom] as String,
    medication: json[EntryFields.medication] as String,
    dosage: json[EntryFields.dosage] as String,
    doctor: json[EntryFields.doctor] as String,
    notes: json[EntryFields.notes] as String,
  );

  Map<String, dynamic> toJsonApi() => {
    "id": id,
    "date": date,
    "symptom": symptom,
    "medication": medication,
    "dosage": dosage,
    "doctor": doctor,
    "notes": notes
  };

  factory Entry.fromJsonApi(Map<String, dynamic> json) => Entry(
    id: json['id'],
    date: json['date'],
    symptom: json['symptom'],
    medication: json['medication'],
    dosage: json['dosage'],
    doctor: json['doctor'],
    notes: json['notes'],
  );
}