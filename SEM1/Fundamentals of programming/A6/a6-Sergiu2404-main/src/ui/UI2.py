from src.functions.functionalitiesA6 import *
import copy


def menu():
    print("MENU")
    print("Welcome! Please select an option from the next: ")
    print("-------------------------------------")
    print("--INSTRUCTIONS--")
    print("    value=integer")
    print("    type=in/out")
    print("    description=any string")
    print("    !!!IMPORTANT: SPACE BETWEEN ALL THE INSTRUCTIONS")
    print("--INSTRUCTIONS--")
    print("add <value> <type> <description>")
    print("insert <day> <value> <type> <description>")
    print("remove <day>")
    print("remove <start day> to <end day>")
    print("remove <type>")
    print("replace <day> <type> <description> with <value>")
    print("list")
    print("list <type>")
    print("list [ < | = | > ] <value>")
    print("list balance <day>")
    print("filter <type>")
    print("filter <type> <value>")
    print("undo")
    print("exit-for closing the program")
    print("-------------------------------------")
    print("MENU")


# def create_transaction(day,value,type,description):
#     return {"day":day,"value":value,"type":type,"description":description}


def menu_loop(list_transactions,currDay,undo_list,operation_list):
    menu()
    option=input("GIVE AN OPTION: ")
    option_input=option.split() #list with option words
    initial = copy.deepcopy(list_transactions)
    if option_input[0].lower()=="add": #ADD
        if (option_input[2]=="in" or option_input[2]=="out") and option_input[1].isnumeric():
            if currDay!=-1:
                add_transaction(list_transactions,int(currDay),int(option_input[1]),option_input[2],option_input[3])
                operation_list.append("add")
                if initial != list_transactions:
                    undo_list.append(copy.deepcopy(list_transactions))
            else:
                print("Insert a day firstly!!!")
        else:
            print("Invalid command. Try:add <value> <type> <description>")
    elif option_input[0].lower() == "insert": #INSERT
        try:
            option_input[2].isnumeric()==True
        except:
            print("not a number")
        if 0<=int(option_input[1])<=30 and (option_input[3].lower()=="in" or option_input[3].lower()=="out") and option_input[2].isnumeric():
            if list_transactions[int(option_input[1])-1]!=[] or len(option_input)<5:
                print("You already have a transaction inserted in this day!!! You only have to use add")
            else:
                currDay = int(option_input[1])
                insert_transaction(list_transactions,int(currDay) , int(option_input[2]), option_input[3], option_input[4])
                operation_list.append("insert")
                if initial != list_transactions:
                    undo_list.append(copy.deepcopy(list_transactions))
        else:
            #
            print("not a number")
            print("invalid command. Try:insert <day> <value> <type> <description>")
    elif option_input[0].lower() == "remove":#REMOVE
        if len(option_input)==2:
            if option_input[1].lower()=="in" or option_input[1].lower()=="out":
                remove_type(list_transactions,option_input[1])
                if initial != list_transactions:
                    undo_list.append(copy.deepcopy(list_transactions))
            elif 0<=int(option_input[1])<=30:
                remove_day(list_transactions,int(option_input[1]))
                operation_list.append("remove")
                if initial != list_transactions:
                    undo_list.append(copy.deepcopy(list_transactions))
            else:
                print("Invalid command. Try:remove <day> OR remove <type>")
        elif len(option_input) == 3:
            if 0<=int(option_input[1])<=30 and 0<=int(option_input[2])<=30:
                remove_day_day(list_transactions,int(option_input[1]),int(option_input[2]))
                if initial != list_transactions:
                    undo_list.append(copy.deepcopy(list_transactions))
            else:
                print("Invalid command. Try:remove <start day> to <end day>")
    elif option_input[0].lower() == "replace":#REPLACE
        try:
            option_input[2].isnumeric()==True
        except:
            print("not a number")
        if option_input[4].lower()=="with" and 0<=int(option_input[1])<=30 and (option_input[2].lower()=="in" or option_input[2].lower()=="out"): 
            if option_input[3].isnumeric():
                replace(list_transactions,int(option_input[1]),option_input[2],option_input[3],int(option_input[5]))
                operation_list.append("replace")
                if initial != list_transactions:
                    undo_list.append(copy.deepcopy(list_transactions))
            else:
                print("not a number")
                print("invalid command. You should enter an integer for the value")
        else:
            print("Invalid command. Try:replace <day> <type> <description> with <value>. You have to use --with--")
    elif option_input[0].lower() == "list":#LIST
        if len(option_input)==1:
            print(list_transactions)
        elif len(option_input)==2:
            if option_input[1].lower()=="in" or option_input[1].lower()=="out":
                list_type(list_transactions,option_input[1])
            else:
                print("Invalid command. Try:list <type>")
        elif len(option_input)==3:
            if int(option_input[2].isnumeric()):
                if option_input[1].lower()=="balance":
                    list_balance(list_transactions,int(option_input[2]))
                elif option_input[1]==">" or option_input[1]=="=" or option_input[1]=="<":
                    list_cmp(list_transactions,option_input[1],int(option_input[2]))
                else:
                    print("invalid command. Try:list </=/> <value> or ")
            else:
                print("Invalid command. You should enter an integer for the value")
    elif option_input[0].lower() == "filter":#FILTER
        if len(option_input)==2:
            if option_input[1].lower()=="in" or option_input[1].lower()=="out":
                filter_type(list_transactions,option_input[1])
                operation_list.append("filter_type")
                if initial != list_transactions:
                    undo_list.append(copy.deepcopy(list_transactions))
            else:
                print("Invalid command. Try:filter <type>")
        elif len(option_input)==3:
            if option_input[1].lower()=="in" or option_input[1].lower()=="out":
                if int(option_input[2].isnumeric()):
                    filter_type_value(list_transactions, option_input[1],int(option_input[2]))
                    operation_list.append("filter_type_value")
                else:
                    print("Invalid command. You should enter an integer for the value")
            else:
                print("Invalid command. Try:filter <type> <value>")
    elif option_input[0].lower()=="exit":#EXIT
        exit()
    elif option_input[0].lower()=="undo":#UNDO
        if len(undo_list)>=2:
            if operation_list[-1]=="add":
                opp_add(list_transactions,currDay)
            elif operation_list[-1]=="insert":
                opp_insert(list_transactions,currDay)
            # elif operation_list[-1]=="remove":
            #     list_transactions=transaction_remove.copy()
            # elif operation_list[-1]=="replace":
            #     list_transactions=transaction_replace.copy()
            # elif operation_list[-1]=="filter_type":
            #     list_transactions=transaction_filter.copy()
            # elif operation_list[-1]=="filter_type_value":
            #     list_transactions=transaction_filter_value.copy()
            else:
                print("Error")
            list_transactions = copy.deepcopy(undo_list.pop())
            #print(undo_list[-1])
        else:
            print("No more operations in history")
    else:
         print("ERROR")
         print("Wrong option chosen. Mai baga o fisa")
         print("ERROR")
    print(list_transactions)
    menu_loop(list_transactions,currDay,undo_list,operation_list)


