import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:client/repo/APIRepo.dart';
import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../api/api.dart';
import '../model/entity.dart';
import '../util/message_response.dart';
import 'package:week_of_year/week_of_year.dart';
import 'package:intl/intl.dart';

class GenrePair<First, Second>{
  First first;
  Second second;

  GenrePair(this.first, this.second);
}

class YearMovies {
  int year;
  int movie_count;

  YearMovies(this.year, this.movie_count);
}


class ReleaseYearSection extends StatefulWidget {
  final bool isOnline;

  ReleaseYearSection(this.isOnline);

  @override
  State<StatefulWidget> createState() => _ReleaseYearSection();
}

class _ReleaseYearSection extends State<ReleaseYearSection> {
  bool online = true;
  bool isLoading = false;

  late StreamSubscription<ConnectivityResult> subscription;

  List<YearMovies> yearMovies = [];
  //List<Symptom> symptoms = [];
  List<GenrePair<String, int>> topGenres = [];

  @override
  void initState() {
    super.initState();

    online = widget.isOnline;

    subscription = Connectivity()
        .onConnectivityChanged
        .listen((ConnectivityResult result) {
      if (result == ConnectivityResult.wifi ||
          result == ConnectivityResult.mobile) {
        online = true;
        getData();
      } else {
        online = false;
      }
    });

    Future.delayed(Duration.zero, () {
      getData();
    });
  }

  computeYearsMovies(List<Entry> allEntries) async {
    final yearCOunts = <int, int>{};

    for(var entry in allEntries)
    {
      final year = entry.year;
      yearCOunts[year] = (yearCOunts[year] ?? 0) + 1;
    }

    yearMovies = yearCOunts.entries
      .map((elem) => YearMovies(elem.key, elem.value))
      .toList()
      ..sort((b, a) => a.movie_count.compareTo(b.movie_count));
  }

  computeTopGenres(List<Entry> allEntries) async {
    var genreCounts = <String, int>{};

    for(var entry in allEntries)
      {
        genreCounts[entry.genre] = (genreCounts[entry.genre] ?? 0) + 1;
      }

    topGenres = genreCounts.entries
      .map((elem) => GenrePair(elem.key, elem.value))
      .toList()
      ..sort((b, a) => a.second.compareTo(b.second));

    topGenres = topGenres.take(3).toList();
  }

  getData() async {
    setState(() => isLoading = true);

    if (online == true) {
      try {
        List<Entry> allEntries = await APIRepo.instance.getAllEntries();
        computeYearsMovies(allEntries);
        computeTopGenres(allEntries);
      } on Exception catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Server get failed"),
        ));
      }
    } else {
      messageResponse(context, "Not available offline");
    }

    setState(() => isLoading = false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Release Year Section"),
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : Row(
        children: [
          Flexible(
            child: Container(
              color: Colors.blueGrey[50],
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Padding(
                    padding: EdgeInsets.all(8.0),
                    child: Text(
                      "Movies by Release Year",
                      style: TextStyle(
                          fontSize: 18, fontWeight: FontWeight.bold),
                    ),
                  ),
                  Expanded(
                    child: ListView.builder(
                      itemCount: yearMovies.length,
                      itemBuilder: (context, index) {
                        final yearMovie = yearMovies[index];
                        return ListTile(
                          title: Text("Year: ${yearMovie.year}"),
                          subtitle: Text(
                              "Movies Released: ${yearMovie.movie_count}"),
                        );
                      },
                    ),
                  ),
                ],
              ),
            ),
          ),
          Flexible(
            child: Container(
              color: Colors.lightGreen[50],
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Padding(
                    padding: EdgeInsets.all(8.0),
                    child: Text(
                      "Top 3 Genres",
                      style: TextStyle(
                          fontSize: 18, fontWeight: FontWeight.bold),
                    ),
                  ),
                  Expanded(
                    child: ListView.builder(
                      itemCount: topGenres.length,
                      itemBuilder: (context, index) {
                        final genre = topGenres[index];
                        return ListTile(
                          title: Text("Genre: ${genre.first}"),
                          subtitle:
                          Text("Movies: ${genre.second.toString()}"),
                        );
                      },
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  @override
  void dispose() {
    subscription.cancel();
    super.dispose();
  }
}