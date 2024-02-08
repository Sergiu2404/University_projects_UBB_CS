def coliniarity_verification(array1,array2,array3):
    if array2[0]*array3[1]+array3[0]*array1[1]+array1[0]*array2[1]-(array1[1]*array2[0]+array2[1]*array3[0]+array1[0]*array3[1])==0:
        #determinant
        return True
    return False
def slope(array1,array2):
    slope=0
    x1=float(array1[0])
    x2=float(array2[0])
    y1=float(array1[1])
    y2=float(array2[1])
    upper=y2-y1
    lower=x2-x1
    if lower!=0:
        slope=upper/lower
    return slope
def check(pointlist):
    length = len(pointlist)
    if length<3: #it needs at least 3 points, so it adds anyway the first 2 points to the list
        return 1
    if slope(pointlist[0],pointlist[1])!=slope(pointlist[0], pointlist[length-1]):
        return 0
    return 1

if __name__ == '__main__':
    coliniarPointsList=[]
    coliniarPoints=[]
    listOfPoints=[] #listOfPoints=[array1,array2,...]
    n=int(input("enter number of points:"))
    for i in range(0,n):
        x=int(input("Enter X: "))
        y=int(input("Enter Y: "))
        listOfPoints.append([x,y])
    for i in range(0,len(listOfPoints)):
        coliniarPoints.append(listOfPoints[i])
        for j in range(i+1, len(listOfPoints)):
            coliniarPoints.append(listOfPoints[j])
            for k in range(j+1,len(listOfPoints)):
                if slope(listOfPoints[i],listOfPoints[j])==slope(listOfPoints[j],listOfPoints[k]):
                    coliniarPoints.append(listOfPoints[k])
                    if len(coliniarPoints)>=3:
                        print(coliniarPoints)
            coliniarPoints=[]
                    