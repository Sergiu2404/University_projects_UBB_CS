import 'dart:convert';

import 'package:sqflite/sqflite.dart';
import '../model/entity.dart';

import 'dart:developer';

const String mainTable = 'entries';
const String auxTable = 'locations';

class DBRepository {
  static final DBRepository instance = DBRepository._init();
  static Database? _db;

  DBRepository._init();

  Future<Database> get database async {
    if (_db != null) return _db!;
    _db = await _initDB('files_catalog_app.db');
    return _db!;
  }

  Future<Database> _initDB(String path) async {
    return await openDatabase(path, version: 2, onCreate: _createDB);
  }

  // define schema with tables
  Future _createDB(Database db, int version) async {
    const idType = 'INTEGER PRIMARY KEY';
    const textType = 'TEXT NOT NULL';
    const boolType = 'BOOLEAN NOT NULL';
    const intType = 'INTEGER NOT NULL';

    await db.execute(''' 
    CREATE TABLE $mainTable (${EntryFields.id} $idType, 
                             ${EntryFields.name} $textType,
                             ${EntryFields.status} $textType,
                             ${EntryFields.size} $intType,
                             ${EntryFields.location} $textType,
                             ${EntryFields.usage} $intType,
                             )
      ''');

    await db.execute(''' 
      CREATE TABLE $auxTable ('id' $idType,
      'location' $textType
      )
      ''');
  }

  Future close() async {
    final db = await instance.database;
    db.close();
  }

  Future<String> addLocation(String location) async {
    log("DB: add location ${location}");
    Map<String, Object?> jsonLocation = {"location": location};
    final db = await instance.database;
    final id = await db.insert(auxTable, jsonLocation);
    return location;
  }

  void deleteAllLocations() async {
    log("DB: delete all locations");
    final db = await instance.database;
    db.rawDelete('delete from locations');
  }

  Future<List<String>> getLocations() async {
    log("DB: get locations");
    final db = await instance.database;
    final result = await db.query(auxTable);
    return result.map((json) => json['location'].toString()).toList();
  }


  Future<Entry> addEntry(Entry entry) async {
    try {
      final db = await instance.database;  // Ensure the database is open
      if (db == null) {
        throw Exception("Database is not initialized.");
      }

      print("Inserting entry: ${entry.toJson()}");
      final id = await db.insert(mainTable, entry.toJson());

      // Ensure the insert was successful
      print("Inserted entry with id: $id");

      return entry.copy(id: id);
    } catch (e) {
      print("Error inserting entry: $e");
      rethrow;
    }
  }


  Future<int> deleteEntry(int id) async {
    log("DB: delete entry with id $id");
    final db = await instance.database;

    return await db
        .delete(mainTable, where: '${EntryFields.id} = ?', whereArgs: [id]);
  }
  
  void deleteAllEntries() async {
    log("DB: delete all entries");
    final db = await instance.database;
    db.rawDelete('delete from entries');
  }

  Future<List<Entry>> getByLocation(String location) async {
    log("DB: get entries for location $location");
    final db = await instance.database;

    final maps = await db.query(mainTable,
        columns: EntryFields.values,
        where: '${EntryFields.location} = ?',
        whereArgs: [location]);

    if (maps.isNotEmpty) {
      log("DB: get entries for $location returned ${maps.length} results");
      return maps.map((e) => Entry.fromJson(e)).toList();
    } else {
      log("DB: get entries for $location failed");
      throw Exception('No entries for the given location');
    }
  }

  Future<List<Entry>> getAllEntries() async {
    log("DB: get entries");
    final db = await instance.database;
    final result = await db.query(mainTable);
    return result.map((json) => Entry.fromJson(json)).toList();
  }

  void deleteAllEntriesByLocation(String location) async {
    log("DB: delete all locaitons");
    final db = await instance.database;
    db.rawDelete('delete from entries where location = ?', [location]);
  }
}