// import 'package:dio/dio.dart';
// import 'dart:developer';
//
// import '../model/entity.dart';
//
// const String baseUrl = 'http://10.0.2.2:2307';
//
// class ApiService {
//   // BaseOptions options = new BaseOptions(
//   //   baseUrl: "http://10.0.2.2:2307",
//   //   connectTimeout: 5000,
//   //   receiveTimeout: 3000,
//   // );
//   static final ApiService instance = ApiService._init();
//   static final Dio dio = Dio();
//
//   ApiService._init();
//
//   // date
//
//   Future<List<String>> getDays() async {
//     log("Server: get day list");
//     const String getUrl = '$baseUrl/days';
//
//     var response = await dio
//         .get(getUrl,
//         options: Options(
//           responseType: ResponseType.json,
//         ))
//         .timeout(const Duration(seconds: 10));
//
//     if (response.statusCode == 200) {
//       var responseData = response.data as List;
//       log("Server: get days list executed successfully");
//       return responseData.map((e) => e.toString()).toList();
//     } else {
//       log("Server: get days list failed");
//       throw Exception("err");
//     }
//   }
//
//   // health entries
//
//   Future<List<Entry>> getEntriesByDate(String date) async {
//     log("Server: get health entries for date $date");
//     String getUrl = '$baseUrl/symptoms/$date';
//     var response = await dio
//         .get(getUrl,
//         options: Options(
//           responseType: ResponseType.json,
//         ))
//         .timeout(const Duration(seconds: 10));
//
//     if (response.statusCode == 200) {
//       log("Server: get health entries for date $date - successful");
//       var responseData = response.data as List;
//       //       return responseData.map((e) => Pokemon.fromJsonApi(e)).toList();
//       // print(">> " + responseData.toString());
//       return responseData.map((e) => Entry.fromJsonApi(e)).toList();
//     } else {
//       log("Server: get health entries for date $date - failed");
//       throw Exception("err");
//     }
//   }
//
//   Future<List<Entry>> getAllEntries() async {
//     log("Server: get entry list");
//     const String getUrl = '$baseUrl/entries';
//
//     var response = await dio
//         .get(getUrl,
//         options: Options(
//           responseType: ResponseType.json,
//         ))
//         .timeout(const Duration(seconds: 10));
//
//     if (response.statusCode == 200) {
//       var responseData = response.data as List;
//       log("Server: get entry list executed successfully");
//       return responseData.map((e) => Entry.fromJsonApi(e)).toList();
//     } else {
//       log("Server: get entry list failed");
//       throw Exception("get all entries");
//     }
//   }
//
//   Future<Entry> addEntry(Entry entry) async {
//     log("Server: add entry $entry");
//     var response = await dio
//         .post('$baseUrl/symptom', data: entry.toJsonApi())
//         .timeout(const Duration(seconds: 10));
//     ;
//     if (response.statusCode == 200) {
//       log("Server: add entry ${entry.symptom} - successful");
//       return Entry.fromJsonApi(response.data);
//     } else {
//       log("Server: add entry ${entry.symptom} - failed");
//       throw Exception('add failed');
//     }
//   }
//
//   void deleteEntry(int id) async {
//     log("Server: delete entry with id $id");
//     var response = await dio
//         .delete('$baseUrl/symptom/$id')
//         .timeout(const Duration(seconds: 10));
//     if (response.statusCode == 200) {
//       log("Server: delete entry $id - successful");
//     } else {
//       log("Server: delete entry $id - failed");
//       throw Exception('delete failed');
//     }
//     log("Server: deleted entry $response");
//   }
// }