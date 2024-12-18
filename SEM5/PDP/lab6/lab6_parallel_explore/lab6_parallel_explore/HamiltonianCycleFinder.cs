using System.Collections.Concurrent;
using System.Linq;
using System.Threading.Tasks;

namespace lab6_parallel_explore
{
    public class HamiltonianCycleFinder
    {
        private readonly Graph graph;
        private readonly int startVertex;
        private List<int> cycleFound;
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
            // If reached nr of vertices, check if the last vertex connects to the start vertex
            if (path.Count == graph.GetVertices().Count())
            {
                if (graph.GetNeighbors(path.Last()).Contains(startVertex))
                {
                    path.Add(startVertex);  // complete cycle
                    cyclesFound.Add(new List<int>(path));
                }
                return;
            }

            var neighbors = graph.GetNeighbors(path.Last());

            // task for each unvisited neighbor
            var tasks = neighbors.Where(n => !visited.Contains(n))
                                 .Select(neighbor => Task.Run(() =>
                                 {
                                     var newVisited = new HashSet<int>(visited);
                                     var newPath = new List<int>(path);

                                     newPath.Add(neighbor);
                                     newVisited.Add(neighbor);

                                     Backtrack(newPath, newVisited);
                                 }))
                                 .ToList();

            Task.WhenAll(tasks).Wait();

            if (cyclesFound.Any())
            {
                return;
            }
        }
    }
}
