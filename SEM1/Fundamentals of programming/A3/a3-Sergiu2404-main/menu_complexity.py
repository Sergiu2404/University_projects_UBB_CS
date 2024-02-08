import random
import time

def menu():
    print("MENU")
    print("Welcome! Please select an option from the next four: ")
    print("-------------------------------------")
    print("1-generate list of numbers")
    print("2-use cocktail sort")
    print("3-use gnome sort")
    print("5-best case(time)")
    print("6-worst case(time)")
    print("7average case(time)")
    print("4-exit")
    print("-------------------------------------")
    print("MENU")

def list_generator(length):
    list=[]
    for i in range(length):
        list.append(random.randint(0,20))
    print(list)
    return list
#SORTING AL----------------------------------------------------
def cocktail_sort(list):
    start=0
    end=len(list)
    end-=1
    swaped= True #for verifying if list is sorted
    while swaped:

        swaped = False
            


        for i in range(start, end): #normal loop
            if list[i] > list[i + 1]:
                aux = list[i + 1]
                list[i + 1] = list[i]
                list[i] = aux
                swaped = True
        end-=1 #because we already know the biggest element
        for i in range(end, -1,-1): #backwards loop
            if list[i] > list[i + 1]:
                aux = list[i + 1]
                list[i + 1] = list[i]
                list[i] = aux
                swaped = True
        start=+1
    print("SORTED LIST: ")
    print(list)
def gnome_sort(list):
    i=1 #compares with the previous element
    step_counter=0
    while i<=len(list)-1:
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
    print("SORTED LIST: ")
    print(list)

# SORTING AL COMPLEXITY----------------------------------------
def cocktail_sort_complexity(list,average):
    start_time1=time.time()
    start=0
    end=len(list)
    end-=1
    swaped= True #for verifying if list is sorted
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
    end1=time.time()
    if average==False:
        print(f'{end1-start_time1} seconds for cocktail sort method')
    else:
        result1=end1-start_time1
        return result1
def gnome_sort_complexity(list,average):
    start_time2=time.time()
    i=1 #compares with the previous element
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
    if average==False:
        print(f'{time.time()-start_time2} seconds for gnome sort method')
    else:
        result2=time.time()-start_time2
        return result2

#------------------------------------------------


