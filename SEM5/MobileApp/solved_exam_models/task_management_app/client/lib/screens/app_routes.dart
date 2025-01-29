import 'package:client/screens/main_section.dart';
import 'package:client/screens/progress_section.dart';
import 'package:client/screens/top_section.dart';
import 'package:flutter/material.dart';
import 'mainpage.dart';

class AppRoutes {
  static Map<String, WidgetBuilder> routes = {
    '/': (context) => MainPage(),
    '/main': (context) {
      final online = ModalRoute.of(context)!.settings.arguments as bool;
      return HomePage("Main Section", online);
    },
    '/progress': (context) {
      final online = ModalRoute.of(context)!.settings.arguments as bool;
      return ProgressSection(online);
    },
    '/top': (context) {
      final online = ModalRoute.of(context)!.settings.arguments as bool;
      return TopSection(online);
    },
  };
}