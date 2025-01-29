import 'package:dio/dio.dart';
import 'package:client/model/category.dart';
import '../model/tip.dart';

// const String baseUrl = 'http://localhost:2302';

import 'dart:developer';

class ApiService {
  static final ApiService instance = ApiService._init();
  static final Dio dio = Dio();

  ApiService._init();

  Future<List<Tip>> getTipsByCategory(String category) async {
    log("Server: get tips from category ${category}");
    String tipsUrl = 'http://10.0.2.2:2302/tips/$category';
    var response = await dio.get(tipsUrl,
        options: Options(
          responseType: ResponseType.json,
        ));

    if (response.statusCode == 200) {
      log("Server: get tips from category ${category} - successful");
      var responseData = response.data as List;
      //       return responseData.map((e) => Pokemon.fromJsonApi(e)).toList();
      // print(">> " + responseData.toString());
      return responseData.map((e) => Tip.fromJsonApi(e)).toList();
    } else {
      log("Server: get tips from category ${category} - failed");
      throw Exception("err");
    }
  }

  Future<List<Tip>> getEasiestTips() async {
    String tipsUrl = 'http://10.0.2.2:2302/easiest';
    var response = await dio.get(tipsUrl,
        options: Options(responseType: ResponseType.json));

    if (response.statusCode == 200) {
      var responseData = response.data as List;

      var allTips = responseData.map((e) => Tip.fromJsonApi(e)).toList();
      allTips.sort((a, b) => a.difficulty.compareTo(b.difficulty));
      allTips.sort((a, b) => a.category.compareTo(b.category));
      return allTips.take(10).toList();
    } else {
      throw Exception("err");
    }
  }

  Future<List<Category>> getCategories() async {
    const String categoriesUrl = 'http://10.0.2.2:2302/categories';
    var response = await dio.get(categoriesUrl,
        options: Options(
          responseType: ResponseType.json,
        ));

    if (response.statusCode == 200) {
      var responseData = response.data as List;
      //       return responseData.map((e) => Pokemon.fromJsonApi(e)).toList();
      // print(">> " + responseData.toString());
      return responseData
          .map((e) => Category(category: e, saved: false))
          .toList();
    } else {
      throw Exception("err");
    }
  }

  Future<Tip> addTip(Tip tip) async {
    var response =
    await dio.post('http://10.0.2.2:2302/tip', data: tip.toJsonApi());
    print(response.data);
    return Tip.fromJsonApi(response.data);
  }

  void deleteTip(int id) async {
    var response = await dio.delete('http://10.0.2.2:2302/tip/$id');
    print(response);
  }

  Future<Tip> updateDifficulty(int id, String difficulty) async {
    Map<String, dynamic> jsonApi = {"id": id, "difficulty": difficulty};
    var response =
    await dio.post('http://10.0.2.2:2302/difficulty', data: jsonApi);

    if (response.statusCode == 200) {
      print("UPDATE: " + response.data.toString());
      return Tip.fromJsonApi(response.data);
    } else {
      throw Exception("err");
    }
  }
}