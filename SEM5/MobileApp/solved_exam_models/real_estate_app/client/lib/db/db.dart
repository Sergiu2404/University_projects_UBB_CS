import 'dart:convert';

import 'package:sqflite/sqflite.dart';
import '../models/entity.dart';

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
    return await openDatabase(path, version: 1, onCreate: _createDB);
  }

  Future _createDB(Database db, int version) async {
    const idType = 'INTEGER PRIMARY KEY';
    const textType = 'TEXT NOT NULL';
    const boolType = 'BOOLEAN NOT NULL';
    const intType = 'INTEGER NOT NULL';
    const doubleType = 'DOUBLE NOT NULL';

    await db.execute(''' 
    CREATE TABLE $mainTable (${PropertyFields.id} $idType, 
                             ${PropertyFields.date} $textType,
                             ${PropertyFields.type} $textType,
                             ${PropertyFields.address} $textType,
                             ${PropertyFields.bedrooms} $intType,
                             ${PropertyFields.bathrooms} $intType,
                             ${PropertyFields.area} $intType,
                             ${PropertyFields.price} $double,
                             ${PropertyFields.notes} $textType
                             )
      ''');

    await db.execute(''' 
      CREATE TABLE $auxTable ('_id' $idType,
      'address' $textType
      )
      ''');
  }

  Future close() async {
    final db = await instance.database;
    db.close();
  }

  // addresses

  Future<Address> addAddress(Address address) async {
    log("DB: add address ${address.address}");
    Map<String, Object?> jsonAddress = {
      "_id": address.id,
      "address": address.address
    };
    final db = await instance.database;
    final id = await db.insert(auxTable, jsonAddress);
    return address;
  }

  void deleteAllAddresses() async {
    // log("DB: delete all dates");
    final db = await instance.database;
    db.rawDelete('delete from address');
  }

  Future<List<Address>> getAddresses() async {
    log("DB: get address");
    final db = await instance.database;
    final result = await db.query(auxTable);
    // maps.map((e) => Entry.fromJson(e)).toList();
    return result.map((e) => Address.fromJson(e)).toList();
  }

  // property entries

  Future<Property> addProperty(Property property) async {
    log("DB: add property");
    final db = await instance.database;
    final id = await db.insert(mainTable, property.toJson());
    return property.copy(id: id);
  }

  Future<int> deleteEntry(int id) async {
    log("DB: delete property with id $id");
    final db = await instance.database;

    return await db
        .delete(mainTable, where: '${PropertyFields.id} = ?', whereArgs: [id]);
  }

  Future<Property> getById(int id) async {
    log("DB: get properties for id $id");
    final db = await instance.database;

    final maps = await db.query(mainTable,
        columns: PropertyFields.values,
        where: '${PropertyFields.id} = ?',
        whereArgs: [id]);

    if (maps.isNotEmpty) {
      log("DB: get properties for id $id returned ${maps.length} results");
      return Property.fromJson(maps[0]);
      //return maps.map((e) => Property.fromJson(e)).toList();
    } else {
      log("DB: get properties for id $id failed");
      throw Exception('No  properties for id $id');
    }
  }

  void deleteAllPropertiesByAddress(int id) async {
    // log("DB: delete all dates");
    final db = await instance.database;
    db.rawDelete('delete from properties where _id = ?', [id]);
  }
}