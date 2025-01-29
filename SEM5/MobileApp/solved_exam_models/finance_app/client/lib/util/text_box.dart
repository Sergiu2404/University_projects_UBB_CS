import 'package:flutter/material.dart';

class TextBox extends StatelessWidget{
  final TextEditingController _controller;
  final String _label;
  const TextBox(this._controller, this._label, {super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
        padding: EdgeInsets.all(15.0),
        child: TextField(
          controller: _controller,
          decoration: InputDecoration(
              filled: true,
              labelText: _label,
              suffix: GestureDetector(child: Icon(Icons.close),
                onTap: (){
                  _controller.clear();
                },)
          ),
        )

    );
  }

}