class Expense:
    def __init__(self, id, amount: float, type: str, date: str, note: str):
        self.id = id
        self.amount = amount
        self.type = type
        self.date = date
        self.note = note


    def as_dict(self):
        return {
            "id": self.id,
            "amount": self.amount,
            "type": self.type,
            "date": self.date,
            "note": self.note
        }