from src.functions.functionalitiesA6 import *


def test_add(list,day,value,type,description):
    list[day-1].append({"day":day,"value":value,"type":type,"description":description})
    assert list==[[{"day":1,"value":2000,"type":"out","description":"pizza"},{"day":1,"value":1000,"type":"in","description":"salary"}],[],[]]
def test_insert(list,day,value,type,description):
    list[day-1].append({"day":day,"value":value,"type":type,"description":description})
    assert list==[[{"day":1,"value":2000,"type":"out","description":"pizza"}],[{"day":2,"value":2000,"type":"in","description":"job"}],[]]
def test_remove_day(list,day):
    while list[day-1]!=[]:
        list[day-1].pop()
    assert list==[[],[],[]]
def test_remove_day_day(list,day1,day2):
    for day in range(day1,day2+1):
        while(list[day-1]!=[]):
            list[day-1].pop()
    assert list==[[],[],[{"day":3,"value":3000,"type":"in","description":"salary"}]]
def test_remove_type(list,type):
    for day in list:
        for transaction in day:
            if get_type(transaction)==type:
                day.pop(-1)
    assert list==[[{"day":1,"value":2000,"type":"out","description":"pizza"},{"day":1,"value":3000,"type":"out","description":"salary"}],[{"day":2,"value":3000,"type":"out","description":"salary"}],[]]

def test():
    #list_test=[[] for _ in range(3)]
    list_test=[[{"day":1,"value":2000,"type":"out","description":"pizza"}],[],[]]
    test_add(list_test,1,1000,"in","salary")
    
    list_test=[[{"day":1,"value":2000,"type":"out","description":"pizza"}],[],[]]
    test_insert(list_test,2,2000,"in","job")
    
    list_test=[[{"day":1,"value":2000,"type":"out","description":"pizza"},{"day":1,"value":3000,"type":"in","description":"salary"}],[],[]]
    test_remove_day(list_test,1)
    
    list_test=[[{"day":1,"value":2000,"type":"out","description":"pizza"},{"day":1,"value":3000,"type":"in","description":"salary"}],[{"day":2,"value":3000,"type":"in","description":"salary"}],[{"day":3,"value":3000,"type":"in","description":"salary"}]]
    test_remove_day_day(list_test,1,2)
    
    list_test=[[{"day":1,"value":2000,"type":"out","description":"pizza"},{"day":1,"value":3000,"type":"out","description":"salary"}],[{"day":2,"value":3000,"type":"out","description":"salary"}],[]]
    test_remove_type(list_test,"in")
    