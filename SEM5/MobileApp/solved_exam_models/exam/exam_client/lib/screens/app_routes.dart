import 'package:exam_client/screens/explore_section.dart';
import 'package:exam_client/screens/top_section.dart';
import 'package:flutter/material.dart';
import 'main_section.dart';
import 'mainpage.dart';

class AppRoutes {
  static Map<String, WidgetBuilder> routes = {
    '/': (context) => MainPage(),
    '/top-section': (context) {
      final online = ModalRoute.of(context)!.settings.arguments as bool;
      return TopSection(online);
    },
    '/explore-section': (context) {
      final online = ModalRoute.of(context)!.settings.arguments as bool;
      return ExploreSection("Explore Section", online);
    },
    '/main': (context) {
      final online = ModalRoute.of(context)!.settings.arguments as bool;
      return MainSection("Main Section", online);
    },
  };
}