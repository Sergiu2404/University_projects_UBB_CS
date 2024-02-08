from functionalities import *
def menu():
    print("MENU")
    print("Welcome! Please select an option from the next 4: ")
    print("-------------------------------------")
    print("1-read list of complex numbers")
    print("2-print the list of complex numbers given")
    print("3-properties")
    print("4-exit")
    print("-------------------------------------")
    print("MENU")
def submenu():
    print("SUBMENU")
    print("Welcome! Please select an option from the next 4: ")
    print("-------------------------------------")
    print("3a-Determine length and elements of a longest subarray of distinct numbers.")
    print("3b-Determine the length and elements of a longest increasing subsequence, when considering each number's real part")
    print("-------------------------------------")
    print("SUBMENU")
def menu_loop(list):
    menu()
    option=input("GIVE AN OPTION NUMBER: ")
    if option=="1":
        print("READ LIST OF COMPLEX NUMBERS")
        list=read_list_elements()
    elif option=="2":
        if len(list)==0:
            print("ERROR")
            print("Your list is empty. Please enter first option number 1 for reading a list")
            print("ERROR")
        else:
            print("Your list is: ")
            print_list(list)
    elif option=="3":
        submenu()
        elements_prop1=[]
        elements_prop2=[]
        sub_option=input("GIVE OPTION: ")
        if sub_option=="3a":
            if len(list)==0:
                print("ERROR")
                print("Your list is empty. Please enter first option number 1 for reading a list")
                print("ERROR")
            else:
                print("length and elements of a longest subarray of distinct numbers: ")
                print(len(property1(list)))
                print_list(property1(list))
        elif sub_option=="3b":
            if len(list)==0:
                print("ERROR")
                print("Your list is empty. Please enter first option number 1 for reading a list")
                print("ERROR")
            else:
                print("the length and elements of a longest increasing subsequence, when considering each number's real part: ")
                print(len(property2(list)))
                print_list(property2(list))
        else:
            print("ERROR")
            print("Wrong option. Read the options from the submenu and try again more carefully!")
            print("ERROR")
    elif option=="4":
        exit()
    else:
        print("ERROR")
        print("Wrong option chosen. Read the options from the menu again!")
        print("ERROR")
    menu_loop(list)
if __name__=='__main__':
    list=[] 
    menu_loop(list)