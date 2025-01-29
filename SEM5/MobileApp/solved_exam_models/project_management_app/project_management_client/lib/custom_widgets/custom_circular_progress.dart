import 'package:flutter/material.dart';

class CustomCircularProgress extends StatelessWidget {
  final double size;
  final Color color;
  final double strokeWidth;

  CustomCircularProgress({this.size = 50.0, this.color = Colors.blue, this.strokeWidth = 5.0});

  @override
  Widget build(BuildContext context) {
    return Center(
      child: SizedBox(
        width: size,
        height: size,
        child: CircularProgressIndicator(
          valueColor: AlwaysStoppedAnimation(color), // Custom color
          strokeWidth: strokeWidth, // Custom stroke width
          backgroundColor: Colors.grey.withOpacity(0.2), // Optional background color
        ),
      ),
    );
  }
}

class AnimatedCircularProgress extends StatefulWidget {
  final double size;
  final double strokeWidth;

  AnimatedCircularProgress({this.size = 50.0, this.strokeWidth = 5.0});

  @override
  _AnimatedCircularProgressState createState() =>
      _AnimatedCircularProgressState();
}

class _AnimatedCircularProgressState extends State<AnimatedCircularProgress>
    with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  late Animation<Color?> _colorAnimation;

  @override
  void initState() {
    super.initState();

    _controller = AnimationController(
      duration: Duration(seconds: 3),
      vsync: this,
    )..repeat();

    _colorAnimation = ColorTween(
      begin: Colors.blue,
      end: Colors.red,
    ).animate(
      CurvedAnimation(parent: _controller, curve: Curves.easeInOut),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: SizedBox(
        width: widget.size,
        height: widget.size,
        child: AnimatedBuilder(
          animation: _controller,
          builder: (context, child) {
            return Transform.rotate(
              angle: _controller.value * 2 * 3.14159, // Rotate the indicator
              child: CircularProgressIndicator(
                valueColor: AlwaysStoppedAnimation(_colorAnimation.value),
                strokeWidth: widget.strokeWidth,
                backgroundColor: Colors.grey.withOpacity(0.2),
              ),
            );
          },
        ),
      ),
    );
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }
}

