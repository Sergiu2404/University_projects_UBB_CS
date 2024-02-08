


class PlayerGrid:
    def __init__(self):
        self.grid =[[" "] *10 for row in range(10)]

    def get_player_grid(self):
        return self.grid

    def set_player_grid(self,new_value):
        self.grid=new_value






    # def build_grid(self):
    #     for row in range(self.rows):
    #         a = []  # entire row
    #         for col in range(self.cols):
    #             a.append(" ")
    #         self.grid.append(a)
    # abcdefghijklmno
    ###################
 #  1#               ##
 #  2#               ##
 #  3#               ##
 #  4#               ##
 #  5#               ##
 #  6#               ##
    ###################

