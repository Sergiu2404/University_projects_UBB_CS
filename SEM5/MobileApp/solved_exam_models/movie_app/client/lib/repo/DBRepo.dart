import 'dart:convert';

import 'package:sqflite/sqflite.dart';
import '../model/entity.dart';

import 'dart:developer';


const String mainTable = 'entries';
const String auxTable = 'genres';

class DBRepo {
  static final DBRepo instance = DBRepo._init();
  static Database? _db;

  DBRepo._init();

  Future<Database> get database async {
    if (_db != null) return _db!;
    _db = await _initDB('movie_app.db');
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
                             ${EntryFields.description} $textType,
                             ${EntryFields.genre} $textType,
                             ${EntryFields.director} $textType,
                             ${EntryFields.year} $intType,
                             )
      ''');

    await db.execute(''' 
      CREATE TABLE $auxTable ('_id' $idType,
      'genre' $textType
      )
      ''');
  }

  Future close() async {
    final db = await instance.database;
    db.close();
  }

  Future<String> addGenre(String genre) async {
    log("DB: add day ${genre}");
    Map<String, Object?> jsonGenre = {"genre": genre};
    final db = await instance.database;
    final id = await db.insert(auxTable, jsonGenre);
    return genre;
  }

  void deleteAllGenres() async {
    log("DB: delete all genres");
    final db = await instance.database;
    db.rawDelete('delete from $auxTable');
  }

  Future<List<String>> getGenres() async {
    log("DB: get genres");
    final db = await instance.database;
    final result = await db.query(auxTable);
    return result.map((json) => json['genre'].toString()).toList();
  }

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

  Future<List<Entry>> getByGenre(String genre) async {
    log("DB: get entries for date $genre");
    final db = await instance.database;

    final maps = await db.query(mainTable,
        columns: EntryFields.values,
        where: '${EntryFields.genre} = ?',
        whereArgs: [genre]);

    if (maps.isNotEmpty) {
      log("DB: get entries for date $genre returned ${maps.length} results");
      return maps.map((e) => Entry.fromJson(e)).toList();
    } else {
      log("DB: get entries for date $genre failed");
      throw Exception('No entries for the given date');
    }
  }

  void deleteAllEntriesByGenre(String genre) async {
    // log("DB: delete all dates");
    final db = await instance.database;
    db.rawDelete('delete from entries where genre = ?', [genre]);
  }
}