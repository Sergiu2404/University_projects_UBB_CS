import 'package:dio/dio.dart';
import 'dart:developer';

import '../model/entity.dart';

const String baseUrl = 'http://10.0.2.2:2404';

class APIRepo {
  // BaseOptions options = new BaseOptions(
  //   baseUrl: "http://10.0.2.2:2307",
  //   connectTimeout: 5000,
  //   receiveTimeout: 3000,
  // );
  static final APIRepo instance = APIRepo._init();
  static final Dio dio = Dio();

  APIRepo._init();

  // date

  Future<List<String>> getGenres() async {
    log("Server: get genres list");
    const String getUrl = '$baseUrl/genres';

    var response = await dio
        .get(getUrl,
        options: Options(
          responseType: ResponseType.json,
        ))
        .timeout(const Duration(seconds: 10));

    if (response.statusCode == 200) {
      var responseData = response.data as List;
      log("Server: get genres list executed successfully");
      return responseData.map((e) => e.toString()).toList();
    } else {
      log("Server: get genres list failed");
      throw Exception("err");
    }
  }

  Future<List<Entry>> getEntriesByGenre(String genre) async {
    log("Server: get entries for date $genre");
    String getUrl = '$baseUrl/movies/$genre';
    var response = await dio
        .get(getUrl,
        options: Options(
          responseType: ResponseType.json,
        ))
        .timeout(const Duration(seconds: 10));

    if (response.statusCode == 200) {
      log("Server: get entries for date $genre - successful");
      var responseData = response.data as List;
      return responseData.map((e) => Entry.fromJsonApi(e)).toList();
    } else {
      log("Server: get entries for date $genre - failed");
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
      log("Server: get entry list failed");
      throw Exception("get all entries");
    }
  }

  Future<Entry> addEntry(Entry entry) async {
    log("Server: add entry $entry");
    var response = await dio
        .post('$baseUrl/movie', data: entry.toJsonApi())
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
        .delete('$baseUrl/movie/$id')
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