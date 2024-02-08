import numpy as np

def printEqualSumSets(list, length,s):
    dp = np.zeros((length + 1, s + 1)) #matrix: True if there is a subset in first i elem that can equal sum j
    for i in range(1, s + 1): #pentru 0 elemente, nu exista suma
        dp[0][i] = False
    for i in range(length + 1): #suma 0 obtinuta fara elemente din i
        dp[i][0] = True
    for i in range(1, length + 1):
        for currSum in range(1, s + 1):
            dp[i][currSum] = dp[i - 1][currSum] #fara elementu curent
            if (list[i - 1] <= currSum): #cu elementu curent
                dp[i][currSum] = (dp[i][currSum] or dp[i - 1][currSum - list[i - 1]])
    set1=[]
    set2=[]
    if (not dp[length][s]):
        return -1 #nu se pot forma subseturi
    i = length
    currSum = s
    while (i > 0 and currSum >= 0): #starting from last elem
        if (dp[i - 1][currSum]): #daca elementul nu ajuta la suma k
            i -= 1
            set2.append(list[i])
        elif (dp[i - 1][currSum - list[i - 1]]): #daca elementul ajuta la suma k
            i -= 1
            currSum -= list[i]
            set1.append(list[i])
    set1 = sorted(set1)
    set2 = sorted(set2)
    print("Set 1 elements:",)
    print(set1)
    print("\nSet 2 elements:",)
    print(set2)
if __name__== '__main__':
    partitions=[]
    s=0
    n=int(input("Number of elements in set A: "))
    i=1
    list=[]
    while i<=n:
        element=int(input("give an element: "))
        list.append(element)
        i+=1
    list.sort()
    length=len(list)
    for element in list:
        s=s+element
    if s%2==1:
        print("The set can not be partitioned into 2 subsets of equal sum because of the odd sum")
    elif printEqualSumSets(list, length,s//2)==-1: #the maximum sum of the 2 subsets is sum/2
        print("The set can not be partitioned into 2 subsets of equal sum")
    else:
        printEqualSumSets(list, length,s//2) #the maximum sum of the 2 subsets is sum/2
