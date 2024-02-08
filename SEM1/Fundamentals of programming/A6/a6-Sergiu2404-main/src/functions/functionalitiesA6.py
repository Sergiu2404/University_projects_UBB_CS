def get_day(transaction):
    return transaction["day"]
def set_day(transaction,day):
    transaction["day"]=day
def get_value(transaction):
    return transaction["value"]
def set_value(transaction,value):
    transaction["value"]=value
def get_type(transaction):
    return transaction["type"]
def set_type(transaction,type):
    transaction["type"]=type
def get_description(transaction):
    return transaction["description"]
def set_description(transaction,description):
    transaction["description"]=description
def create_dict(day,value,type,description):
    return {"day":day,"value":value,"type":type,"description":description}

def opp_add(list_transactions,day):
    list_transactions[day-1].pop()
def opp_insert(list_transactions,day):
    list_transactions[day-1].pop()
def opp_remove_day(list__transactions,day):
    pass
#########
def add_transaction(list_transactions,day,value,type,description):
    '''add transaction to initial list'''
    list_transactions[day-1].append({"day":day,"value":value,"type":type,"description":description})
def insert_transaction(list_transactions,day,value,type,description):
    '''insert transaction to initial list'''
    list_transactions[day-1].append({"day":day,"value":value,"type":type,"description":description})
def remove_type(list_transactions,type):
    '''remove transaction of type'''
    for day in list_transactions:
        for transaction in day:
            if get_type(transaction)==type:
                day.pop(-1)
def remove_day(list_transactions,day):
    '''remove transactions from day'''
    while list_transactions[day-1]!=[]:
        list_transactions[day-1].pop()
def remove_day_day(list_transactions,day1,day2):
    for day in range(day1,day2+1):
        while(list_transactions[day-1]!=[]):
            list_transactions[day-1].pop()
def replace(list_transactions,day,type,description,value):
    for transaction in list_transactions[day-1]:
        if get_type(transaction)==type:
            if get_description(transaction)==description:
                set_value(transaction,value)
def list_type(list_transactions,type):
    for day in list_transactions:
        for transaction in day:
            if get_type(transaction).lower()==type:
                print(transaction)
def list_cmp(list_transactions,sign,value):
    for day in list_transactions:
        for transaction in day:
            if sign==">":
                if get_value(transaction)>value:
                    print(transaction)
            elif sign=="=":
                if get_value(transaction)==value:
                    print(transaction)
            elif sign=="<":
                if get_value(transaction)<value:
                    print(transaction)
def list_balance(list_transactions,day):
    sumin=0
    sumout=0
    i=0
    while i<day:
        for transaction in list_transactions[i]:
            if get_type(transaction).lower()=="in":
                sumin+=int(get_value(transaction))
            elif get_type(transaction).lower()=="out":
                sumout+=int(get_value(transaction))
        i+=1
    print(sumin-sumout)
def filter_type(list_transactions,type):
    for day in list_transactions:
        for transaction in day:
            if get_type(transaction).lower()!=type:
                day.pop(-1)
def filter_type_value(list_transactions,type,value):
    for day in list_transactions:
        for transaction in day:
            if get_type(transaction).lower()!=type or get_value(transaction)>=value:
                day.pop(-1)




# def get_day(transaction):
#     return transaction["day"]
# def set_day(transaction,day):
#     transaction["day"]=day
# def get_value(transaction):
#     return transaction["value"]
# def set_value(transaction,value):
#     transaction["value"]=value
# def get_type(transaction):
#     return transaction["type"]
# def set_type(transaction,type):
#     transaction["type"]=type
# def get_description(transaction):
#     return transaction["description"]
# def set_description(transaction,description):
#     transaction["description"]=description
# def create_dict(day,value,type,description):
#     return {"day":day,"value":value,"type":type,"description":description}

# def add_transaction(list_transactions,day,value,type,description):
#     list_transactions[day-1].append({"day":day,"value":value,"type":type,"description":description})
# def insert_transaction(list_transactions,day,value,type,description):
#     list_transactions[day-1].append({"day":day,"value":value,"type":type,"description":description})
# def remove_type(list_transactions,type):
#     for day in list_transactions:
#         for transaction in day:
#             if get_type(transaction)==type:
#                 day.pop(-1)
# def remove_day(list_transactions,day):
#     while list_transactions[day-1]!=[]:
#         list_transactions[day-1].pop()
# def remove_day_day(list_transactions,day1,day2):
#     for day in range(day1,day2+1):
#         while(list_transactions[day-1]!=[]):
#             list_transactions[day-1].pop()
# def replace(list_transactions,day,type,description,value):
#     for transaction in list_transactions[day-1]:
#         if get_type(transaction)==type:
#             if get_description(transaction)==description:
#                 set_value(transaction,value)
# def list_type(list_transactions,type):
#     for day in list_transactions:
#         for transaction in day:
#             if get_type(transaction).lower()==type:
#                 print(transaction)
# def list_cmp(list_transactions,sign,value):
#     for day in list_transactions:
#         for transaction in day:
#             if sign==">":
#                 if get_value(transaction)>value:
#                     print(transaction)
#             elif sign=="=":
#                 if get_value(transaction)==value:
#                     print(transaction)
#             elif sign=="<":
#                 if get_value(transaction)<value:
#                     print(transaction)
# def list_balance(list_transactions,day):
#     sumin=0
#     sumout=0
#     i=0
#     while i<day:
#         for transaction in list_transactions[i]:
#             if get_type(transaction).lower()=="in":
#                 sumin+=int(get_value(transaction))
#             elif get_type(transaction).lower()=="out":
#                 sumout+=int(get_value(transaction))
#         i+=1
#     print(sumin-sumout)
# def filter_type(list_transactions,type):
#     for day in list_transactions:
#         for transaction in day:
#             if get_type(transaction).lower()!=type:
#                 day.pop(-1)

# def filter_type_value(list_transactions,type,value):
#     for day in list_transactions:
#         for transaction in day:
#             if get_type(transaction).lower()!=type or get_value(transaction)>=value:
#                 day.pop(-1)
