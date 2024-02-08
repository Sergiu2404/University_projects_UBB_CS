# Solve the problem from the second set here
year=int(input("enter year: "))
day=int(input("enter day: "))
def calculate_date(year,day):
    counter=0
    months=["jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"]
    while day>31:
        if year%4==0:
            if counter==1:
                day=day-29
            elif counter%2==1 and counter<6:
                day=day-30
            elif counter%2==0 and counter<7:
                day=day-31
            elif counter%2==1 and counter>=7:
                day=day-31
            else:
                day=day-30
        else:
            if counter==1:
                day=day-28
            elif counter%2==1 and counter<6:
                day=day-30
            elif counter%2==0 and counter<7:
                day=day-31
            elif counter%2==1 and counter>=7:
                day=day-31
            else:
                day=day-30
        counter+=1
        if counter%12==0:
            year+=1
            counter=0
    if day==31 and counter in [1,3,5,8,10]:
        day=1
        counter+=1
    if day==0:
        day=1
    if year%4==0:
        if day>29 and counter==1:
            day=day-29
            counter+=1
    elif year%4!=0:
        if day>28 and counter==1:
            day=day-28
            counter+=1
    print(str(day)+" "+months[counter]+" "+ str(year))

calculate_date(year,day)
        


