class Expense {
  int? _id;
  double _amount;
  String _type;
  String _category;
  String _date;
  String _note;

  Expense(this._amount, this._type, this._category, this._date, this._note);
  Expense.withId(this._id, this._amount, this._type, this._category, this._date, this._note);

  // Getters
  int? get id => _id;
  double get amount => _amount;
  String get type => _type;
  String get category => _category;
  String get date => _date;
  String get note => _note;

  // Setters
  set amount(double newAmount) => _amount = newAmount;
  set type(String newType) => _type = newType;
  set category(String newCategory) => _category = newCategory;
  set date(String newDate) => _date = newDate;
  set note(String newNote) => _note = newNote;

  // Convert expense into map object for DB compatibility
  Map<String, dynamic> toMap() {
    var map = <String, dynamic>{};
    if (id != null) {
      map['id'] = _id;
    }
    map['amount'] = _amount;
    map['type'] = _type;
    map['category'] = _category;
    map['date'] = _date;
    map['note'] = _note;
    return map;
  }

  void fromMap(Map<String, dynamic> map) {
    _id = map['id'];
    _amount = map['amount'];
    _type = map['type'];
    _category = map['category'];
    _date = map['date'];
    _note = map['note'];
  }
}
