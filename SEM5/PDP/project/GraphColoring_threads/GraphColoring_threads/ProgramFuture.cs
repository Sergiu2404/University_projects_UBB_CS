using GraphColoring;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using GraphColoring.entities;
using GraphColoring.utils;

public class ProgramFuture
{
    static int nodeCount = 5;
    static int coloringDegree = 3;

    public static async Task Main(string[] args)
    {
        string[] colors = { "red", "green", "blue", "purple", "amber", "yellow", "black", "white", "orange", "pink" };

        var graph = new Graph(nodeCount);

        // Set edges for the graph
        graph.SetEdge(0, 1);
        graph.SetEdge(1, 2);
        graph.SetEdge(1, 4);
        graph.SetEdge(2, 0);
        graph.SetEdge(2, 3);
        graph.SetEdge(3, 1);
        graph.SetEdge(3, 4);
        graph.SetEdge(4, 0);

        // Set the number of colors and assign color names
        Colors.SetNoColors(coloringDegree);
        for (int i = 1; i <= coloringDegree; i++)
        {
            Colors.SetColorName(i, colors[i - 1]);
        }

        // List to hold tasks (replacing ExecutorService)
        var tasks = new List<Task<int[]>>();

        // Prepare tasks for each color to compute the graph coloring
        for (int i = 1; i <= coloringDegree; i++)
        {
            int[] index0Solution = new int[nodeCount];
            index0Solution[0] = i;

            int finalI = i;

            // Add task to the list
            tasks.Add(Task.Run(() => GraphColoringFuture.GraphColoringWorker(finalI, graph, 0, index0Solution)));
        }

        // Wait for all tasks to complete
        var results = await Task.WhenAll(tasks);

        // Output the results
        foreach (var result in results)
        {
            var colorAssignment = Colors.GetNodesToColors(result);

            foreach (var kvPair in colorAssignment)
            {
                //print all possible colorings of the graph
                Console.WriteLine($"Node {kvPair.Key}: {kvPair.Value}");
            }
        }
    }
}
