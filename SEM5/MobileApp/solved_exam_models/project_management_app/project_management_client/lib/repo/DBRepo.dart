import 'dart:convert';

import 'package:sqflite/sqflite.dart';
import '../model/entity.dart';

import 'dart:developer';


const String mainTable = 'entries';
// const String auxTable = 'dates';

class DBRepo {
  static final DBRepo instance = DBRepo._init();
  static Database? _db;

  DBRepo._init();

  Future<Database> get database async {
    if (_db != null) return _db!;
    _db = await _initDB('project_management.db');
    return _db!;
  }

  Future<Database> _initDB(String path) async {
    return await openDatabase(path, version: 2, onCreate: _createDB);
  }

  Future _createDB(Database db, int version) async {
    const idType = 'INTEGER PRIMARY KEY';
    const textType = 'TEXT NOT NULL';
    const boolType = 'BOOLEAN NOT NULL';
    const intType = 'INTEGER NOT NULL';
    const realType = 'REAL NOT NULL';

    await db.execute(''' 
    CREATE TABLE $mainTable (${EntryFields.id} $idType, 
                             ${EntryFields.name} $textType,
                             ${EntryFields.team} $textType,
                             ${EntryFields.details} $textType,
                             ${EntryFields.status} $textType,
                             ${EntryFields.members} $intType,
                             ${EntryFields.type} $textType
                             )
      ''');

    // await db.execute('''
    //   CREATE TABLE $auxTable ('_id' $idType,
    //   'date' $textType
    //   )
    //   ''');
  }

  Future close() async {
    final db = await instance.database;
    db.close();
  }

  // Future<String> addDate(String date) async {
  //   log("DB: add day ${date}");
  //   Map<String, Object?> jsonGenre = {"date": date};
  //   final db = await instance.database;
  //   final id = await db.insert(auxTable, jsonGenre);
  //   return date;
  // }
  //
  // void deleteAllDates() async {
  //   log("DB: delete all date");
  //   final db = await instance.database;
  //   db.rawDelete('delete from $auxTable');
  // }
  //
  // Future<List<String>> getDates() async {
  //   log("DB: get dates");
  //   final db = await instance.database;
  //   final result = await db.query(auxTable);
  //   return result.map((json) => json['date'].toString()).toList();
  // }

  Future<List<Entry>> getAllEntries() async {
    final db = await instance.database;
    final results = await db.query(mainTable);

    return results.map((json) => Entry.fromJson(json)).toList();
  }

  Future<List<Entry>> getInProgressEntries() async {
    final db = await instance.database;
    final results = await db.query(mainTable);

    return results
        .map((json) => Entry.fromJson(json)).toList();
  }

  Future<Entry> addEntry(Entry entry) async {
    final db = await instance.database;
    final id = await db.insert(mainTable, entry.toJson());
    return entry.copy(id: id);
  }

  void deleteAllEntries() async {
    // log("DB: delete all dates");
    final db = await instance.database;
    db.rawDelete('delete from $mainTable');
  }

  Future<int> deleteEntry(int id) async {
    log("DB: delete entry with id $id");
    final db = await instance.database;

    return await db
        .delete(mainTable, where: '${EntryFields.id} = ?', whereArgs: [id]);
  }

  Future<Entry?> getEntryById(int id) async {
    final db = await instance.database;
    final List<Map<String, dynamic>> results = await db.query(
      mainTable,
      where: '${EntryFields.id} = ?',
      whereArgs: [id],
    );

    if (results.isNotEmpty) {
      return Entry.fromJson(results.first); // Convert to Entry object.
    }
    return null; // No matching entry found.
  }


// Future<List<Entry>> getByDate(String date) async {
  //   log("DB: get entries for date $date");
  //   final db = await instance.database;
  //
  //   final maps = await db.query(mainTable,
  //       columns: EntryFields.values,
  //       where: '${EntryFields.date} = ?',
  //       whereArgs: [date]);
  //
  //   if (maps.isNotEmpty) {
  //     log("DB: get entries for date $date returned ${maps.length} results");
  //     return maps.map((e) => Entry.fromJson(e)).toList();
  //   } else {
  //     log("DB: get entries for date $date failed");
  //     throw Exception('No entries for the given date');
  //   }
  // }

  // void deleteAllEntriesByDate(String date) async {
  //   // log("DB: delete all dates");
  //   final db = await instance.database;
  //   db.rawDelete('delete from $mainTable where date = ?', [date]);
  // }
}