import 'dart:convert';

import 'package:sqflite/sqflite.dart';
import '../model/recipe.dart';

import 'dart:developer';

class RecipeDB {
  static final RecipeDB instance = RecipeDB._init();
  static Database? _db;

  RecipeDB._init();

  Future<Database> get database async {
    if (_db != null) return _db!;
    _db = await _initDB('tip.db');
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
    CREATE TABLE $recipeTable (${RecipeFields.id} $idType, 
                             ${RecipeFields.name} $textType,
                             ${RecipeFields.description} $textType,
                             ${RecipeFields.ingredients} $textType,
                             ${RecipeFields.instructions} $textType,
                             ${RecipeFields.category} $textType,
                             ${RecipeFields.difficulty} $textType
                             )
      ''');

    await db.execute(''' 
      CREATE TABLE $categoryTable ('_id' $idType,
      'category' $textType
      )
      ''');
  }

  Future close() async {
    final db = await instance.database;
    db.close();
  }

  // categories

  Future<String> addCategory(String category) async {
    log("DB: add category ${category}");
    Map<String, Object?> jsoncategory = {"category": category};
    final db = await instance.database;
    final id = await db.insert(categoryTable, jsoncategory);
    return category;
  }

  void deleteAllcategories() async {
    // log("DB: delete all categories");
    final db = await instance.database;
    db.rawDelete('delete from categories');
  }

  Future<List<String>> getcategories() async {
    log("DB: get categories");
    final db = await instance.database;
    final result = await db.query(categoryTable);
    return result.map((json) => json['category'].toString()).toList();
  }

  // recipes

  Future<Recipe> addRecipe(Recipe recipe) async {
    final db = await instance.database;
    final id = await db.insert(recipeTable, recipe.toJson());
    return recipe.copy(id: id);
  }

  Future<List<Recipe>> getByCategory(String category) async {
    log("DB: get recipes for category $category");
    final db = await instance.database;

    final maps = await db.query(recipeTable,
        columns: RecipeFields.values,
        where: '${RecipeFields.category} = ?',
        whereArgs: [category]);

    if (maps.isNotEmpty) {
      log("DB: get recipes for category $category returned ${maps.length} results");
      return maps.map((e) => Recipe.fromJson(e)).toList();
    } else {
      log("DB: get recipes for category $category failed");
      throw Exception('No recipes for the category $category');
    }
  }

  void deleteAllRecipesByCategory(String category) async {
    // log("DB: delete all genres");
    final db = await instance.database;
    db.rawDelete('delete from recipes where category = ?', [category]);
  }

  Future<int> deleteRecipe(int id) async {
    log("DB: delete recipe with id $id");
    final db = await instance.database;

    return await db
        .delete(recipeTable, where: '${RecipeFields.id} = ?', whereArgs: [id]);
  }

  Future<int> updateRecipe(Recipe recipe) async {
    log("DB: update recipe with id $recipe.id");
    final db = await instance.database;

    return db.update(
      recipeTable,
      recipe.toJson(),
      where: '${RecipeFields.id} = ?',
      whereArgs: [recipe.id],
    );
  }
}