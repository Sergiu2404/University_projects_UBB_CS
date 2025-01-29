import 'package:flutter/material.dart';
import 'package:project_management_client/screens/team_member_section.dart';
import 'main_section.dart';
import 'mainpage.dart';

class AppRoutes {
  static Map<String, WidgetBuilder> routes = {
    '/': (context) => MainPage(),
    '/main': (context) {
      final online = ModalRoute.of(context)!.settings.arguments as bool;
      return ProjectManagerSection("Main Section", online);
    },
    '/inProgress': (context) {
      final online = ModalRoute.of(context)!.settings.arguments as bool;
      return GetInProgressProjects("In Progress Projects Section", online);
    },
    // '/progress': (context) {
    //   final online = ModalRoute.of(context)!.settings.arguments as bool;
    //   return ProgressSection(online);
    // },
    // '/top': (context) {
    //   final online = ModalRoute.of(context)!.settings.arguments as bool;
    //   return TopSection(online);
    // },
  };
}