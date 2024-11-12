using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace lab6_parallel_explore
{
    public class HamiltonianCycleFinder
    {
        private readonly Graph graph;
        private readonly int startVertex;
        private readonly ConcurrentBag<List<int>> cyclesFound;

        public HamiltonianCycleFinder(Graph graph, int startVertex)
        {
            this.graph = graph;
            this.startVertex = startVertex;
            cyclesFound = new ConcurrentBag<List<int>>();
        }

        public List<int> FindHamiltonianCycle()
        {
            List<int> path = new List<int> { startVertex };
            HashSet<int> visited = new HashSet<int> { startVertex };
            Backtrack(path, visited);
            return cyclesFound.FirstOrDefault();
        }

        private void Backtrack(List<int> path, HashSet<int> visited)
        {
            if (path.Count == graph.GetVertices().Count())
            {
                // Check if there's an edge back to the starting vertex
                if (graph.GetNeighbors(path.Last()).Contains(startVertex))
                {
                    path.Add(startVertex); // Complete the cycle
                    cyclesFound.Add(new List<int>(path));
                    path.RemoveAt(path.Count - 1); // Backtrack
                }
                return;
            }

            var neighbors = graph.GetNeighbors(path.Last());

            // Prepare tasks for each neighbor
            Parallel.ForEach(neighbors.Where(n => !visited.Contains(n)), neighbor =>
            {
                lock (visited) // Lock to modify the shared visited set
                {
                    path.Add(neighbor);
                    visited.Add(neighbor);
                }

                Backtrack(path, visited);

                lock (visited) // Lock to revert the visited set
                {
                    visited.Remove(neighbor);
                    path.RemoveAt(path.Count - 1);
                }
            });
        }
    }

}
