// id, date - str, medication, dosage, doctor, notes - all str

import 'dart:convert';

const String mainTable = 'properties';
const String auxTable = 'address';

List<Property> entriesFromJsonApi(String str) =>
    List<Property>.from(json.decode(str).map((x) => Property.fromJsonApi(x)));

String propertyToJsonApi(List<Property> entries) =>
    json.encode(List<dynamic>.from(entries.map((x) => x.toJsonApi())));

class PropertyFields {
  static final List<String> values = [
    id,
    date,
    type,
    address,
    bedrooms,
    bathrooms,
    price,
    area,
    notes
  ];
  static const String id = '_id';
  static const String date = 'date';
  static const String type = 'type';
  static const String address = 'address';
  static const String bedrooms = 'bedrooms';
  static const String bathrooms = 'bathrooms';
  static const String price = 'price';
  static const String area = 'area';
  static const String notes = 'notes';
}

class Address {
  int? id;
  String address;

  Address({
    this.id,
    required this.address,
  });

  Address copy({
    int? id,
    String? address,
  }) =>
      Address(
        id: id ?? this.id,
        address: address ?? this.address,
      );

  Map<String, Object?> toJson() => {
    PropertyFields.id: id,
    PropertyFields.address: address,
  };

  static Address fromJson(Map<String, Object?> json) => Address(
    id: json[PropertyFields.id] as int?,
    address: json[PropertyFields.address] as String,
  );

  Map<String, dynamic> toJsonApi() => {
    "id": id,
    "address": address,
  };

  factory Address.fromJsonApi(Map<String, dynamic> json) => Address(
    id: json['id'],
    address: json['address'],
  );
}

class Property {
  int? id;
  String date;
  String type;
  String address;
  int bedrooms;
  int bathrooms;
  double price;
  int area;
  String notes;

  Property({
    this.id,
    required this.date,
    required this.type,
    required this.address,
    required this.bedrooms,
    required this.bathrooms,
    required this.area,
    required this.price,
    required this.notes,
  });

  Property copy({
    int? id,
    String? date,
    String? type,
    String? address,
    int? area,
    int? bedrooms,
    int? bathrooms,
    double? price,
    String? notes,
  }) =>
      Property(
        id: id ?? this.id,
        date: date ?? this.date,
        type: type ?? this.type,
        address: address ?? this.address,
        bedrooms: bedrooms ?? this.bedrooms,
        bathrooms: bathrooms ?? this.bathrooms,
        area: area ?? this.area,
        price: price ?? this.price,
        notes: notes ?? this.notes,
      );

  Map<String, Object?> toJson() => {
    PropertyFields.id: id,
    PropertyFields.date: date,
    PropertyFields.type: type,
    PropertyFields.address: address,
    PropertyFields.bedrooms: bedrooms,
    PropertyFields.bathrooms: bathrooms,
    PropertyFields.area: area,
    PropertyFields.price: price,
    PropertyFields.notes: notes,
  };

  static Property fromJson(Map<String, Object?> json) => Property(
    id: json[PropertyFields.id] as int?,
    date: json[PropertyFields.date] as String,
    type: json[PropertyFields.type] as String,
    address: json[PropertyFields.address] as String,
    bedrooms: json[PropertyFields.bedrooms] as int,
    bathrooms: json[PropertyFields.bathrooms] as int,
    area: json[PropertyFields.area] as int,
    price: json[PropertyFields.price] as double,
    notes: json[PropertyFields.notes] as String,
  );

  Map<String, dynamic> toJsonApi() => {
    "id": id,
    "date": date,
    "type": type,
    "address": address,
    "bedrooms": bedrooms,
    "bathrooms": bathrooms,
    "price": price,
    "area": area,
    "notes": notes
  };

  factory Property.fromJsonApi(Map<dynamic, dynamic> json) => Property(
    id: json['id'],
    date: json['date'],
    type: json['type'],
    address: json['address'],
    bedrooms: json['bedrooms'],
    bathrooms: json['bathrooms'],
    price: json['price'].toDouble(),
    area: json['area'],
    notes: json['notes'],
  );
}