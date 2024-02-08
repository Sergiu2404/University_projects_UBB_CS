import random

def menu():
    print("MENU")
    print("Welcome! Please select an option from the next four: ")
    print("-------------------------------------")
    print("1-generate list of numbers")
    print("2-use cocktail sort")
    print("3-use gnome sort")
    print("4-exit")
    print("-------------------------------------")
    print("MENU")

def list_generator():
    list=[]
    length=random.randint(0,10)
    for i in range(length):
        list.append(random.randint(0,20))
    print(list)
    return list

# SORTING AL----------------------------------------
def cocktail_sort(list,steps):
    start=0
    end=len(list)
    end-=1
    swaped= True #for verifying if list is sorted
    step_counter= 0
    while swaped:

        swaped = False
            


        for i in range(start, end):
            if list[i] > list[i + 1]:
                aux = list[i + 1]
                list[i + 1] = list[i]
                list[i] = aux
                swaped = True
        
        end-=1 #because we already know the biggest element
        for i in range(end, -1,-1): #backwards loop-
            if list[i] > list[i + 1]:
                aux = list[i + 1]
                list[i + 1] = list[i]
                list[i] = aux
                swaped = True
        start=+1
        step_counter += 1
        if step_counter % steps == 0 :
            print(list)
    print("SORTED LIST: ")
    print(list)
def gnome_sort(list,steps):
    i=1 #compares with the previous element
    step_counter=0
    while i<len(list)-1:
        # 3 1 2
        # 1 3 2
        if i == 0:
            i = i + 1
        if list[i] >= list[i - 1]:  # then make step to the right
            i += 1
        else:  # then make step to the left
            list[i], list[i-1] = list[i-1], list[i]
            i -= 1
        
        
        step_counter+=1
        if step_counter%steps==0:
            print(list)
    print("SORTED LIST: ")
    print(list)
#------------------------------------------------

def menu_loop(list):
    menu()
    option = input("please give option number: ")
    
    if option == "1":
        print("random list generated: ")
        list=list_generator()
    elif option == "2":
        print(">>>cocktail sort method selected<<<")
        if list==[]:
            print("ERROR")
            print("YOUR LIST IS EMPTY! PRESS 1 FOR GENERATING A LIST")
            print("ERROR")
        else:
            steps=int(input(" please give a number of steps to see the sorting process: "))
            cocktail_sort(list,steps)
            print("-------------------------------------")
    elif option == "3":
        print(">>>gnome sort method selected<<<")
        if list==[]:
            print("ERROR")
            print("YOUR LIST IS EMPTY! PRESS 1 FOR GENERATING A LIST")
            print("ERROR")
        else:
            steps=int(input(" please give a number of steps to see the sorting process: "))
            gnome_sort(list,steps)
            print("-------------------------------------")
    elif option=="4":
        exit()
    else:
        print("Invalid option! Mai baga o fisa ")
    menu_loop(list)

if __name__ == '__main__':
    list = [] 
    menu_loop(list)           





