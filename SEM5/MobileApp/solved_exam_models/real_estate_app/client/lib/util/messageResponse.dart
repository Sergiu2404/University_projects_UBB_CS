import "package:flutter/material.dart";

messageResponse(BuildContext context, String content)
{
  showDialog(
    context: context,
    builder: (_) => AlertDialog(
      title: Text("Info"),
      content: Text(content),
    )
  );
}