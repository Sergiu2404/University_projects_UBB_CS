def findSets(arr, n, set1, set2, sum1, sum2, pos) :
    if (pos == n) :
        if (sum1 == sum2) :
            set1=sorted(set1)
            set2=sorted(set2)
            print(set1)
            print(set2)
            return True
        else:
            return False    
 
    set1.append(arr[pos]) #adds the current elements to set1
    res = findSets(arr, n, set1, set2, sum1 + arr[pos], sum2, pos + 1) #recursive call after added the element to set1, adds it to the sum
    if res: #for set 1 being found, after resulting in an equal sum
      return res
    # If not then backtrack 
    set1.pop() #else remove current element from set1 and append it to set2
    set2.append(arr[pos])
 
    # Recursive call after including current elem to set2
    res= findSets(arr, n, set1, set2, sum1,
                     sum2 + arr[pos], pos + 1) #recursive call after current element added to set2 and adds it to the sum
    if not res:       
        set2.pop() #poped out if returns False
    return res
 
def isPartitionPoss(list, n) :
    sum=0
    for i in range(0, n):
        sum+=list[i]
    if (sum % 2 != 0): #can not be partitioned at odd sum
        return False
    set1 = []
    set2 = []
    return findSets(list, n, set1, set2, 0, 0, 0)
list=[]
length=int(input("input length of the set: "))
i=1
while i<=length:
    element=int(input("input element in set: "))
    list.append(element)
    i+=1
if (isPartitionPoss(list, length) == False):
    print("The set can not be partitioned")
    