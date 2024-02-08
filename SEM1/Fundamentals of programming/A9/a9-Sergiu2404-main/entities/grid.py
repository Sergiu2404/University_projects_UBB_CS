from dataclasses import dataclass



class ComputerGrid:
    def __init__(self):
        self.grid=[[" "]*10 for row in range(10)]
        self.grid_seen_by_player=[[" "]*10 for row in range(10)]

    def get_computer_grid(self):
        return self.grid
    def get_grid_seen_by_player(self):
        return self.grid_seen_by_player




    def set_computer_grid(self,new_value):
        self.grid=new_value
    def set_grid_seen_by_player(self,new_value):
        self.grid_seen_by_player=new_value