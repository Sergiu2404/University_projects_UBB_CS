import 'package:dio/dio.dart';
import 'dart:developer';
import 'package:http/http.dart' as http;

import '../model/entity.dart';

const String baseUrl = 'http://10.0.2.2:2404';
// const String baseUrl = 'http://192.168.1.1:2404';

class APIRepository {
  // BaseOptions options = new BaseOptions(
  //   baseUrl: "http://10.0.2.2:2404",
  //   connectTimeout: 5000,
  //   receiveTimeout: 3000,
  // );
  static final APIRepository instance = APIRepository._init();
  static final Dio dio = Dio();


  APIRepository._init();

  Future<List<String>> getLocations() async {
    log("Server: get locations list");
    const String getUrl = '$baseUrl/locations';

    var response = await dio
        .get(getUrl,
        options: Options(
          responseType: ResponseType.json,
        ))
        .timeout(const Duration(seconds: 10));

    if (response.statusCode == 200) {
      var responseData = response.data as List;
      log("Server: get locations list executed successfully");
      return responseData.map((e) => e.toString()).toList();
    } else {
      log("Server: get locations list failed");
      throw Exception("err");
    }
  }

  Future<List<Entry>> getEntriesByLocation(String location) async {
    log("Server: get entries for $location");
    String getUrl = '$baseUrl/files/$location';
    var response = await dio
        .get(getUrl,
        options: Options(
          responseType: ResponseType.json,
        ))
        .timeout(const Duration(seconds: 10));

    if (response.statusCode == 200) {
      log("Server: get entries for $location - successful");
      var responseData = response.data as List;
      return responseData.map((e) => Entry.fromJsonApi(e)).toList();
    } else {
      log("Server: get entries for $location - failed");
      throw Exception("err");
    }
  }

  Future<List<Entry>> getAllEntries() async {
    log("Server: get entry list");
    const String getUrl = '$baseUrl/all';

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
      log("Server: get entry list failed with error ${response.statusCode}");
      throw Exception("get all entries");
    }
  }

  Future<Entry> addEntry(Entry entry) async {
    log("Server: add entry $entry");
    var response = await dio
        .post('$baseUrl/file', data: entry.toJsonApi())
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
        .delete('$baseUrl/file/$id')
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