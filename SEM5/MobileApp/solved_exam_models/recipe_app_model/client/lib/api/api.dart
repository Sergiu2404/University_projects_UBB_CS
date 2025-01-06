import 'package:dio/dio.dart';
import 'dart:developer';

import '../model/recipe.dart';

const String baseUrl = 'http://10.0.2.2:2325';

class ApiService {
  static final ApiService instance = ApiService._init();
  static final Dio dio = Dio();

  ApiService._init();

  // categories

  Future<List<String>> getCategories() async {
    log("Server: get category list");
    const String getUrl = '$baseUrl/categories';

    var response = await dio.get(getUrl,
        options: Options(
          responseType: ResponseType.json,
        ));

    if (response.statusCode == 200) {
      var responseData = response.data as List;
      log("Server: get category list executed successfully");
      return responseData.map((e) => e.toString()).toList();
    } else {
      log("Server: get category list failed");
      throw Exception("err");
    }
  }

  // recipes

  Future<List<Recipe>> getRecipesByCategory(String category) async {
    log("Server: get recipes for category $category");
    String getUrl = '$baseUrl/recipes/$category';
    var response = await dio.get(getUrl,
        options: Options(
          responseType: ResponseType.json,
        ));

    if (response.statusCode == 200) {
      log("Server: get recipes for category $category - successful");
      var responseData = response.data as List;
      return responseData.map((e) => Recipe.fromJsonApi(e)).toList();
    } else {
      log("Server: get recipes for category $category");
      throw Exception("err");
    }
  }

  Future<Recipe> addRecipe(Recipe recipe) async {
    log("Server: add recipe ${recipe.name} ");
    var response = await dio.post('$baseUrl/recipe', data: recipe.toJsonApi());
    if (response.statusCode == 200) {
      log("Server: add recipe ${recipe.name} - successful");
      return Recipe.fromJsonApi(response.data);
    } else {
      log("Server: add recipe ${recipe.name} - failed");
      throw Exception('err');
    }
  }

  void deleteRecipe(int id) async {
    log("Server: delete recipe with id $id");
    var response = await dio.delete('$baseUrl/recipe/$id');

    if (response.statusCode == 200) {
      log("Server: delete recipe with $id - successful");
    } else {
      log("Server: delete recipe with $id - failed");
      throw Exception('err');
    }
  }

  Future<List<Recipe>> getAllRecipes() async {
    log("Server: get all recipes");
    String getUrl = '$baseUrl/easiest';
    var response = await dio.get(getUrl,
        options: Options(
          responseType: ResponseType.json,
        ));

    if (response.statusCode == 200) {
      log("Server: get all recipes - successful");
      var responseData = response.data as List;
      return responseData.map((e) => Recipe.fromJsonApi(e)).toList();
    } else {
      log("Server: get all recipes = failed");
      throw Exception("err");
    }
  }

  Future<Recipe> updateDifficulty(int id, String difficulty) async {
    log("Server: Update difficulty for recipe $id to $difficulty");

    Map<String, dynamic> jsonApi = {"id": id, "difficulty": difficulty};
    var response = await dio.post('$baseUrl/difficulty', data: jsonApi);

    if (response.statusCode == 200) {
      log("Server: Update difficulty for recipe $id to $difficulty - successful");
      return Recipe.fromJsonApi(response.data);
    } else {
      throw Exception("err");
    }
  }
}