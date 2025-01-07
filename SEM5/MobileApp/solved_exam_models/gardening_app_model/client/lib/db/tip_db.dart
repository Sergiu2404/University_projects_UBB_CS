import 'dart:convert';

import 'package:sqflite/sqflite.dart';
import '../model/tip.dart';
import '../model/category.dart';

class TipDB {
  static final TipDB instance = TipDB._init();
  static Database? _db;

  TipDB._init();

  Future<Database> get database async {
    if (_db != null) return _db!;
    _db = await _initDB('tip.db');
    return _db!;
  }

  Future<Database> _initDB(String path) async {
    return await openDatabase(path, version: 1, onCreate: _createDB);
  }

  Future _createDB(Database db, int version) async {
    const idType = 'INTEGER PRIMARY KEY AUTOINCREMENT';
    const textType = 'TEXT NOT NULL';
    const boolType = 'BOOLEAN NOT NULL';

    await db.execute(''' 
    CREATE TABLE $tipsTable (${TipFields.id} $idType, 
                             ${TipFields.name} $textType,
                             ${TipFields.description} $textType,
                             ${TipFields.materials} $textType,
                             ${TipFields.steps} $textType,
                             ${TipFields.category} $textType,
                             ${TipFields.difficulty} $textType,
                             ${TipFields.isLocal} $boolType
                             )
      ''');

    await db.execute(''' 
      CREATE TABLE $categoryTable (${CategoryFields.id} $idType,
      ${CategoryFields.category} $textType,
      ${CategoryFields.saved} $boolType
      )
      ''');
  }

  Future close() async {
    final db = await instance.database;
    db.delete(categoryTable);
    db.close();
  }

  Future<Tip> addTip(Tip tip) async {
    final db = await instance.database;
    final id = await db.insert(tipsTable, tip.toJson());
    return tip.copy(id: id);
  }

  Future<List<Tip>> getAllTips() async {
    final db = await instance.database;
    final result = await db.query(tipsTable);
    return result.map((json) => Tip.fromJson(json)).toList();
  }

  Future<List<Tip>> getByCategory(String category) async {
    final db = await instance.database;

    final maps = await db.query(tipsTable,
        columns: TipFields.values,
        where: '${TipFields.category} = ?',
        whereArgs: [category]);

    if (maps.isNotEmpty) {
      print(maps.first);
      return maps.map((e) => Tip.fromJson(e)).toList();
    } else {
      throw Exception('No tips for the given category');
    }
  }

  Future<List<Category>> getAllCategories() async {
    final db = await instance.database;
    final result = await db.query(categoryTable);
    // print("CATEGORIES: " + result.toString());
    return result.map((json) => Category.fromJson(json)).toList();
  }

  Future<Category> findByName(String name) async {
    final db = await instance.database;

    final maps = await db.query(categoryTable,
        columns: CategoryFields.values,
        where: '${CategoryFields.category} = ?',
        whereArgs: [name]);

    if (maps.isNotEmpty) {
      // print(maps.first);
      return Category.fromJson(maps.first);
    } else {
      throw Exception('Category not found.');
    }
  }

  Future<Category> addCategory(Category category) async {
    final db = await instance.database;
    final id = await db.insert(categoryTable, category.toJson());
    // print("Category " + category.category + " inserted in db with id: " + id.toString());
    return category.copy(id: id);
  }

  Future<List<Category>> findAllCategories() async {
    final db = await instance.database;

    final result = await db.query(categoryTable);

    return result.map((json) => Category.fromJson(json)).toList();
  }

  Future<int> delete(String name) async {
    final db = await instance.database;

    return await db
        .delete(tipsTable, where: '${TipFields.name} = ?', whereArgs: [name]);
  }
}