import 'dart:convert';

import 'package:sqflite/sqflite.dart';
import '../model/entity.dart';

import 'dart:developer';

class EntityDB {
  static final EntityDB instance = EntityDB._init();
  static Database? _db;

  EntityDB._init();

  Future<Database> get database async {
    if (_db != null) return _db!;
    _db = await _initDB('entity.db');
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

    await db.execute(''' 
    CREATE TABLE $mainTable (${EntryFields.id} $idType, 
                             ${EntryFields.name} $textType,
                             ${EntryFields.details} $textType,
                             ${EntryFields.time} $intType,
                             ${EntryFields.type} $textType,
                             ${EntryFields.rating} $textType
                             )
      ''');

    await db.execute(''' 
      CREATE TABLE $auxTable ('_id' $idType,
      'type' $textType
      )
      ''');
  }

  Future close() async {
    final db = await instance.database;
    db.close();
  }

  // days
  Future<String> addDay(String type) async {
    log("DB: add type ${type}");
    Map<String, Object?> jsonDay = {"day": type};
    final db = await instance.database;
    final id = await db.insert(auxTable, jsonDay);
    return type;
  }

  void deleteAllDays() async {
    // log("DB: delete all dates");
    final db = await instance.database;
    db.rawDelete('delete from types');
  }

  Future<List<String>> getDates() async {
    log("DB: get types");
    final db = await instance.database;
    final result = await db.query(auxTable);
    return result.map((json) => json['type'].toString()).toList();
  }

  // health entries

  Future<Entry> addEntry(Entry entry) async {
    final db = await instance.database;
    final id = await db.insert(mainTable, entry.toJson());
    return entry.copy(id: id);
  }

  Future<int> deleteEntry(int id) async {
    log("DB: delete entry with id $id");
    final db = await instance.database;

    return await db
        .delete(mainTable, where: '${EntryFields.id} = ?', whereArgs: [id]);
  }

  Future<List<Entry>> getByType(String type) async {
    log("DB: get health entries for type $type");
    final db = await instance.database;

    final maps = await db.query(mainTable,
        columns: EntryFields.values,
        where: '${EntryFields.type} = ?',
        whereArgs: [type]);

    if (maps.isNotEmpty) {
      log("DB: get health entries for type $type returned ${maps.length} results");
      return maps.map((e) => Entry.fromJson(e)).toList();
    } else {
      log("DB: get health entries for type $type failed");
      throw Exception('No health entries for the given date');
    }
  }

  void deleteAllEntriesByType(String type) async {
    // log("DB: delete all dates");
    final db = await instance.database;
    db.rawDelete('delete from entries where type = ?', [type]);
  }

  // Future<List<Entry>> getUnderRatedRecipes() async {
  //   final db = await instance.database;
  //
  //   final maps = await db.query(mainTable,
  //       columns: EntryFields.values,
  //       where: '${EntryFields.type} = ?',
  //       whereArgs: [type]);
  //
  //   if (maps.isNotEmpty) {
  //     log("DB: get health entries for type $type returned ${maps.length} results");
  //     return maps.map((e) => Entry.fromJson(e)).toList();
  //   } else {
  //     log("DB: get health entries for type $type failed");
  //     throw Exception('No health entries for the given date');
  //   }
  // }
}

