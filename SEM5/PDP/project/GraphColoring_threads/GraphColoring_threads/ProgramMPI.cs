using MPI;
using System;
using GraphColoring;
using GraphColoring.entities;
using GraphColoring.utils;

public class ProgramMPI
{
    static int nodeCount = 5;
    static int coloringDegree = 3;

    public static void MPI_Main(string[] args)
    {
        // Initialize MPI
        using (MPI.Environment mpiEnv = new MPI.Environment(ref args))
        {
            // Communicator to handle MPI ranks
            Intracommunicator comm = Communicator.world;

            // Get the rank and size of the process
            int id = comm.Rank;
            int size = comm.Size;

            Console.WriteLine($"Num of processes (size): {size}");

            // Setup color names
            string[] colors = { "red", "green", "blue", "purple", "amber", "yellow", "black", "white", "orange", "pink" };

            // Initialize the graph and edges
            var graph = new Graph(nodeCount);

            graph.SetEdge(0, 1);
            graph.SetEdge(1, 2);
            graph.SetEdge(1, 4);
            graph.SetEdge(2, 0);
            graph.SetEdge(2, 3);
            graph.SetEdge(3, 1);
            graph.SetEdge(3, 4);
            graph.SetEdge(4, 0);

            // Set the number of colors and assign names to the colors
            Colors.SetNoColors(coloringDegree);
            for (int i = 1; i <= coloringDegree; i++)
                Colors.SetColorName(i, colors[i - 1]);

            // Verify that the number of processes matches the number of colors
            if (size - 1 != Colors.GetNoColors())
            {
                Console.WriteLine("Error: The number of processes (excluding the main process) must match the number of colors.");
                return;
            }

            // Create an instance of the GraphColoring class
            var graphColoring = new GraphColoring.GraphColoring();

            // If this is the main process (rank 0), handle the main graph coloring logic
            if (id == 0)
            {
                try
                {
                    graphColoring.GraphColoringMain(graph);
                }
                catch (Exception e)
                {
                    Console.WriteLine("Exception occurred during graph coloring: " + e.Message);
                }
            }
            else
            {
                // Worker processes handle the coloring work for their respective color
                Console.WriteLine($"Graph coloring {id}");
                graphColoring.GraphColoringWorker(id, graph);
            }
        } // Finalize MPI automatically at the end of the using block
    }
}
