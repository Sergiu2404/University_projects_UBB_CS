// ignore_for_file: non_constant_identifier_names

import 'package:flutter/material.dart';
import '../models/expense.dart';
import '../../repositories/expenses_sv_database_repository.dart';
import '../utils/pair.dart';
import 'dart:io';


class ExpensesService extends ChangeNotifier { //give current state to the widgets that use it
  final ExpensesSVDatabaseRepository _repository;

  ExpensesService(this._repository){
    _repository.onOnlineStatusChanged = (is_online) {
      notifyListeners();
    };
    _repository.onDataUpdated = (is_updated) {
      notifyListeners();
    };
  }

  bool get is_online => _repository.is_online;

  static Future<ExpensesService> init() async {
    var repository = await ExpensesSVDatabaseRepository.init();
    return ExpensesService(repository);
  }

  void add(double amount, String type, String date, String note) {
    _repository.add(Expense(amount, type, date, note)).then((_) {
      notifyListeners();
    });
  }

  void remove(int id) {
    _repository.remove(id).then((_) {
      notifyListeners();
    });
  }

  void update(int id, double amount, String type, String date, String note) {
    _repository.update(id, Expense(amount, type, date, note)).then((_) {
      notifyListeners();
    });
  }

  Future<Pair> getAllExpenses() async {
    return await _repository.getAll();
  }
}