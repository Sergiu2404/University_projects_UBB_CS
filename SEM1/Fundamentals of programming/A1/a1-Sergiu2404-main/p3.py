# Solve the problem from the third set here
#14
n=int(input("enter a number: "))
def isPrime(num):
    if num<=1:
        return False
    if num==2:
        return True

    if num % 2 == 0:
        return False

    for i in range(3,int(num/2)+1,2):
        if num%i==0:
            return False
    return True

def determine_element(n):
    elements=[]
    
    num=1
    while n!=0:
        if num == 1:
            elements.append(num)
            n = n - 1
        if isPrime(num):
            elements.append(num)
            n = n - 1
        else:
            d = 2
            x = num
            while x > 1:
                if x % d == 0:
                    print_counter = d
                    while print_counter:
                        elements.append(d)
                        print_counter = print_counter - 1
                        n = n - 1 
                while (x % d == 0):
                    x = x // d                           
                d += 1
        num+=1
    print(elements)

determine_element(n)


