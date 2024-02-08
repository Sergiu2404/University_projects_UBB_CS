from src.ui.UI2 import *
from src.tests.tests import *

if __name__=='__main__':
    list_transactions = [[] for _ in range(30)]
    undo_list=[]
    currDay=-1
    operation_list=[]
    test()
    menu_loop(list_transactions,currDay,undo_list,operation_list)
#{"day":1,"value":20,"type":"in","description":"jjjjjjjj"},{"day":1,"value":20,"type":"out","description":"oijoijoid"}