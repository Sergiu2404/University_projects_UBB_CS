
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace lab6_parallel_explore
{
    public class Graph
    {
        private readonly Dictionary<int, List<int>> adjList;

        public Graph()
        {
            adjList = new Dictionary<int, List<int>>();
        }

        public void AddEdge(int u, int v)
        {
            if (!adjList.ContainsKey(u))
                adjList[u] = new List<int>();
            adjList[u].Add(v);
        }

        public List<int> GetNeighbors(int vertex)
        {
            return adjList.ContainsKey(vertex) ? adjList[vertex] : new List<int>();
        }

        public IEnumerable<int> GetVertices()
        {
            return adjList.Keys;
        }
    }
}
