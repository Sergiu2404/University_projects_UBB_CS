def print_grid(self):
    print("  ABCDEFGHIJKLMNOPQRSTUVWXYZ  ")
    print("##############################")
    counter = 0
    for row in range(self.comp_grid.rows):
        for col in range(self.comp_grid.cols):
            if col == 0:
                print("##",end="")
                print(self.comp_grid.grid[row][col], end="")
            elif col==25:
                print(self.comp_grid.grid[row][col], end="")
                print("##",end="")
                print(counter,end="")
                counter += 1
            else:
                print(self.comp_grid.grid[row][col], end="")
        print(end="\n")
    print("##############################")


