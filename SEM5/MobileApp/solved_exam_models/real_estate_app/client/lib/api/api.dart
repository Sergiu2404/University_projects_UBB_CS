import 'package:dio/dio.dart';
import 'dart:developer';

import 'package:client/models/entity.dart';

// import '../model/entity.dart';

const String baseUrl = 'http://10.0.2.2:2309';

class ApiService {
  static final ApiService instance = ApiService._init();
  static final Dio dio = Dio();

  ApiService._init();

  // date

  Future<List<Address>> getAddresses() async {
    log("Server: get properties list");
    const String getUrl = '$baseUrl/properties';

    var response = await dio
        .get(getUrl,
        options: Options(
          responseType: ResponseType.json,
        ))
        .timeout(const Duration(seconds: 15));

    if (response.statusCode == 200) {
      var responseData = response.data as List;
      log("Server: get properties list executed successfully");
      return responseData.map((e) => Address.fromJsonApi(e)).toList();
    } else {
      log("Server: get properties list failed");
      throw Exception("err");
    }
  }

  // properties

  Future<Property> getPropertiesByAddress(int id) async {
    log("Server: get properties for address id-ed $id");
    String getUrl = '$baseUrl/property/$id';
    var response = await dio
        .get(
      getUrl,
      // options: Options(
      //   responseType: ResponseType.json,
      // )
    )
        .timeout(const Duration(seconds: 15));

    if (response.statusCode == 200) {
      log("Server: get properties for address id-ed $id - successful");
      /*
      List<dynamic> list = json.decode(receivedJson);
Fact fact = Fact.fromJson(list[0]);
return (response.data as List)
          .map((x) => OnBoardingModel.fromJson(x))
          .toList();
      */
      Map responseData = response.data as Map;
      print(responseData.toString());
      //       return responseData.map((e) => Pokemon.fromJsonApi(e)).toList();
      // print(">> " + responseData.toString());
      return Property.fromJsonApi(responseData);
      // return responseData.map((e) => Property.fromJsonApi(e);
    } else {
      log("Server: get properties for address id-ed $id - failed");
      throw Exception("err");
    }
  }

  Future<List<Property>> getAllEntries() async {
    log("Server: get search list");
    const String getUrl = '$baseUrl/search';

    var response = await dio.get(getUrl,
        options: Options(
          responseType: ResponseType.json,
        ));

    if (response.statusCode == 200) {
      var responseData = response.data as List;
      log("Server: get property search list executed successfully");
      return responseData.map((e) => Property.fromJsonApi(e)).toList();
    } else {
      log("Server: get search list failed");
      throw Exception("err");
    }
  }

  Future<Property> addEntry(Property p) async {
    log("Server: add property " + p.toString());
    var response = await dio
        .post('$baseUrl/property', data: p.toJsonApi())
        .timeout(const Duration(seconds: 15));
    if (response.statusCode == 200) {
      log("Server: add property - successful");
      return Property.fromJsonApi(response.data);
    } else {
      log("Server: add property - failed");
      throw Exception('err');
    }
  }

  void deleteEntry(int id) async {
    log("Server: delete entry with id $id");
    var response = await dio.delete('$baseUrl/property/$id');
    log("Server: delete entry $response");
  }
}