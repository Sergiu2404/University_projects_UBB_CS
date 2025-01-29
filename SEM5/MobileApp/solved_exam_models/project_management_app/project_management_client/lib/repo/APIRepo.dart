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

  // Future<List<String>> getDates() async {
  //   log("Server: get dates list");
  //   const String getUrl = '$baseUrl/taskDays';
  //
  //   var response = await dio
  //       .get(getUrl,
  //       options: Options(
  //         responseType: ResponseType.json,
  //       ))
  //       .timeout(const Duration(seconds: 10));
  //
  //   if (response.statusCode == 200) {
  //     var responseData = response.data as List;
  //     log("Server: get dates list executed successfully");
  //     return responseData.map((e) => e.toString()).toList();
  //   } else {
  //     log("Server: get dates list failed");
  //     throw Exception("err");
  //   }
  // }
  //
  // Future<List<Entry>> getEntriesByDate(String date) async {
  //   log("Server: get entries for date $date");
  //   String getUrl = '$baseUrl/details/$date';
  //   var response = await dio
  //       .get(getUrl,
  //       options: Options(
  //         responseType: ResponseType.json,
  //       ))
  //       .timeout(const Duration(seconds: 10));
  //
  //   if (response.statusCode == 200) {
  //     log("Server: get entries for date $date - successful");
  //     var responseData = response.data as List;
  //     return responseData.map((e) => Entry.fromJsonApi(e)).toList();
  //   } else {
  //     log("Server: get entries for date $date - failed");
  //     throw Exception("err");
  //   }
  // }

  Future<List<Entry>> getAllEntries() async {
    log("Server: get entry list");
    const String getUrl = '$baseUrl/projects';

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

  Future<List<Entry>> getAllProjects() async {
    log("Server: get entry list");
    const String getUrl = '$baseUrl/allProjects';

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

  Future<List<Entry>> getInProgressEntries() async {
    log("Server: get in progress entry list");
    const String getUrl = '$baseUrl/inProgress';

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
        .post('$baseUrl/project', data: entry.toJsonApi())
        .timeout(const Duration(seconds: 10));
    ;
    if (response.statusCode == 200) {
      log("Server: add entry ${entry.type}, ${entry.name} - successful");
      return Entry.fromJsonApi(response.data);
    } else {
      log("Server: add entry ${entry.type}, ${entry.name} - failed");
      throw Exception('add failed');
    }
  }

  Future<Entry> getEntryById(int id) async {
    log("Server: get entry with id $id");
    try {
      var response = await dio
          .get('$baseUrl/project/$id')
          .timeout(const Duration(seconds: 10));

      if (response.statusCode == 200) {
        log("Server: get entry $id - successful");
        // Convert response data to an Entry object
        return Entry.fromJson(response.data);
      } else {
        log("Server: get entry $id - failed with status ${response.statusCode}");
        throw Exception('Failed to retrieve entry with id $id');
      }
    } catch (e) {
      log("Server: get entry $id - error $e");
      throw Exception('Failed to retrieve entry: $e');
    }
  }

  void deleteEntry(int id) async {
    log("Server: delete entry with id $id");
    var response = await dio
        .delete('$baseUrl/project/$id')
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