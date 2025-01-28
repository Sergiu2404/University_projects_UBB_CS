import 'package:dio/dio.dart';
import 'dart:developer';

import '../model/entry.dart';

const String baseUrl = 'http://10.0.2.2:7496';

class API_Repo {
  static final API_Repo instance = API_Repo._init();
  static final Dio dio = Dio();

  API_Repo._init();

  Future<List<String>> getDates() async {
    log("Server: get date list");
    const String getUrl = '$baseUrl/dates';

    var response = await dio.get(getUrl,
        options: Options(
          responseType: ResponseType.json,
        ));

    if (response.statusCode == 200) {
      var responseData = response.data as List;
      log("Server: get date list executed successfully");
      return responseData.map((e) => e.toString()).toList();
    } else {
      log("Server: get date list failed");
      throw Exception("err");
    }
  }

  Future<List<Entry>> getEntriesByDate(String date) async {
    log("Server: get entries for date $date");
    String getUrl = '$baseUrl/entries/$date';
    var response = await dio.get(getUrl,
        options: Options(
          responseType: ResponseType.json,
        ));

    if (response.statusCode == 200) {
      log("Server: get entries for date $date - successful");
      var responseData = response.data as List;
      //       return responseData.map((e) => Pokemon.fromJsonApi(e)).toList();
      // print(">> " + responseData.toString());
      return responseData.map((e) => Entry.fromJsonApi(e)).toList();
    } else {
      log("Server: get entries for date $date");
      throw Exception("err");
    }
  }

  Future<List<Entry>> getAllEntries() async {
    log("Server: get entry list");
    const String getUrl = '$baseUrl/all';

    var response = await dio.get(getUrl,
        options: Options(
          responseType: ResponseType.json,
        ));

    if (response.statusCode == 200) {
      var responseData = response.data as List;
      log("Server: get entry list executed successfully");
      return responseData.map((e) => Entry.fromJsonApi(e)).toList();
    } else {
      log("Server: get entry list failed");
      throw Exception("err");
    }
  }

  Future<Entry> addEntry(Entry entry) async {
    log("Server: add entry " + entry.toString());
    var response = await dio.post('$baseUrl/entry', data: entry.toJsonApi());
    log("added?");
    if (response.statusCode == 200) {
      log("Server: add entry ${entry.type} - successful");
      return Entry.fromJsonApi(response.data);
    } else {
      log("Server: add entry ${entry.type} - failed");
      throw Exception('err');
    }
  }

  Future<Entry> updateEntry(Entry entry) async {
    log("Server: update entry with id ${entry.id}");
    String updateUrl = '$baseUrl/entry/${entry.id}';

    var response = await dio.put(
      updateUrl,
      data: entry.toJsonApi(),
    );

    if (response.statusCode == 200) {
      log("Server: update entry ${entry.type} - successful");
      return Entry.fromJsonApi(response.data);
    } else {
      log("Server: update entry ${entry.type} - failed");
      throw Exception('Error updating entry');
    }
  }

  void deleteEntry(int id) async {
    log("Server: delete entry with id $id");
    var response = await dio.delete('$baseUrl/entry/$id');
    log("Server: delete entry $response");
  }
}