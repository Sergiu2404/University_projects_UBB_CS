# import pygame
#
#
# pygame.init()
#
# white=(255,255,255)
# black=(0,0,0)
# blue=(0,0,128)
from entities.grid import ComputerGrid
from entities.playerGrid import PlayerGrid
from repository.repo1 import Repo
from service.service1 import Service
from ui.UI import UI
from ui.UI_hard import UIHard

if __name__=="__main__":
    computer_grid=ComputerGrid()
    player_grid=PlayerGrid()
    repo=Repo(computer_grid,player_grid)
    service=Service(repo)

    # ui=UI(service)
    # ui.enter_game()

    #HARD MODE
    ui_hard = UIHard(service)
    ui_hard.enter_game()