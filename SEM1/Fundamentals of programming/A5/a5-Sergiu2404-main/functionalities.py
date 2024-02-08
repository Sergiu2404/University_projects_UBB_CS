def read_list_elements():
    list=[]
    length=int(input("give length of list:"))
    for i in range(length):
        real_part=int(input("give real part of the complex number: "))
        imaginary_part=int(input("give imaginary part of the complex number: "))
        list.append([real_part,imaginary_part]) #list of all the complex numbers--list=[number1,number2,...number[len(list)-1]]
    return list
def print_list(list):
    for i in range(len(list)):
        if list[i][1]<0:
            print(str(list[i][0])+str(list[i][1])+"i",) #a-bi
        elif list[i][1]==0: #imaginary part equal to 0
            print(str(list[i][0]),) #a-print only real part
        else:
            print(str(list[i][0])+"+"+str(list[i][1])+"i",) #a+bi
def property1(list):
    maxim_length=0
    solution=[]  #solution list of complex numbers
    if len(list)==1:
        return list
    for i in range(len(list)-1):
        current=[] #current solution list of complex numbers
        real=list[i][0]
        imaginary=list[i][1]
        current.append([real,imaginary])
        for j in range(i+1,len(list)):
            real2=list[j][0]
            imaginary2=list[j][1]
            if real!=real2 or imaginary!=imaginary2: #if they are distinct
                current.append([real2,imaginary2])
            else:
                if len(current)>=maxim_length:
                    maxim_length=len(current) #subsirul de lungime maxima
                    solution.clear()
                    solution.extend(current)#puts the content of current in solution  
    return solution #vector cu numere diferite
def property2(list):
    maxim_length=0
    solution=[]
    if len(list)==1:
        return list
    for i in range(len(list)): 
        current=[]
        current.append([list[i][0],list[i][1]]) #1 2 3 4 2 1 7 8
        j=i+1
        while(j<len(list) and list[j][0]>list[j-1][0]):
            current.append([list[j][0],list[j][1]])
            j+=1
        if len(current)>=maxim_length:
            maxim_length=len(current)
            solution.clear()
            solution.extend(current)
    return solution
