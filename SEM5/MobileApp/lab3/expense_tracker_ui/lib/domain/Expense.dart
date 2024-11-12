class Expense {
  int id;
  double amount;
  String category;
  String date;
  String type;
  String notes;

  Expense({
    required this.id,
    required this.amount,
    required this.category,
    required this.date,
    required this.type,
    required this.notes,
  });

  @override
  String toString() {
    return 'Expense(id: $id, amount: $amount, category: $category, date: $date, type: $type, notes: $notes)';
  }
}