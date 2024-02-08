# Solve the problem from the first set here
#2
n=int(input("Give a nat. num: "))
def isPrime(num):
    if num<=1:
        return False
    if num==2:
        return True
    if num%2==0:
        return False
    for i in range(3,int(num/2)+1,2):
        if num%i==0:
            return False
    return True

def sum_of_prime_numbers(n):
    for p1 in range(1,n):
        if isPrime(p1) and isPrime(n-p1):
            print(p1,n-p1)
            break

sum_of_prime_numbers(n)
