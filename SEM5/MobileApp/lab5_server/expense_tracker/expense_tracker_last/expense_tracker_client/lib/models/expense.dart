class Expense{
  late int id;
  double amount;
  String type;
  String date;
  String note;

  Expense(this.amount, this.type, this.date, this.note);

  Map<String, dynamic> toMapWithId(){
    return {
      'id': id,
      'amount': amount,
      'type': type,
      'date': date,
      'note': note
    };
  }

  Map<String, dynamic> toMap(){
    return {
      'amount':amount,
      'type': type,
      'date': date,
      'note': note
    };
  }

  factory Expense.fromMap(Map<String, dynamic> map){
    var expense = Expense(map['amount'], map['type'], map['date'], map['note']);
    expense.id = map['id'];
    return expense;
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'amount': amount,
      'type': type,
      'date': date,
      'note': note
    };
  }

  factory Expense.fromJson(Map<String, dynamic> map) {
    var expense = Expense(map['amount'], map['type'], map['date'], map['note']);
    expense.id = map['id'];
    return expense;
  }
}