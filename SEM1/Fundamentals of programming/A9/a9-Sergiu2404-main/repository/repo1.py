

class Repo:
    def __init__(self,computer_grid,player_grid):

        self.computer_grid=computer_grid
        self.player_grid=player_grid

        self.ships_number=1
        self.computer_ships_number=1

        self.player_ships_positions = []
        self.computer_ships_positions = []

        self.player_ships_remained = ["1","2","3","4","5","6"]
        self.computer_ships_remained = ["1", "2", "3", "4", "5", "6"]

        self.computer_grid_got_hit=0
        self.player_grid_got_hit=0

        self.computer_missed_shots=0
        self.player_missed_shots=0

        #for hard mode
        self.list_of_computer_hits_coordinates=[]

    def get_player_grid(self):
        return self.player_grid.grid

    def get_computer_grid(self):
        return self.computer_grid.grid

    def get_player_ships_remained(self):
        return self.player_ships_remained
    def get_computer_ships_remained(self):
        return self.computer_ships_remained


    def get_player_grid_first_position_repo(self):

        x=0
        y=0
        found=0
        for index_R,row in enumerate(self.player_grid.grid):
            for index_C,col in enumerate(row):
                if found==1:
                    break
                if col in ["1","2","3","4","5","6"]:
                    x=index_R
                    y=index_C
                    found=1
            if found==1:
                break

        return x,y




    # modify player grid
    def place_player_ships_repo(self,ships_positions_list):

        self.player_ships_positions.append(ships_positions_list)

        start_row=ships_positions_list[0]
        end_row=ships_positions_list[1]
        start_col = ships_positions_list[2]
        end_col = ships_positions_list[3]

        for index_R,row in enumerate(self.player_grid.grid):
            for index_C,col in enumerate(row):
                if start_row<=index_R<=end_row and start_col<=index_C<=end_col:
                    self.player_grid.grid[index_R][index_C]=str(self.ships_number)

        self.ships_number+=1

    #modify computer grid
    def place_computer_ships_repo(self,ships_positions_list):

        self.computer_ships_positions.append(ships_positions_list)

        start_row=ships_positions_list[0]
        end_row=ships_positions_list[1]
        start_col = ships_positions_list[2]
        end_col = ships_positions_list[3]

        for index_R,row in enumerate(self.computer_grid.grid):
            for index_C,col in enumerate(row):
                if start_row<=index_R<=end_row and start_col<=index_C<=end_col:
                    self.computer_grid.grid[index_R][index_C]=str(self.computer_ships_number)

        self.computer_ships_number+=1




    def place_hit_on_player_grid_repo(self,x,y):

        if self.player_grid.grid[x][y] in ["1","2","3","4","5","6"]:
            self.player_grid.grid[x][y]="X"
            self.player_grid_got_hit+=1
            self.list_of_computer_hits_coordinates.append([x,y])
            # for index_ship,ship in enumerate(self.player_ships_positions):
            #     if x in [ship[0],ship[1]] and y in [0,0,ship[2],ship[3]]: #ca sa fie 4 elmente ca in sirul initial
            #         index_x_from_list=ship.index(x)
            #         index_y_from_list=ship.index(y)
            #         self.player_ships_positions[index_ship][index_x_from_list]=-1
            #         self.player

        elif self.player_grid.grid[x][y]==" ":
            self.player_grid.grid[x][y] = "-"
            self.computer_missed_shots+=1

    def place_hit_on_computer_grid_repo(self,x,y):

        if self.computer_grid.grid[x][y] in ["1","2","3","4","5","6"]:
            self.computer_grid.grid[x][y]="X"
            self.computer_grid.grid_seen_by_player[x][y]="X"
            self.computer_grid_got_hit+=1

        elif self.computer_grid.grid[x][y]==" ":
            self.computer_grid.grid[x][y] = "-"
            self.computer_grid.grid_seen_by_player[x][y]="-"
            self.player_missed_shots += 1



    def empty_grids_repo(self):
        #bring variables to initial setup
        self.computer_grid.grid=[ [" "] * 10 for row in range(10)]
        self.player_grid.grid= [[" "] *10 for row in range(10)]
        self.computer_grid.grid_seen_by_player=[[" "] *10 for row in range(10)]

        self.ships_number = 1
        self.computer_ships_number = 1

        self.player_ships_positions = []
        self.computer_ships_positions = []

        self.player_ships_remained = ["1", "2", "3", "4", "5", "6"]
        self.computer_ships_remained = ["1", "2", "3", "4", "5", "6"]

        self.computer_grid_got_hit = 0
        self.player_grid_got_hit = 0

        self.list_of_computer_hits_coordinates=[]


