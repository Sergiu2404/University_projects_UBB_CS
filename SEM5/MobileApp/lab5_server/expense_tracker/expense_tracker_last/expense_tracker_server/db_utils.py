import logging
import sqlite3

from sqlite3 import Error, Connection

from expense import Expense

logger = logging.getLogger('debugLogger')
logger.setLevel(logging.DEBUG)

def sql_connection() -> Connection:
    try:
        conn = sqlite3.connect("expenses.db")
        create_expenses_table(conn)

        return conn
    except Error as e:
        logger.error(f"Connection to the database failed with error: {e}")
        exit(1)

def create_expenses_table(conn: Connection):
    curs = conn.cursor()

    curs.execute("""
        CREATE TABLE IF NOT EXISTS Expenses(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            amount REAL,
            type TEXT,
            date TEXT,
            note TEXT)
    """)

    conn.commit()


def get_expenses_db(conn: Connection) -> [Expense]:
    curs = conn.cursor()

    rows = curs.execute("""
        SELECT * FROM Expenses;
    """).fetchall()

    expenses = []
    for row in rows:
        expense = Expense(int(row[0]), float(row[1]), row[2], row[3], row[4])
        expenses.append(expense)

        logger.debug(
            f"get_expenses_db - Expense: {expense.id}, {expense.amount}, {expense.type}, {expense.date}, {expense.note}")

    return expenses


def get_expense_db(conn: Connection, id: int) -> Expense:
    curs = conn.cursor()

    rows = curs.execute(f"""
        SELECT * FROM Expenses WHERE id={id};
    """).fetchall()

    if len(rows) != 1:
        logger.debug(f"There are more instances with same id")

        return None

    else:
        expense = Expense(int(rows[0][0]), float(rows[0][1]), rows[0][2], rows[0][3], rows[0][4])
        logger.debug(
            f"get_expense_db - Expense: {expense.id}, {expense.amount}, {expense.type}, {expense.date}, {expense.note}")

        return expense


def add_expense_db(conn: Connection, expense: Expense):
    curs = conn.cursor()

    curs.execute(f"""
        INSERT INTO Expenses(amount, type, date, note) VALUES(
            {expense.amount},
            '{expense.type}',
            '{expense.date}',
            '{expense.note}'
        );
    """)

    logger.debug(f"add_expense_db - Expense: {expense.id}, {expense.amount}, {expense.type}, {expense.date}, {expense.note}")

    conn.commit()

    return expense


def update_expense_db(conn: Connection, other: Expense):
    curs = conn.cursor()

    curs.execute(f"""
        UPDATE Expenses
        SET
        amount = {other.amount},
        type = '{other.type}',
        date = '{other.date}',
        note = '{other.note}'
        WHERE id = {other.id};
    """)

    logger.debug(
        f"update_expense_db - Expense: {other.id}, {other.amount}, {other.type}, {other.date}, {other.note}")

    conn.commit()


def delete_expense_db(conn: Connection, expense_id: int):
    curs = conn.cursor()

    curs.execute(f"""
        DELETE FROM Expenses WHERE id={expense_id};
    """)

    logger.debug(f"delete_expense_db - Expense: {expense_id  }")

    conn.commit()