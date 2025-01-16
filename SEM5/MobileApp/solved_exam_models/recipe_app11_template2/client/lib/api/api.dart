import 'package:dio/dio.dart';
import 'dart:developer';

import '../model/entity.dart';

const String baseUrl = 'http://10.0.2.2:2307';

class ApiService {
  // BaseOptions options = new BaseOptions(
  //   baseUrl: "http://10.0.2.2:2307",
  //   connectTimeout: 5000,
  //   receiveTimeout: 3000,
  // );
  static final ApiService instance = ApiService._init();
  static final Dio dio = Dio();

  ApiService._init();

  // date

  Future<List<String>> getTypes() async {
    log("Server: get types list");
    const String getUrl = '$baseUrl/types';

    var response = await dio
        .get(getUrl,
        options: Options(
          responseType: ResponseType.json,
        ))
        .timeout(const Duration(seconds: 10));

    if (response.statusCode == 200) {
      var responseData = response.data as List;
      log("Server: get types list executed successfully");
      return responseData.map((e) => e.toString()).toList();
    } else {
      log("Server: get types list failed");
      throw Exception("err");
    }
  }

  // health entries

  Future<List<Entry>> getEntriesByType(String type) async {
    log("Server: get entries for type $type");
    String getUrl = '$baseUrl/recipes/$type';
    var response = await dio
        .get(getUrl,
        options: Options(
          responseType: ResponseType.json,
        ))
        .timeout(const Duration(seconds: 10));

    if (response.statusCode == 200) {
      log("Server: get data for type $type - successful");
      var responseData = response.data as List;
      //       return responseData.map((e) => Pokemon.fromJsonApi(e)).toList();
      // print(">> " + responseData.toString());
      return responseData.map((e) => Entry.fromJsonApi(e)).toList();
    } else {
      log("Server: get health entries for type $type - failed");
      throw Exception("err");
    }
  }

  Future<List<Entry>> getTopUnderrated() async {
    log("Server: get underrated entries");
    String getUrl = '$baseUrl/low';
    var response = await dio
        .get(getUrl,
        options: Options(
          responseType: ResponseType.json,
        ))
        .timeout(const Duration(seconds: 10));

    if (response.statusCode == 200) {
      log("Server: get underrated data - successful");
      var responseData = response.data as List;
      //       return responseData.map((e) => Pokemon.fromJsonApi(e)).toList();
      // print(">> " + responseData.toString());
      return responseData.map((e) => Entry.fromJsonApi(e)).toList();
    } else {
      log("Server: get underrated data - failed");
      throw Exception("err");
    }
  }

  Future<Entry?> incrementRating(int id) async {
    log("Increment rating by one for recipe with id $id");
    String postUrl = '$baseUrl/increment';
    var response = await dio
        .post(postUrl,
        options: Options(
          responseType: ResponseType.json,
        ),
        data: {"id": id})
        .timeout(const Duration(seconds: 10));

    if (response.statusCode == 200) {
      log("Server: incrementing rating - successful");
      var responseData = response.data;
      log("Updated Recipe: ${responseData['updatedRecipe']}");
      // Convert the updated recipe to an Entry object and return it
      return Entry.fromJsonApi(responseData['updatedRecipe']);
    } else {
      log("Server: incrementing rating - failed");
      throw Exception("Failed to increment rating");
    }
  }

  Future<List<Entry>> getAllEntries() async {
    log("Server: get entry list");
    const String getUrl = '$baseUrl/recipes';

    var response = await dio
        .get(getUrl,
        options: Options(
          responseType: ResponseType.json,
        ))
        .timeout(const Duration(seconds: 10));

    if (response.statusCode == 200) {
      var responseData = response.data as List;
      log("Server: get entry list executed successfully");
      return responseData.map((e) => Entry.fromJsonApi(e)).toList();
    } else {
      log("Server: get entry list failed");
      throw Exception("get all entries");
    }
  }

  Future<Entry> addEntry(Entry entry) async {
    log("Server: add entry $entry");
    var response = await dio
        .post('$baseUrl/recipe', data: entry.toJsonApi())
        .timeout(const Duration(seconds: 10));
    ;
    if (response.statusCode == 200) {
      log("Server: add entry ${entry.name} - successful");
      return Entry.fromJsonApi(response.data);
    } else {
      log("Server: add entry ${entry.name} - failed");
      throw Exception('add failed');
    }
  }

  void deleteEntry(int id) async {
    log("Server: delete entry with id $id");
    var response = await dio
        .delete('$baseUrl/recipe/$id')
        .timeout(const Duration(seconds: 10));
    if (response.statusCode == 200) {
      log("Server: delete entry $id - successful");
    } else {
      log("Server: delete entry $id - failed");
      throw Exception('delete failed');
    }
    log("Server: deleted entry $response");
  }
}