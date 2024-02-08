import copy
import queue
from queue import PriorityQueue
import heapq

class Graph:
    def __init__(self, numberVertices, numberEdges):

        self.__numberVertices = numberVertices
        self.__numberEdges = numberEdges

        self.__in = {}
        self.__out = {}
        self.__dictionary_cost = {}

        for i in range(numberVertices):
            self.__in[i] = []
            self.__out[i] = []


    def write_graph_to_file(self,graph, file_name):

        file = open(file_name, "w")
        first_line = str(graph.__numberVertices) + ' ' + str(graph.__numberEdges) + '\n'
        file.write(first_line)

        print("Intrat")

        for edge in graph.__dictionary_cost.keys():
            new_line = "{}->{}: {}\n".format(edge[0], edge[1], graph.__dictionary_cost[edge])
            #new_line = str(edge[0]) + " " + str(edge[1]) + " " + str(graph.__dictionary_cost[edge]) + "\n"

            file.write(new_line)

        print("Isolated vertices: ")

        for vertex in graph.__in.keys():
            if len(graph.__in[vertex]) == 0 and len(graph.__out[vertex]) == 0:
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
                graph.__out[vertex2] = []

            else:
                graph.__in[vertex2].append(vertex1)
                graph.__out[vertex1].append(vertex2)
                graph.__dictionary_cost[(vertex1, vertex2)]= cost

            line = file.readline().strip()


        file.close()

        return graph


    def list_costs(self):
        for key in self.dictionary_cost():
            line = "({}, {})".format(key[0], key[1]) + " :" + str(self.__dictionary_cost[key])
            print(line)


    def dictionary_cost(self):
        return self.__dictionary_cost


    def dictionary_in(self):
        return self.__in


    def dictionary_out(self):
        return self.__out


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


    def parse_outbound(self, vertex):
        #return [y for y in self.__out[vertex]]
        return list(self.__out[vertex])


    def parse_cost(self):
        # edges = list(self.__dictionary_cost.keys())
        # return [edge for edge in edges]
        return list(self.__dictionary_cost.keys())


    def add_vertex(self, x):
        if x in self.__in.keys() or x in self.__out.keys():
            return False
        self.__in[x] = []
        self.__out[x] = []
        self.__numberVertices += 1
        return True


    def remove_vertex(self, vertex):
        if vertex not in self.__in.keys() or vertex not in self.__out.keys():
            return False
        self.__in.pop(vertex)
        self.__out.pop(vertex)

        for key in self.__in.keys():
            if vertex in self.__in[key]:
                self.__in[key].remove(vertex)
            elif vertex in self.__out[key]:
                self.__out[key].remove(vertex)
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
        return len(self.__in[vertex])


    def out_degree(self, vertex):
        if vertex not in self.__out.keys():
            return False
        return len(self.__out[vertex])


    def add_edge(self, vertex1, vertex2, cost):

        if vertex1 in self.__in[vertex2] or vertex2 in self.__out[vertex1] or (vertex1, vertex2) in self.__dictionary_cost.keys():
            return False
        self.__in[vertex2].append(vertex1)
        self.__out[vertex1].append(vertex2)
        self.__dictionary_cost[(vertex1, vertex2)] = cost
        self.__numberEdges = self.__numberEdges + 1
        return True


    def remove_edge(self, vertex1, vertex2):

        if vertex1 not in self.__in.keys() or vertex2 not in self.__in.keys() or vertex1 not in self.__out.keys() or vertex2 not in self.__out.keys() or vertex1 not in self.__in[vertex2] or vertex2 not in self.__out[vertex1] or (vertex1, vertex2) not in self.__dictionary_cost.keys():
            return False

        self.__in[vertex2].remove(vertex1)
        self.__out[vertex1].remove(vertex2)
        self.__dictionary_cost.pop((vertex1, vertex2))
        self.__numberEdges -= 1

        return True


    def find_if_edge(self, vertex1, vertex2):
        if vertex1 in self.__in[vertex2] or vertex2 in self.__out[vertex1]:
            return self.__dictionary_cost[(vertex1, vertex2)]
        return False


    def update_cost(self, vertex1, vertex2, cost):
        if (vertex1, vertex2) not in self.__dictionary_cost.keys():
            return False
        self.__dictionary_cost[(vertex1, vertex2)] = cost
        return True

    def print_to_console(self):
        for edge in self.__dictionary_cost.keys():
            new_line = str(edge[0]) + "->" + str(edge[1]) + ": " + str(self.__dictionary_cost[edge]) + "\n"
            print(new_line)

        print("Isolated vertices: ")
        for vertex in self.__in.keys():
            if len(self.__in[vertex]) == 0 and len(self.__out[vertex]) == 0:
                new_line = "{}\n".format(vertex)
                print(new_line)

    # def backwards_dijkstra(self, start, end):
    #     # Initialize the distance dictionary and priority queue
    #     distance = {end: 0}
    #     queue = [(0, end)]
    #
    #     # Traverse the graph using Dijkstra's algorithm
    #     while queue:
    #         # Pop the vertex with the smallest distance
    #         cost, vertex = heapq.heappop(queue)
    #
    #         # Check if the current vertex is the start vertex
    #         if vertex == start:
    #             # Construct the path by backtracking from the start vertex to the end vertex
    #             path = [start]
    #             while path[-1] != end:
    #                 path.append(min(self.__in[path[-1]], key=lambda x: distance.get(x, float('inf')), default=end))
    #             return path[::-1]
    #
    #         # Update the distance of each neighboring vertex
    #         for neighbor in self.__in[vertex]:
    #             weight = self.__dictionary_cost.get((neighbor, vertex))
    #             new_distance = distance.get(vertex, float('inf')) + weight
    #             if new_distance < distance.get(neighbor, float('inf')):
    #                 distance[neighbor] = new_distance
    #                 heapq.heappush(queue, (new_distance, neighbor))
    #
    #     # If no path is found, return None
    #     return None

    def backwards_dijkstra(self, source, target):
        '''

        :param source:
        :param target:
        :return:
        '''
        distances = {v: float('inf') for v in range(self.__numberVertices)} #dictionary with every vertex as a key, and intial value infinity
        # for every key
        distances[target] = 0

        visited = set() #every visited node, only once(set)

        pq = PriorityQueue() #using priority queue, because when geting/removing element from priority queue, we get the
        #element with the highest priority(with the smallest value from the first element of the tuple/if value is the same => order matters)
        pq.put((distances[target], target)) #we start from the target node(backwards)

        parent = {} #dictionary of all the predecessors vertices for every vertex

        while not pq.empty(): #while tuple exists in queue
            (dist, current_vertex) = pq.get() #dist=first element of the tuple(with the smallest value),
            # current_vertex=second element of the tuple,
            # and then removes the element from the queue
            if current_vertex == source:
                break #exit the loop when arriving to the source vertex

            visited.add(current_vertex) #mark vertex as visited

            for neighbor in self.__in[current_vertex]: #get every neighbor of current vertex
                if neighbor in visited: #if vertex already visited, pass to the next neighbor
                    continue
                if neighbor not in distances: #if neighbor is not found between the keys of the distances dictionary
                    distances[neighbor] = float('inf') #initialize value of key neighbor with infinity
                new_distance = distances[current_vertex] + self.__dictionary_cost[(neighbor,current_vertex)] #add to the distance
                #the previous distance between the vertices
                if new_distance < distances[neighbor]: #if the new_distance is less than the value of key neighbor from distances,
                    #then change the value of the key neighbor, add the new distance and the coresponding neighbor to the priority queue
                    #and then give the value of the key neighbor to the current vertex
                    distances[neighbor] = new_distance
                    pq.put((new_distance, neighbor))
                    parent[neighbor] = current_vertex

        path = [] #path to add the edges
        costs=[] #costs coresponding to all the edges
        current_vertex = source #start from source vertex
        while current_vertex != target: #while until vertex is target
            try:
                next_vertex = parent[current_vertex] #get the succesor of the current vertex
            except KeyError:
                return None
            path.append((current_vertex,next_vertex)) #add edge to the path
            costs.append(self.__dictionary_cost[(current_vertex,next_vertex)]) #add cost coresponding to the edge
            current_vertex = next_vertex #pass to the next vertex

        #path.reverse()
        #costs.reverse() uncomment this if it does not work

        return path , costs


    #
    # def backwards_dijkstra(self,start_vertex,finish_vertex):
    #
    #     dist={finish_vertex:0}
    #     pq=PriorityQueue()
    #     pq.put((0,finish_vertex))
    #
    #     while not pq.empty():
    #         distance,vertex=pq.get() #gets vertex with smallest distance and removes it from the queue
    #
    #         if vertex==start_vertex:
    #             break
    #
    #         for neighbor in self.__out[vertex]:
    #
    #             cost=self.__dictionary_cost[vertex,neighbor]
    #             new_distance=distance+cost
    #
    #             if neighbor not in dist or new_distance<dist[neighbor]:
    #                 dist[neighbor]=new_distance
    #                 pq.put((new_distance,neighbor))
    #
    #
    #     walk=[start_vertex]
    #
    #     while walk[-1] != finish_vertex:
    #         last=walk[-1]
    #         neighbor= min(self.__in[walk[-1]],key=lambda x: dist.get(x,float('inf')),default=finish_vertex)
    #         walk.append(neighbor)
    #
    #     walk.reverse()
    #     #return walk
    #     for elem in walk:
    #         print(elem,end=" ")
    #     print()


    # def make_copy(self):
    #     return copy.deepcopy(self)

