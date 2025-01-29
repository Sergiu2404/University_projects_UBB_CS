import 'package:flutter/material.dart';

CustomMessageResponse(BuildContext context, String content) {
  showDialog(
      context: context,
      builder: (_) => AlertDialog(
        title: Text("Info"),
        content: Text(content),
      ));
}