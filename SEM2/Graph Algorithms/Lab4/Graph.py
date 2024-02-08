import copy
from collections import deque, defaultdict
from queue import Queue
import heapq


class Graph:
    def __init__(self, numberVertices, numberEdges):

        self.__numberVertices = numberVertices
        self.__numberEdges = numberEdges

        self.__in = {}
        self.__dictionary_cost = {}

        for i in range(numberVertices):
            self.__in[i] = []

    def get_dict_in(self):
        return self.__in


    def write_graph_to_file(self,graph, file_name):

        file = open(file_name, "w")
        first_line = str(graph.__numberVertices) + ' ' + str(graph.__numberEdges) + '\n'
        file.write(first_line)
        count=0
        for edge in graph.__dictionary_cost.keys():
            if count%2==0:
                new_line = "{}->{}: {}\n".format(edge[0], edge[1], graph.__dictionary_cost[edge])
                file.write(new_line)
            count+=1
            #new_line = str(edge[0]) + " " + str(edge[1]) + " " + str(graph.__dictionary_cost[edge]) + "\n"


        print("Isolated vertices: ")

        for vertex in graph.__in.keys():
            if len(graph.__in[vertex]) == 0:
                new_line = "{}\n".format(vertex)
                #new_line = str(vertex) + "\n"
                file.write(new_line)

        file.close()



    def read_graph_from_file(self,file_name):

        file = open(file_name, "r")
        line = file.readline()
        line = line.strip()

        vertices, edges = line.split()

        graph = Graph(int(vertices), int(edges))
        line = file.readline().strip()

        while len(line) != 0:

            line = line.split()
            vertex1=int(line[0])
            vertex2=int(line[1])
            cost=int(line[2])

            if len(line) == 1:
                graph.__in[vertex1] = []
                graph.__in[vertex2]=[]
            else:
                graph.__in[vertex2].append(vertex1)
                graph.__in[vertex1].append(vertex2)
                graph.__dictionary_cost[(vertex1, vertex2)]= cost
                graph.__dictionary_cost[(vertex2, vertex1)] = cost

            line = file.readline().strip()


        file.close()

        return graph


    def list_costs(self):
        count=0
        for key in self.dictionary_cost():
            if count%2==0:
                line = "({}, {})".format(key[0], key[1]) + " :" + str(self.__dictionary_cost[key])
                print(line)
            count+=1

    def dictionary_cost(self):
        return self.__dictionary_cost


    def dictionary_in(self):
        return self.__in




    def numberVertices(self):
        return self.__numberVertices


    # def numberEdges(self):
    #     return self.__numberEdges


    def parse_vertices(self):
        # vertices = list(self.__in.keys())
        # return [vertex for vertex in vertices]
        return list(self.__in.keys())


    def parse_inbound(self, vertex):
        #return [y for y in self.__in[vertex]]
        return list(self.__in[vertex])



    def parse_cost(self):
        # edges = list(self.__dictionary_cost.keys())
        # return [edge for edge in edges]
        return list(self.__dictionary_cost.keys())


    def add_vertex(self, x):
        if x in self.__in.keys():
            return False
        self.__in[x] = []
        self.__numberVertices += 1
        return True


    def remove_vertex(self, vertex):
        if vertex not in self.__in.keys():
            return False
        self.__in.pop(vertex)

        for key in self.__in.keys():
            if vertex in self.__in[key]:
                self.__in[key].remove(vertex)
        keys = list(self.__dictionary_cost.keys())

        for key in keys:
            if key[0] == vertex or key[1] == vertex:
                self.__dictionary_cost.pop(key)
                self.__numberEdges -= 1
        self.__numberVertices -= 1
        return True


    def in_degree(self, vertex):
        if vertex not in self.__in.keys():
            return False
        counter=0
        for key in self.__in.keys(): #keys in __in dictionary
            if key!=vertex:
                for value in self.__in[key]: #list of values for every node in __in
                    if value==vertex:
                        counter+=1
        return len(self.__in[vertex])+counter



    def add_edge(self, vertex1, vertex2, cost):

        if vertex1==vertex2 or (vertex1 in self.__in[vertex2] and vertex2 in self.__in[vertex1]) or ((vertex1, vertex2) in self.__dictionary_cost.keys() and (vertex2,vertex1) in self.__dictionary_cost.keys()):
            return False

        self.__in[vertex2].append(vertex1)
        self.__in[vertex1].append(vertex2)
        self.__dictionary_cost[(vertex1, vertex2)] = cost
        self.__dictionary_cost[(vertex2,vertex1)]=cost
        self.__numberEdges = self.__numberEdges + 1

        return True


    def remove_edge(self, vertex1, vertex2):

        if vertex1 not in self.__in.keys() or vertex2 not in self.__in.keys() or vertex1 not in self.__in[vertex2] or (vertex1, vertex2) not in self.__dictionary_cost.keys():
            return False

        self.__in[vertex2].remove(vertex1)
        self.__in[vertex1].remove(vertex2)
        self.__dictionary_cost.pop((vertex1, vertex2))
        self.__dictionary_cost.pop((vertex2,vertex1))
        self.__numberEdges -= 1

        return True


    def find_if_edge(self, vertex1, vertex2):
        if vertex1 in self.__in[vertex2]:
            return self.__dictionary_cost[(vertex1, vertex2)]
        return False


    def update_cost(self, vertex1, vertex2, cost):
        if (vertex1, vertex2) not in self.__dictionary_cost.keys():
            return False
        self.__dictionary_cost[(vertex1, vertex2)] = cost
        self.__dictionary_cost[(vertex2,vertex1)]=cost
        return True

    def print_to_console(self):
        #count=0
        for edge in self.__dictionary_cost.keys():
            # if count%2==0:
                new_line = str(edge[0]) + "->" + str(edge[1]) + ": " + str(self.__dictionary_cost[edge]) + "\n"
                print(new_line)
            # count+=1

        print("Isolated vertices: ")
        for vertex in self.__in.keys():
            if len(self.__in[vertex]) == 0:
                new_line = "{}\n".format(vertex)
                print(new_line)

    def bfs(self, start_vertex,visited):
        component=[]
        queue= Queue()
        queue.put(start_vertex)
        visited[start_vertex]=True
        component.append(start_vertex)

        while not queue.empty():
            vertex=queue.get()
            for neighbor in self.__in[vertex]:
                if not visited[neighbor]:
                    queue.put(neighbor)
                    visited[neighbor]=True
                    component.append(neighbor)

        return component

   
    def find_connected_components(self):
        visited = defaultdict(bool)
        components=[]

        for node in self.__in:
            if not visited[node]:
                component=self.bfs(node , visited)
                components.append(component)

        return components


    def prim_all_trees(self, start_vertex):
        visited = set()
        heap = [(0, start_vertex, None)]# we use heap because when we pop from the heap, we can pop the element which has the smallest value,
        # in our case we need to pop the tuple from the heap that got the smallest cost
        tree = [] #minimal spanning tree
        all_trees = [] #list of all the minimal spanning trees

        while len(visited) < len(self.__in):  # Continue until all vertices have been visited-stops at tree of number of vertices-1, because
            #can only be number of vertices-1 minimal trees in a graph
            (cost, vertex, parent) = heapq.heappop(heap) #pops from the heap the element which has the first value of the tuple(cost in this case),
            # the tuple that has the smallest value, the smallest cost

            if vertex not in visited: #add every current vertex to the set of visited vertices
                visited.add(vertex)

                if parent is not None: #check if still is the first element added to the heap, the one before the while loop
                    tree.append((parent, vertex, cost)) #add tuple(edge and coresponding cost) to the old tree
                    all_trees.append(copy.deepcopy(tree)) #add tree to the list that contains all the trees

                for neighbor in self.__in[vertex]: #parse all neigbors of the current vertex
                    if neighbor not in visited: #check if neighbor was already visited, for efficiency
                        heapq.heappush(heap, (self.__dictionary_cost[(vertex, neighbor)], neighbor, vertex)) # adds to the heap a tuple formed
                        #by 3 elements(cost,neighbor,vertex)

        return all_trees #returns all the trees in a list

    # def make_copy(self):
    #     return copy.deepcopy(self)

