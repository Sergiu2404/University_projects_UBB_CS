import 'dart:convert';

import 'package:sqflite/sqflite.dart';
import '../model/entry.dart';

import 'dart:developer';

class DB_Repo {
  static final DB_Repo instance = DB_Repo._init();
  static Database? _db;

  DB_Repo._init();

  Future<Database> get database async {
    if (_db != null) return _db!;
    _db = await _initDB('fitness.db');
    return _db!;
  }

  Future<Database> _initDB(String path) async {
    return await openDatabase(path, version: 1, onCreate: _createDB);
  }

  Future _createDB(Database db, int version) async {
    const idType = 'INTEGER PRIMARY KEY';
    const textType = 'TEXT NOT NULL';
    const boolType = 'BOOLEAN NOT NULL';
    const intType = 'INTEGER NOT NULL';

    await db.execute(''' 
    CREATE TABLE $fitnessTable (${EntryFields.id} $idType, 
                             ${EntryFields.date} $textType,
                             ${EntryFields.type} $textType,
                             ${EntryFields.duration} $intType,
                             ${EntryFields.distance} $intType,
                             ${EntryFields.calories} $intType,
                             ${EntryFields.rate} $intType
                             )
      ''');

    await db.execute(''' 
      CREATE TABLE $dateTable ('_id' $idType,
      'date' $textType
      )
      ''');

    // New reserved entries table
    await db.execute('''
    CREATE TABLE reserved_entries (
      id $idType,
      entry_data $textType
    )
  ''');
  }

  Future close() async {
    final db = await instance.database;
    db.close();
  }

  Future<String> addDate(String date) async {
    log("DB: add date ${date}");
    Map<String, Object?> jsonDate = {"date": date};
    final db = await instance.database;
    final id = await db.insert(dateTable, jsonDate);
    return date;
  }

  void deleteAllDates() async {
    // log("DB: delete all dates");
    final db = await instance.database;
    db.rawDelete('delete from dates');
  }

  Future<List<String>> getDates() async {
    log("DB: get dates");
    final db = await instance.database;
    final result = await db.query(dateTable);
    return result.map((json) => json['date'].toString()).toList();
  }

  // entries

  Future<Entry> addEntry(Entry entry) async {
    final db = await instance.database;
    // final id = await db.insert(fitnessTable, entry.toJson());
    // return entry.copy(id: id);
    try {
      final id = await db.insert(fitnessTable, entry.toJson(),
          conflictAlgorithm: ConflictAlgorithm.replace);
      return entry.copy(id: id);
    } catch (e) {
      print('Error adding entry: $e');
      return entry;
    }
  }

  Future<int> deleteEntry(int id) async {
    log("DB: delete entry with id $id");
    final db = await instance.database;

    return await db
        .delete(fitnessTable, where: '${EntryFields.id} = ?', whereArgs: [id]);
  }

  Future<void> deleteAllEntries() async {
    final db = await instance.database;
    await db.rawDelete('DELETE FROM $fitnessTable');
  }

  Future<int> updateEntry(Entry entry) async {
    final db = await instance.database;
    return await db.update(
      fitnessTable,
      entry.toJson(),
      where: '${EntryFields.id} = ?',
      whereArgs: [entry.id],
    );
  }

  Future<List<Entry>> getBydate(String date) async {
    log("DB: get entries for date $date");
    final db = await instance.database;

    final maps = await db.query(fitnessTable,
        columns: EntryFields.values,
        where: '${EntryFields.date} = ?',
        whereArgs: [date]);

    if (maps.isNotEmpty) {
      log("DB: get entries for date $date returned ${maps.length} results");
      return maps.map((e) => Entry.fromJson(e)).toList();
    } else {
      log("DB: get entries for date $date failed");
      throw Exception('No entries for the given date');
    }
  }

  void deleteAllEntriesBydate(String date) async {
    // log("DB: delete all dates");
    final db = await instance.database;
    db.rawDelete('delete from entries where date = ?', [date]);
  }

  Future<List<Entry>> getAllEntries() async {
    final db = await instance.database;
    final maps = await db.query(fitnessTable);
    return maps.map((e) => Entry.fromJson(e)).toList();
  }

// Add method to store reserved entries
  Future<void> addReservedEntry(Map<String, dynamic> reservedEntry) async {
    final db = await instance.database;
    await db.insert('reserved_entries', reservedEntry);
  }

// Get reserved entries
  Future<List<Map<String, dynamic>>> getReservedEntries() async {
    final db = await instance.database;
    return await db.query('reserved_entries');
  }

}

