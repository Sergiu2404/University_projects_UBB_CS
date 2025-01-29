import 'dart:async';
import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../api/api.dart';
import '../model/entity.dart';
import '../util/message_response.dart';

class Category {
  String category;
  int totalTransactions;
  Category(this.category, this.totalTransactions);
}

class CategoriesPage extends StatefulWidget {
  final bool isOnline;

  CategoriesPage(this.isOnline);

  @override
  State<StatefulWidget> createState() => _CategoriesPage();
}

class _CategoriesPage extends State<CategoriesPage> {
  bool online = true;
  bool isLoading = false;

  late StreamSubscription<ConnectivityResult> subscription;

  List<Entry> allEntries = [];
  List<Category> topCategories = [];

  @override
  void initState() {
    super.initState();

    online = widget.isOnline;

    subscription = Connectivity().onConnectivityChanged.listen((result) {
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

  getTopCategories() async {
    var categories = allEntries.map((element) => element.category).toList();
    var transactionCounter = {};

    for (var element in categories) {
      transactionCounter[element] =
          (transactionCounter[element] ?? 0) + 1;
    }

    topCategories = transactionCounter.entries
        .map((entry) => Category(entry.key, entry.value))
        .toList();

    topCategories.sort((b, a) => a.totalTransactions.compareTo(b.totalTransactions));
    topCategories = topCategories.take(3).toList();
  }

  getData() async {
    setState(() => isLoading = true);

    if (online) {
      try {
        allEntries = await ApiService.instance.getAllEntries();
        getTopCategories();
      } catch (_) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("Server get failed")),
        );
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
        title: const Text("Top Categories"),
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
        itemCount: topCategories.length,
        itemBuilder: (context, index) {
          final category = topCategories[index];
          return ListTile(
            title: Text(category.category),
            subtitle: Text("Transactions: ${category.totalTransactions}"),
          );
        },
      ),
    );
  }

  @override
  void dispose() {
    subscription.cancel();
    super.dispose();
  }
}
