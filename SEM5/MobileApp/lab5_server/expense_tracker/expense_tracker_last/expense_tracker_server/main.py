import asyncio
import json
from flask_cors import CORS
from threading import Thread

import websockets
from flask import Flask, request

from db_utils import *

app = Flask(__name__)

debug = True

CORS(app)

CLIENTS = set()


@app.route("/", methods=['GET'])
def check():
    return "SUCCESS"


@app.route("/expenses", methods=['GET'])
def get_expenses():
    conn = sql_connection()
    expenses = get_expenses_db(conn)
    conn.close()

    expenses_json = [expense.as_dict() for expense in expenses]

    return json.dumps({'expenses': expenses_json})


@app.route("/expense/sync", methods=['POST'])
def expenses_sync():
    conn = sql_connection()
    expenses = get_expenses_db(conn)

    expenses_json = json.loads(request.data)

    print(expenses_json)

    for expense_id in json.loads(expenses_json['deleted_ids']):
        print(expense_id)
        delete_expense_db(conn, expense_id)

    for expense_json in json.loads(expenses_json['updated_expenses']):
        print(expense_json)
        update_expense_db(conn, Expense(int(expense_json["id"]), expense_json["amount"], expense_json["type"], expense_json["date"], expense_json["note"]))

    expenses_to_add = []
    for expense_json in json.loads(expenses_json['expenses']):
        found = False
        for expense in expenses:
            if expense_json['amount'] == expense.amount \
                    and expense_json['type'] == expense.type \
                    and expense_json['date'] == expense.date \
                    and expense_json['note'] == expense.note:
                found = True
            elif expense_json['id'] == expense.id:
                found = True
        if not found:
            expenses_to_add.append(
                Expense(expense_json['amount'], expense_json['type'], expense_json['date'], expense_json['note']))


    for expense in expenses_to_add:
        add_expense_db(conn, expense)

    expenses = get_expenses_db(conn)
    conn.close()

    expenses_json = [expense.as_dict() for expense in expenses]

    return json.dumps({'expenses': expenses_json})


@app.route("/expense/<id>", methods=['GET'])
def get_expense(id):
    conn = sql_connection()
    expense = get_expense_db(conn, id)
    conn.close()

    if expense is None:
        return "ERROR"

    return json.dumps(expense.as_dict())


@app.route("/expense", methods=['POST'])
def add_expense():
    request_data_str = request.data.decode("utf-8")
    expense_json = json.loads(request_data_str)
    conn = sql_connection()
    expense = add_expense_db(conn, Expense(0, expense_json['amount'], expense_json['type'], expense_json['date'], expense_json['note']))
    conn.close()

    expense_json['id'] = expense.id
    # Convert the updated data back to a JSON string
    updated_data_json_str = json.dumps(expense_json)

    # Broadcasting the new JSON string with the 'id'
    asyncio.run(broadcast("ADD$" + updated_data_json_str))

    return "SUCCESS"


@app.route("/expense", methods=['PUT'])
def update_expense():
    expense_json = json.loads(request.data)
    conn = sql_connection()
    update_expense_db(conn, Expense(int(expense_json['id']), expense_json['amount'], expense_json['type'], expense_json['date'], expense_json['note']))
    conn.close()

    asyncio.run(broadcast("UPDATE$" + str(request.data, "UTF-8")))

    return "SUCCESS"


@app.route("/expense/<id>", methods=['DELETE'])
def delete_expense(id):
    conn = sql_connection()
    delete_expense_db(conn, id)
    conn.close()

    asyncio.run(broadcast("DELETE$" + id))

    return "SUCCESS"


async def send(websocket, message):
    try:
        await websocket.send(message)
    except websockets.ConnectionClosed:
        CLIENTS.remove(websocket)
        pass


async def broadcast(message):
    for websocket in CLIENTS:
        asyncio.create_task(send(websocket, message))


async def echo(websocket):
    CLIENTS.add(websocket)
    try:
        # data = await websocket.recv()
        # print(data)

        await websocket.wait_closed()
    finally:
        CLIENTS.remove(websocket)


async def main_sockets():
    async with websockets.serve(echo, "0.0.0.0", 8765, ssl=None):
        await asyncio.Future()


def routine1():
    asyncio.run(main_sockets(), debug=False)


def routine2():
    app.run(host="0.0.0.0", port=5001, debug=False, threaded=True)


if __name__ == "__main__":
    print("ok")
    thread_web = Thread(target=routine1)
    thread_server = Thread(target=routine2)
    thread_web.start()
    thread_server.start()
    thread_web.join()
    thread_server.join()