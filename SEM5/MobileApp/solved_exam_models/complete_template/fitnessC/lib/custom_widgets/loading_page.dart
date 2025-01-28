import 'package:flutter/material.dart';

import 'custom_circular_progress.dart';


class LoadingPage extends StatelessWidget {
  const LoadingPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          Center(
            child: Center(
              child: AnimatedCircularProgress(
                size: 70.0, // Custom size
                strokeWidth: 6.0, // Custom stroke width
              ),
            )
            // CustomCircularProgress(
            //   size: 70.0, // Custom size
            //   color: Colors.red, // Custom color
            //   strokeWidth: 6.0, // Custom stroke width
            // ),
          )

        ],
      ),
    );
  }
}
