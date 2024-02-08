

import random




class UIHard:
    def __init__(self,service):
        self.service=service


    def __menu(self):
        print("MENU---------------------------")
        print("Enjoy playing...              -")
        print("for start type start")
        print("for exit type exit")
        print("MENU---------------------------")


    def enter_game(self):
        print("###############")
        print(" X =>You hit the opponent's ship")
        print(" - => You missed")
        print(" 1/2/3/4/5/6 =>Your numbered battle ships")
        print("")
        print("###############")
        self.__menu()
        result=self.get_user_first_option()
        if result=="start":
            self.run_game()
        else:
            self.enter_game()


    def run_game(self):

        print("Your Grid")
        self.print_grid(self.service.repo.get_player_grid())  #print player grid

        required_ships=6
        # player places ships
        while required_ships!=0:
            print("########################")
            print("Your Grid")
            print(f"{required_ships} more ships to place")
            print("########################")
            ##generate random for player
            self.generate_player_random_ships_positions_for_test()
            ##

            self.print_grid(self.service.repo.get_player_grid())  # print player grid

            #####
            #self.player_choose_ships_positions()
            #####
            #@TODO uncomment code above for playing and comment the line 49 and reverse when testing
            # self.generate_player_random_ships_positions_for_test() function which is only for
            # testing the game
            self.computer_choose_random_ships_positions()
            required_ships-=1


        #self.print_grid(self.service.repo.get_computer_grid()) #print computer grid after all ships are placed
        print("###########################")
        print("Ships placed succesfully")
        print('Let the battle begin')
        print("###########################")

        self.rockets=50
        WINNER=0

        print("###########################")
        print("OPPONENT'S GRID")
        print("###########################")
        # print grid with hits done by the player
        self.print_hits_grid(self.service.repo.computer_grid.get_grid_seen_by_player())  # empty grid only with hits
        #self.print_grid(self.service.repo.computer_grid.get_computer_grid()) #@TODO comment this line when plsying


        print("###########################")
        print("YOUR STARTING GRID")
        print(f"You have {self.rockets} more rockets")
        print("###########################")
        self.print_grid(self.service.repo.player_grid.get_player_grid())  # player's grid

        while self.rockets!=0:

            ###############################################
            #get player hit coordinates
            x,y=self.choose_hit_coordinates()
            self.service.place_hit_on_computer_grid(x,y)

            #computer strikes automatically
            self.computer_choose_random_coordinates_and_hits() #ALSO HITS
            ###############################################

            print("###########################")
            print("OPPONENT'S GRID")
            print(f"Opponent's navy got hit {self.service.repo.computer_grid_got_hit} times")
            print("###########################")
            # print grid with hits done by the player
            self.print_hits_grid(self.service.repo.computer_grid.get_grid_seen_by_player())  # empty grid only with hits

            #self.print_grid(self.service.repo.computer_grid.get_computer_grid()) #@TODO this needs to be commented

            print("###########################")
            print("YOUR GRID")
            print(f"You have {self.rockets-1} more rockets")
            print(f"You have {len(self.service.repo.computer_ships_remained)} more ships to sink to the computer")
            print(f"You hit opponent's navy {self.service.repo.computer_grid_got_hit} times")
            print("###########################")
            self.print_grid(self.service.repo.player_grid.get_player_grid())  # player's grid


            #check for destroyed ships in both sides
            if self.service.check_for_number_of_destroyed_player_ships()==True:


                WINNER=1


                print("###########################")
                print("YOUR GRID")
                print(f"You had {self.rockets-1} more rockets")
                print(f"You had {len(self.service.repo.computer_ships_remained)} more ships to sink to the computer")
                print("###########################")
                self.print_grid(self.service.repo.player_grid.get_player_grid())  # player's grid

                print("###########################")
                print("OPPONENT'S GRID")
                print(f"Opponent hit your navy {self.service.repo.player_grid_got_hit} times")
                print("###########################")
                # print grid with hits done by the player
                self.print_hits_grid(self.service.repo.computer_grid.get_computer_grid())  # empty grid only with hits

                print("You lost")
                print("######################################################################################")
                print("##                                                                                  ##")
                print("##                                                                                  ##")
                print("##                                                                                  ##")
                print("##                                                                                  ##")
                print("##                                                                                  ##")
                print("######################################################################################")
                break

            elif self.service.check_for_number_of_destroyed_computer_ships()==True:


                WINNER=1


                print("###########################")
                print("YOUR GRID")
                print(f"You have {self.rockets-1} more rockets")
                print("###########################")
                self.print_grid(self.service.repo.player_grid.get_player_grid())  # player's grid

                print("###########################")
                print("OPPONENT'S GRID")
                print(f"Opponent hit your navy {self.service.repo.player_grid_got_hit} times")
                print("###########################")
                # print grid with hits done by the player
                self.print_hits_grid(self.service.repo.computer_grid.get_grid_seen_by_player())  # empty grid only with hits

                print("You won")
                print("######################################################################################")
                print("##                                                                                  ##")
                print("##                                                                                  ##")
                print("##                                                                                  ##")
                print("##                                                                                  ##")
                print("##                                                                                  ##")
                print("######################################################################################")
                break


            self.rockets-=1

        print("#############################")
        print("You ran out of rockets")
        print("#############################")

        if WINNER==0:
            if len(self.service.repo.computer_ships_remained) > len(self.service.repo.player_ships_remained):
                print("You lost because of the number of ships your opponent sunk!!! CONGRATULATIONS")

            elif len(self.service.repo.computer_ships_remained) < len(self.service.repo.player_ships_remained):
                print("But you won because of the number of ships you sunk!!! CONGRATULATIONS")

            else:
                if self.service.repo.computer_grid_got_hit > self.service.repo.player_grid_got_hit:
                    print("[[[[[[[[[[[[[[[[[[[[[[[[   YOU WON   ]]]]]]]]]]]]]]]]]]]]]]]]]")
                elif self.service.repo.computer_grid_got_hit < self.service.repo.player_grid_got_hit:
                    print("[[[[[[[[[[[[[[[[[[[[[[[[   YOU LOST  ]]]]]]]]]]]]]]]]]]]]]]]]]")
                else:
                    print("[[[[[[[[[[[[[[[[[[[[[[[[   TIE   ]]]]]]]]]]]]]]]]]]]]]]]]")

        self.service.empty_grids()
        self.enter_game()



    #get user input
    def get_user_first_option(self):
        '''
        gets start/exit input from user
        :return:
        '''
        option=input("start/exit: ")
        while(option.lower()!="start" and option.lower()!="exit"):
            print("invalid command. Try again")
            option=self.get_user_first_option()
        if option.lower()=="start":
            return "start"
        else :
            exit()



    def player_choose_ships_positions(self):

        x,y=self.choose_coordinates()
        #valid coordinates(they were already checked)

        direction=self.choose_direction()
        length = self.choose_length()

        while self.check_user_ship_position_validity_player(x,y,direction,length)==False:
            x,y=self.choose_coordinates()
            direction=self.choose_direction()
            length=self.choose_length()

        if self.check_user_ship_position_validity_player(x,y,direction,length)!=False:
            ship_position_list=self.check_user_ship_position_validity_player(x,y,direction,length).copy() #lista
            #cu pozitiile:[start_row,end_row,start_col,end_col
            self.service.place_player_ships_service(ship_position_list)
        else:
            self.player_choose_ships_positions()


    def choose_coordinates(self):
        #try:
            coord_X=input("enter number between 0-9: ")
            coord_Y=input("enter letter between A-J: ")

            while coord_X not in ["0","1","2","3","4","5","6","7","8","9"] or coord_Y.lower() not in ["a"
            ,"b","c","d","e","f","g","h","i","j"]:
                print("wrong input. Try again")
                coord_X = input("enter number between 0-9: ")
                coord_Y = input("enter letter between A-J: ")
                #coord_X,coord_Y=self.choose_coordinates()

            coord_X, coord_Y = self.service.transform_strings_to_coordinates(coord_X,coord_Y.lower())

            if self.service.check_coordinates_validity(coord_X, coord_Y) == True:
                return coord_X, coord_Y
            else:
                coord_X,coord_Y=self.choose_coordinates()
                return coord_X,coord_Y

        #except ValueError as V:
            #print(f"[Exception]: {V}")
            #coord_X,coord_Y=self.choose_coordinates()
            #return 0 #for cases in which this function needs to be executed again
        #except Exception as E:
            #print(f"[Exception]: {E}")
            #coord_X,coord_Y=self.choose_coordinates()
            #return 0
        #return coord_X, coord_Y.lower() #return self.service.transform_strings_to_coordinates(coord_X,coord_Y)


    def choose_length(self):
        try:
            length=int(input("length between 4 and 5: "))
            if 4<=length<=5:
                return length
            else:
                while 5<length or length<4:
                    print("invalid length")
                    length=self.choose_length()
        except ValueError as V:
            print(f"Exception {V}")
            length=self.choose_length()
        except Exception as E:
            print(f"Exception {E}")
            length=self.choose_length()
        return length


    def choose_direction(self):
        try:
            direction=input("left/right/up/down: ")
            while direction.lower() not in ["left","right","up","down"]:
                print("invalid direction")
                direction =self.choose_direction()
            return direction
        except ValueError as V:
            print(f"Exception {V}")
            direction=self.choose_direction()
        except Exception as E:
            print(f"Exception {E}")
            direction=self.choose_direction()
        return direction


    def choose_hit_coordinates(self):

            coord_X=input("enter number between 0-9: ")
            coord_Y=input("enter letter between A-J: ")

            while coord_X not in ["0","1","2","3","4","5","6","7","8","9"] or coord_Y.lower() not in ["a"
            ,"b","c","d","e","f","g","h","i","j"]:
                print("wrong input. Try again")
                coord_X = input("enter number between 0-9: ")
                coord_Y = input("enter letter between A-J: ")
                #coord_X,coord_Y=self.choose_coordinates()

            coord_X, coord_Y = self.service.transform_strings_to_coordinates(coord_X,coord_Y.lower())

            if self.service.check_hit_coordinates_validity(coord_X, coord_Y) == True:
                return coord_X, coord_Y
            else:
                print("wrong coords. Try again")
                coord_X,coord_Y=self.choose_coordinates()
                return coord_X,coord_Y


    def computer_choose_random_coordinates_and_hits(self):

        #strategy=1
        strategy=2

        #1-hits random positions
        #2-hits by remembering the previous hits

        if strategy==1:
            x = random.randint(0, 9)
            y = random.randint(0, 9)
            if self.service.check_hit_coordinates_validity(x, y)==False:
                self.computer_choose_random_coordinates_and_hits()
            else:
                self.service.place_hit_on_player_grid(x,y)

        elif strategy==2:

            hit=0

            if self.rockets!=50 and len(self.service.repo.list_of_computer_hits_coordinates)!=0:

                #computer searches through coordinates where he already hit
                for index,coordinate in enumerate(self.service.repo.list_of_computer_hits_coordinates):

                    if 0<=coordinate[0]+1<=9 and 0<=coordinate[1]<=9 and hit==0:
                        #check if position was already hit
                        if self.service.repo.player_grid.grid[coordinate[0]+1][coordinate[1]]!="X" and self.service.repo.player_grid.grid[coordinate[0]+1][coordinate[1]]!="-":
                            hit = 1
                            self.service.place_hit_on_player_grid(coordinate[0]+1,coordinate[1])

                    if 0<=coordinate[0]-1<=9 and 0<=coordinate[1]<=9 and hit==0:
                        if self.service.repo.player_grid.grid[coordinate[0]-1][coordinate[1]]!="X" and self.service.repo.player_grid.grid[coordinate[0]-1][coordinate[1]]!="-":
                            hit = 1
                            self.service.place_hit_on_player_grid(coordinate[0] - 1, coordinate[1])

                    if 0 <= coordinate[0]<= 9 and 0 <= coordinate[1]+1 <= 9 and hit==0:
                        if self.service.repo.player_grid.grid[coordinate[0]][coordinate[1]+1]!="X" and self.service.repo.player_grid.grid[coordinate[0]][coordinate[1]+1]!="-":
                            hit = 1
                            self.service.place_hit_on_player_grid(coordinate[0], coordinate[1]+1)

                    if 0<=coordinate[0]+1<=9 and 0<=coordinate[1]-1<=9 and hit==0:
                        if self.service.repo.player_grid.grid[coordinate[0]][coordinate[1]-1]!="X" and self.service.repo.player_grid.grid[coordinate[0]][coordinate[1]-1]!="-":
                            hit = 1
                            self.service.place_hit_on_player_grid(coordinate[0], coordinate[1] - 1)
                    # if hit==0:
                    #     self.service.repo.list_of_computer_hits_coordinates.remove(coordinate)


            if self.rockets==50:
                hit=1
                x,y=self.service.get_player_grid_first_position()
                self.service.place_hit_on_player_grid(x, y) #@TODO comment this if you want the computer to not hit from the first time

            if hit==0:

                x = random.randint(0, 9)
                y = random.randint(0, 9)
                if self.service.check_hit_coordinates_validity(x, y) == False:
                    self.computer_choose_random_coordinates_and_hits()
                else:
                    self.service.place_hit_on_player_grid(x, y)


        else:
            pass


    #check input validity functions
    def check_user_ship_position_validity_player(self,x, y, direction, length):
        if direction.lower()=="left":
            start_row=x
            start_col=y - length+1
            end_row=x
            end_col=y
            if self.service.check_if_fits_in_grid(start_row, end_row, start_col, end_col)==True:
                if self.service.check_for_existent_ships_positions_player(start_row, end_row, start_col, end_col)==True:
                    return [start_row,end_row,start_col,end_col]
                else:
                    #print("check existent ships False")
                    return False
            else:
                #print("check if fits in grid False")
                return False

        elif direction.lower()=="right":
            start_row = x
            start_col = y
            end_row = x
            end_col = y + length-1
            if self.service.check_if_fits_in_grid(start_row, end_row, start_col, end_col) == True:
                if self.service.check_for_existent_ships_positions_player(start_row, end_row, start_col, end_col) == True:
                    return [start_row, end_row, start_col, end_col]
                else:
                    #print("check existent ships False")
                    return False
            else:
                #print("check if fits in grid False")
                return False

        elif direction.lower()=="up":
            start_row = x - length+1
            start_col = y
            end_row = x
            end_col = y
            if self.service.check_if_fits_in_grid(start_row, end_row, start_col, end_col) == True:
                if self.service.check_for_existent_ships_positions_player(start_row, end_row, start_col, end_col) == True:
                    return [start_row, end_row, start_col, end_col]
                else:
                    #print("check existent ships False")
                    return False
            else:
                #print("check if fits in grid False")
                return False

        elif direction.lower()=="down":
            start_row = x
            start_col = y
            end_row = x + length-1
            end_col = y
            if self.service.check_if_fits_in_grid(start_row, end_row, start_col, end_col) == True:
                if self.service.check_for_existent_ships_positions_player(start_row,
                                                                   end_row, start_col, end_col) == True:
                    return [start_row, end_row, start_col, end_col]
                else:
                    #print("check existent ships False")
                    return False
            else:
                #print("check if fits in grid False")
                return False

        else:
            #print("Invalid direction check ship position validity")
            return False
        #daca nu sunt deja nave acolo

    def check_user_ship_position_validity_computer(self, x, y, direction, length):
        if direction.lower() == "left":
            start_row = x
            start_col = y - length + 1
            end_row = x
            end_col = y
            if self.service.check_if_fits_in_grid(start_row, end_row, start_col, end_col) == True:
                if self.service.check_for_existent_ships_positions_computer(start_row, end_row, start_col,
                                                                          end_col) == True:
                    return [start_row, end_row, start_col, end_col]
                else:
                    # print("check existent ships False")
                    return False
            else:
                # print("check if fits in grid False")
                return False

        elif direction.lower() == "right":
            start_row = x
            start_col = y
            end_row = x
            end_col = y + length - 1
            if self.service.check_if_fits_in_grid(start_row, end_row, start_col, end_col) == True:
                if self.service.check_for_existent_ships_positions_computer(start_row, end_row, start_col, end_col) == True:
                    return [start_row, end_row, start_col, end_col]
                else:
                    # print("check existent ships False")
                    return False
            else:
                # print("check if fits in grid False")
                return False

        elif direction.lower() == "up":
            start_row = x - length + 1
            start_col = y
            end_row = x
            end_col = y
            if self.service.check_if_fits_in_grid(start_row, end_row, start_col, end_col) == True:
                if self.service.check_for_existent_ships_positions_computer(start_row, end_row, start_col, end_col) == True:
                    return [start_row, end_row, start_col, end_col]
                else:
                    # print("check existent ships False")
                    return False
            else:
                # print("check if fits in grid False")
                return False

        elif direction.lower() == "down":
            start_row = x
            start_col = y
            end_row = x + length - 1
            end_col = y
            if self.service.check_if_fits_in_grid(start_row, end_row, start_col, end_col) == True:
                if self.service.check_for_existent_ships_positions_computer(start_row,
                                                                   end_row, start_col, end_col) == True:
                    return [start_row, end_row, start_col, end_col]
                else:
                    # print("check existent ships False")
                    return False
            else:
                # print("check if fits in grid False")
                return False

        else:
            # print("Invalid direction check ship position validity")
            return False
        # daca nu sunt deja nave acolo

        ########################
        # COMPUTER PLAYER PART #
        ########################
    def computer_choose_random_ships_positions(self):
        directions = ["right", "left", "up", "down"]

        x = random.randint(0, 9)
        y = random.randint(0, 9)
        direction = random.choice(directions)
        length = 3#random.randint(2, 3)

        while self.check_user_ship_position_validity_computer(x, y, direction, length) == False:
            x = random.randint(0, 9)
            y = random.randint(0, 9)
            direction = random.choice(directions)
            length = 3#random.randint(2, 5)

        if self.check_user_ship_position_validity_computer(x, y, direction, length) != False:
            computer_ship_position_list = self.check_user_ship_position_validity_computer(x, y, direction,length).copy()
            # cpoiaza lista cu pozitiile:[start_row,end_row,start_col,end_col] returnata de functie
            self.service.place_computer_ships_service(computer_ship_position_list)
        else:
            self.computer_choose_random_ships_positions()

    def generate_player_random_ships_positions_for_test(self):
        directions = ["right", "left", "up", "down"]

        x = random.randint(0, 9)
        y = random.randint(0, 9)
        direction = random.choice(directions)
        length = random.randint(4, 5)

        while self.check_user_ship_position_validity_player(x, y, direction, length) == False:
            x = random.randint(0, 9)
            y = random.randint(0, 9)
            direction = random.choice(directions)
            length = random.randint(4, 5)

        if self.check_user_ship_position_validity_player(x, y, direction, length) != False:
            computer_ship_position_list = self.check_user_ship_position_validity_player(x, y, direction, length).copy()
            # cpoiaza lista cu pozitiile:[start_row,end_row,start_col,end_col] returnata de functie
            self.service.place_player_ships_service(computer_ship_position_list)
        else:
            self.computer_choose_random_ships_positions()







    #print functions
    def print_grid(self,matrix):
        list=["Y","O","U","R"," "," ","G","R","I","D"]
        print("  A B C D E F G H I J ")
        print(" +-+-+-+-+-+-+-+-+-+-+")
        counter=0
        for indexR,row in enumerate(matrix):
            for indexC,col in enumerate(row):
                if indexC == 0:
                    print(counter, end="|")
                    counter += 1
                    print(col, end="|")
                elif indexC == 9:
                    print(col, end=f"|{list[indexR]}")
                else:
                    print(col, end="|")
            print()
        print(" +-+-+-+-+-+-+-+-+-+-+  ")


    def print_hits_grid(self,matrix):
        list=["#","O","P","P","O","N","E","N","T","#"]
        print("  A B C D E F G H I J ")
        print(" +-+-+-+-+-+-+-+-+-+-+")
        counter = 0
        for indexR, row in enumerate(matrix):
            for indexC, col in enumerate(row):
                if indexC == 0:
                    print(counter, end="|")
                    counter += 1
                    print(col, end="|")
                elif indexC == 9:
                    print(col, end=f"|{list[indexR]}")
                else:
                    print(col, end="|")
            print()
        print(" +-+-+-+-+-+-+-+-+-+-+  ")
