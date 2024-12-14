using lab6_parallel_explore;
using System.Diagnostics;

class Program
{
    static void Main(string[] args)
    {
        var graph = new Graph();
        graph.AddEdge(1, 2);
        graph.AddEdge(2, 0);
        graph.AddEdge(0, 3);
        graph.AddEdge(3, 1);
        graph.AddEdge(3, 2);
        graph.AddEdge(2, 3);

        int startVertex = 0;
        var finder = new HamiltonianCycleFinder(graph, startVertex);

        var watch = Stopwatch.StartNew();
        var cycle = finder.FindHamiltonianCycle();
        watch.Stop();

        if (cycle != null)
        {
            Console.WriteLine($"Hamiltonian Cycle Found: {string.Join(" -> ", cycle)}");
        }
        else
        {
            Console.WriteLine("No Hamiltonian Cycle Found.");
        }

        Console.WriteLine($"Time Taken: {watch.ElapsedMilliseconds} ms");
    }
}