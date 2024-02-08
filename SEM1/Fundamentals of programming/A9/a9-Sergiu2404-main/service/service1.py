import random

from entities.grid import ComputerGrid
from ui import UI


class Service:
    def __init__(self,repo):
        self.repo=repo
        self.player_ships_positions=[]
        self.computer_ships_positions=[]

    #check input validity functions
    def check_coordinates_validity(self,x,y):
        matrix=self.repo.player_grid.get_player_grid().copy()
        if matrix[x][y]!=" ":
            return False
        return True

    def check_hit_coordinates_validity(self,x, y):
        # matrix=self.repo.computer_grid.get_computer_grid().copy()
        # if matrix[x][y]=="X" or matrix[x][y]=="-":
        #     return False
        # return True
        if self.repo.computer_grid.get_computer_grid()[x][y] == "X" or self.repo.computer_grid.get_computer_grid()[x][y] == "-":
            return False
        return True

    def check_if_fits_in_grid(self,start_row, end_row, start_col, end_col):
        # verificare incadrare in gridul de 10 pe 10
        if start_row not in [0, 1, 2, 3, 4, 5, 6, 7, 8, 9] or end_row not in [0,
        1, 2, 3, 4, 5, 6, 7, 8,9] or start_col not in [0,
        1, 2, 3, 4, 5, 6, 7, 8, 9] or end_col not in [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]:
            return False
        return True

    def check_for_existent_ships_positions_player(self,start_row_index, end_row_index, start_col_index, end_col_index):

        #verificare nave deja existente pe pozitiile  start_row_index, end_row_index, start_col_index, end_col_index
        #din grid
        for index_R,row in enumerate(self.repo.player_grid.get_player_grid()):
            for index_C,col in enumerate(row):
                if start_col_index<=index_C<=end_col_index and start_row_index<=index_R<=end_row_index:
                    if col!=" ":
                        return False
        return True


    def check_for_existent_ships_positions_computer(self,start_row_index, end_row_index, start_col_index, end_col_index):

        #verificare nave deja existente pe pozitiile  start_row_index, end_row_index, start_col_index, end_col_index
        #din grid
        for index_R,row in enumerate(self.repo.computer_grid.get_computer_grid()):
            for index_C,col in enumerate(row):
                if start_col_index<=index_C<=end_col_index and start_row_index<=index_R<=end_row_index:
                    if col!=" ":
                        return False
        return True


    def check_for_number_of_destroyed_computer_ships(self):

        if len(self.repo.computer_ships_remained)==0:
            return True #all were destroyed => player wins

        else:

            # se verifica daca in grid mai exista cifrele care reprezinta numarul unei nave
            for number_of_ship in self.repo.computer_ships_remained:
                count_non_appearences = 0
                for row in self.repo.computer_grid.get_computer_grid():
                    if number_of_ship not in row:
                        count_non_appearences+=1
                if count_non_appearences==10:
                    index=self.repo.computer_ships_remained.index(number_of_ship)
                    self.repo.computer_ships_remained.remove(self.repo.computer_ships_remained[index])

        if len(self.repo.computer_ships_remained)==0:
            return True
        return False #still remained ships


    def check_for_number_of_destroyed_player_ships(self):

        if len(self.repo.player_ships_remained) == 0:
            return True  # all were destroyed => computer wins

        else:

            remained_ships = len(self.repo.player_ships_remained)

            # se verifica daca in grid mai exista cifrele care reprezinta numarul unei nave
            for number_of_ship in self.repo.player_ships_remained:
                count_non_appearences = 0
                for row in self.repo.player_grid.get_player_grid():
                    if number_of_ship not in row:
                        count_non_appearences += 1
                if count_non_appearences == 10:
                    index = self.repo.player_ships_remained.index(number_of_ship)
                    self.repo.player_ships_remained.remove(self.repo.player_ships_remained[index])

        if len(self.repo.player_ships_remained) == 0:
            return True
        return False  # still remained ships





    def place_computer_ships_service(self,ships_positions_list):

        return self.repo.place_computer_ships_repo(ships_positions_list)


    def place_player_ships_service(self,ships_positions_list):

        return self.repo.place_player_ships_repo(ships_positions_list)


    def transform_strings_to_coordinates(self,x,y):
        letter_dict={"a":0, "b":1, "c":2, "d":3, "e":4, "f":5, "g":6, "h":7, "i":8, "j":9}
        return int(x),letter_dict[y]





    def place_hit_on_player_grid(self,x,y):
        return self.repo.place_hit_on_player_grid_repo(x,y)

    def place_hit_on_computer_grid(self,x,y):
        return self.repo.place_hit_on_computer_grid_repo(x,y)

    def empty_grids(self):
        return self.repo.empty_grids_repo()

    def get_player_grid_first_position(self):
        return self.repo.get_player_grid_first_position_repo()



