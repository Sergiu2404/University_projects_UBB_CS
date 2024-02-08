from random import randint

from Graph import Graph

class UI:
    def __init__(self):
        self.__graphs = []
        self._current = None

    def create_graph(self):
        # if self._current is None:
        #     self._current = 0
        graph = Graph(0, 0)
        self.__graphs.append(graph)
        self._current = len(self.__graphs) - 1



    def create_random_graph_ui(self):

        vertices = int(input("Enter the number of vertices: "))
        edges = int(input("Enter the number of edges: "))
        graph = self.generate_random(vertices, edges)

        if self._current is None:
            self._current = 0
        self.__graphs.append(graph)
        self._current = len(self.__graphs) - 1



    def generate_random(self,vertices, edges):

        if edges > vertices*(vertices-1)//2:
            raise ValueError("Too many edges")


        graph = Graph(vertices, 0)
        index = 0


        while index < edges:

            vertex1 = randint(0, vertices - 1)
            vertex2 = randint(0, vertices - 1)
            cost = randint(0, 500)
            if graph.add_edge(vertex1, vertex2, cost):
                index+= 1

        return graph


    def read_graph_from_file_ui(self):
        filename = input("Enter the name of the file: ")
        if self._current is None:
            self._current = 0
        graph = self.__graphs[self._current].read_graph_from_file(filename+".txt")
        self.__graphs.append(graph)
        self._current = len(self.__graphs) - 1


    def write_graph_to_file_ui(self):
        current_graph = self.__graphs[self._current]
        output_file = input("file to write named: ")
        self.__graphs[self._current].write_graph_to_file(current_graph, output_file+".txt")


    def get_number_of_vertices_ui(self):
        print(f"Vertices: {len(self.__graphs[self._current].parse_vertices())}")


    def list_outbound(self):
        vertex = int(input("Enter the vertex: "))
        line = str(vertex) + " :"
        for x in self.__graphs[self._current].parse_outbound(vertex):
            line = line + " " + "({}, {})".format(str(vertex), str(x))
        print(line)



    def list_inbound(self):
        vertex = int(input("Enter the vertex: "))
        line = str(vertex) + " :"
        for x in self.__graphs[self._current].parse_inbound(vertex):
            line = line + " " + "({}, {})".format(str(x), str(vertex))
        print(line)



    def list_all_costs(self):
        self.__graphs[self._current].list_costs()


    def parse_all_vertices(self):

        # for vertex in self.__graphs[self._current].parse_vertices():
            print(len(self.__graphs[self._current].parse_vertices()))

    def check_vertex_existence(self):
        vertex=int(input("vertex to check: "))
        if vertex in self.__graphs[self._current].parse_vertices():
            print("Vertex exists")
        else:
            print("Vertex not in graph")


    def add_vertex_ui(self):
        vertex = int(input("Enter the new vertex: "))

        if self.__graphs[self._current].add_vertex(vertex):
            print("Vertex added")
        else:
            print("This vertex already exists")


    def delete_vertex_ui(self):
        vertex = int(input("Enter the vertex you want to delete: "))

        if self.__graphs[self._current].remove_vertex(vertex):
            print("Vertex deleted")
        else:
            print("The vertex does not exist")


    def add_edge_ui(self):

        vertex1 = int(input("vertex1: "))
        vertex2 = int(input("vertex2: "))
        cost = int(input("cost: "))

        if self.__graphs[self._current].add_edge(vertex1, vertex2, cost):
            print("Edge added")
        else:
            print("The edge already exists")


    def remove_edge_ui(self):
        vertex1 = int(input("Enter x = "))
        vertex2 = int(input("Enter y = "))
        if self.__graphs[self._current].remove_edge(vertex1, vertex2):
            print("Edge deleted")
        else:
            print("The edge does not exist")



    def update_cost_ui(self):

        vertex1 = int(input("vertex 1: "))
        vertex2 = int(input("vertex 2: "))
        cost = int(input("cost: "))
        if self.__graphs[self._current].update_cost(vertex1, vertex2, cost):
            print("Cost modified")
        else:
            print("The edge does not exist")


    def get_in_degree_ui(self):

        vertex = int(input("Enter the vertex:"))
        if self.__graphs[self._current].in_degree(vertex) == False:
            print("The vertex does not exist")
        else:
            print(f"{vertex} has in degree {self.__graphs[self._current].in_degree(vertex)}")




    def check_if_edge_ui(self):

        vertex1 = int(input("Enter the first vertex: "))
        vertex2 = int(input("Enter the second vertex: "))


        if self.__graphs[self._current].find_if_edge(vertex1, vertex2) is False:
            print(f"({vertex1}, {vertex2}) does not exist")

        else:
            print(f"edge: ({vertex1}, {vertex2}), cost: {self.__graphs[self._current].find_if_edge(vertex1, vertex2)}")


    # def copy_current_graph_ui(self):
    #     copy = self.__graphs[self._current].make_copy()
    #     self.__graphs.append(copy)


    def print_to_console_ui(self):
        self.__graphs[self._current].print_to_console()


    def find_connected_components(self):
        #vertex=int(input("vertex: "))
        components= self.__graphs[self._current].find_connected_components()  #self.graphs[self._current]
        vertices = self.__graphs[self._current].get_dict_in().keys()
        counter=1
        for component in components:
            print(f"component {counter}: {component}")
            counter+=1
        # for vertex in vertices:
        #     if vertex not in components:
        #         print(f"component {counter}: {vertex}")
        #         counter+=1

    def minimal_tree(self):
        start_vertex=int(input("vertex to start: "))

        result1=self.__graphs[self._current].prim_all_trees(start_vertex)

        # graph = Graph(0, 0)
        #
        # for elem in result1[-1]:
        #     if elem[0] not in self.__graphs[self._current].__in[elem[1]] and elem[1] not in self.__graphs[self._current].__out[elem[0]]:
        #         graph.add_edge(elem[0],elem[1],elem[2])
        #         self.__graphs.append(graph)
        #         self._current = len(self.__graphs) - 1
        #self.print_to_console_ui()

        #print(result1)
        for i,elem in enumerate(result1):
            print(f"tree number {i}: {elem}")


    @staticmethod
    def print_menu():
        print("MENU========================================================\n"
              "Choose your action: \n"
              "random_graph\n" 
              "read_graph\n"
              "write_graph\n"
              "check_vertex_existence\n"
              "check_edge_existence\n"
              "number_of_vertices\n"
              "inbound\n" 
              "outbound\n"
              "add_vertex\n"
              "add_edge\n"
              "remove_vertex\n"
              "remove_edge\n"
              "update_cost\n"
              "in_degree\n"
              "out_degree\n"
              "create_graph\n"
              "print_graph\n"
              "find_connected_components\n"
              "minimal_tree\n"
              "Exit\n"
              "MENU========================================================")

    def start(self):
        self.create_graph()
        print("The current graph is an empty graph!")

        while True:
            try:

                self.print_menu()
                option = input("Choose an option from the menu: \n")

                if option.lower() == "exit":
                    exit()
                elif option.lower()=="random_graph":
                    self.create_random_graph_ui()
                elif option.lower()=="read_graph":
                    self.read_graph_from_file_ui()
                elif option.lower()=="write_graph":
                    self.write_graph_to_file_ui()
                elif option.lower() == "number_of_vertices":
                    self.get_number_of_vertices_ui()
                # elif option.lower() == "outbound":
                #     self.list_outbound()
                elif option.lower() == "inbound":
                    self.list_inbound()
                elif option.lower() == "print_graph":
                    self.list_all_costs()
                elif option.lower() == "add_vertex":
                    self.add_vertex_ui()
                elif option.lower() == "add_edge":
                    self.add_edge_ui()
                elif option.lower() == "remove_vertex":
                    self.delete_vertex_ui()
                elif option.lower() == "remove_edge":
                    self.remove_edge_ui()
                elif option.lower() == "update_cost":
                    self.update_cost_ui()
                elif option.lower() == "in_degree":
                    self.get_in_degree_ui()
                elif option.lower()=="create_graph":
                    self.create_graph()
                elif option.lower()=="check_edge_existence":
                    self.check_if_edge_ui()
                elif option.lower()=="check_vertex_existence":
                    self.check_vertex_existence()
                elif option.lower()=="find_connected_components":
                    self.find_connected_components()
                elif option.lower()=="minimal_tree":
                    self.minimal_tree()
                else:
                    print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                    print("!       Invalid option        !")
                    print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            except Exception as e:
                print(f"[EXCEPTION]: {e}")








# def prim(self,start_vertex):
    #     visited = set()
    #     heap = [(0, start_vertex, None)]
    #     tree = []
    #
    #     while heap:
    #         (cost, vertex, parent) = heapq.heappop(heap)
    #
    #         if vertex not in visited:
    #             visited.add(vertex)
    #
    #             if parent is not None:
    #                 tree.append((parent, vertex, cost))
    #
    #             for neighbor in self.__in[vertex]:
    #                 if neighbor not in visited:
    #                     heapq.heappush(heap, (self.__dictionary_cost[(vertex, neighbor)], neighbor, vertex))
    #
    #     return tree