def menu_loop(list,lists_array,average):
    menu()
    option = input("please give option number: ")
    if option == "1":
        length=int(input("please enter list length: "))
        print("random list generated: ")
        list=list_generator(length)
    elif option == "2":
        print(">>>cocktail sort method selected<<<")
        if list==[]:
            print("ERROR")
            print("YOUR LIST IS EMPTY! PRESS 1 FOR GENERATING A LIST")
            print("ERROR")
        else:
            cocktail_sort(list)
            print("-------------------------------------")
    elif option == "3":
        print(">>>gnome sort method selected<<<")
        if list==[]:
            print("ERROR")
            print("YOUR LIST IS EMPTY! PRESS 1 FOR GENERATING A LIST")
            print("ERROR")
        else:
            gnome_sort(list)
            print("-------------------------------------")
    elif option=="4":
        exit()
    elif option=="5":
        average=False
        print("best case COCKTAIL SORT(when list is sorted reverse):")
        print("for 250")
        cocktail_sort_complexity(sorted(lists_array[0]),average)
        print("for 500")
        cocktail_sort_complexity(sorted(lists_array[1]),average)
        print("for 1000")
        cocktail_sort_complexity(sorted(lists_array[2]),average)
        print("for 2000")
        cocktail_sort_complexity(sorted(lists_array[3]),average)
        print("for 4000")
        cocktail_sort_complexity(sorted(lists_array[4]),average)
        print("best case GNOME SORT(when list is sorted reverse):")
        print("for 250")
        gnome_sort_complexity(sorted(lists_array[0]),average)
        print("for 500")
        gnome_sort_complexity(sorted(lists_array[1]),average)
        print("for 1000")
        gnome_sort_complexity(sorted(lists_array[2]),average)
        print("for 2000")
        gnome_sort_complexity(sorted(lists_array[3]),average)
        print("for 4000")
        gnome_sort_complexity(sorted(lists_array[4]),average)
    elif option=="6":
        average=False
        print("worst case COCKTAIL SORT(when list is sorted reverse):")
        print("for 250")
        cocktail_sort_complexity(sorted(lists_array[0],reverse=True),average)
        print("for 500")
        cocktail_sort_complexity(sorted(lists_array[1],reverse=True),average)
        print("for 1000")
        cocktail_sort_complexity(sorted(lists_array[2],reverse=True),average)
        print("for 2000")
        cocktail_sort_complexity(sorted(lists_array[3],reverse=True),average)
        print("for 4000")
        cocktail_sort_complexity(sorted(lists_array[4],reverse=True),average)
        print("worst case GNOME SORT(when list is sorted reverse):")
        print("for 250")
        gnome_sort_complexity(sorted(lists_array[0],reverse=True),average)
        print("for 500")
        gnome_sort_complexity(sorted(lists_array[1],reverse=True),average)
        print("for 1000")
        gnome_sort_complexity(sorted(lists_array[2],reverse=True),average)
        print("for 2000")
        gnome_sort_complexity(sorted(lists_array[3],reverse=True),average)
        print("for 4000")
        gnome_sort_complexity(sorted(lists_array[4],reverse=True),average)
    elif option=="7":
        average=True
        print("average case for COCKTAIL SORT:")
        print("for 250")
        print(cocktail_sort_complexity(sorted(lists_array[0],reverse=True),average)+cocktail_sort_complexity(sorted(lists_array[0]),average))
        print("for 500")
        print(cocktail_sort_complexity(sorted(lists_array[1],reverse=True),average)+cocktail_sort_complexity(sorted(lists_array[1]),average))
        print("for 1000")
        print(cocktail_sort_complexity(sorted(lists_array[2],reverse=True),average)+cocktail_sort_complexity(sorted(lists_array[2]),average))
        print("for 2000")
        print(cocktail_sort_complexity(sorted(lists_array[3],reverse=True),average)+cocktail_sort_complexity(sorted(lists_array[3]),average))
        print("for 4000")
        print(cocktail_sort_complexity(sorted(lists_array[4],reverse=True),average)+cocktail_sort_complexity(sorted(lists_array[4]),average))
        print("average case for GNOME SORT:")
        print("for 250")
        print(gnome_sort_complexity(sorted(lists_array[0],reverse=True),average)+gnome_sort_complexity(sorted(lists_array[0]),average))
        print("for 500")
        print(gnome_sort_complexity(sorted(lists_array[1],reverse=True),average)+gnome_sort_complexity(sorted(lists_array[1]),average))
        print("for 1000")
        print(gnome_sort_complexity(sorted(lists_array[2],reverse=True),average)+gnome_sort_complexity(sorted(lists_array[2]),average))
        print("for 2000")
        print(gnome_sort_complexity(sorted(lists_array[3],reverse=True),average)+gnome_sort_complexity(sorted(lists_array[3]),average))
        print("for 4000")
        print(gnome_sort_complexity(sorted(lists_array[4],reverse=True),average)+gnome_sort_complexity(sorted(lists_array[4]),average))
    else:
        print("Invalid option! Mai baga o fisa ")
    menu_loop(list,lists_array,average)

if __name__ == '__main__':
    list = [] 
    average=True
    length=0
    list1=[]
    list2=[]
    list3=[]
    list4=[]
    list5=[]
    for i in range(0,4000):
        list5.append(random.randint(0,1001))
    list4[0:2000]=list5[0:2000]
    list3[0:1000]=list5[0:1000]
    list2[0:500]=list5[0:500]
    list1[0:250]=list5[0:250]
    lists_array=[list1,list2,list3,list4,list5]
    menu_loop(list,lists_array,average)           





