import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

CustomRequestMessage(BuildContext context, String content) {
  showDialog(
      context: context,
      builder: (_) => AlertDialog(
        title: Text("Info"),
        content: Text(content),
      ));
}