package mpi_coloring;

import mpi.MPI;
import utils.Colors;
import utils.Graph;

import java.util.Arrays;

public class GraphColoring {
    private static boolean solutionFound = false;

    public static void graphColoringMain(Graph graph) throws InterruptedException {
        // Get the number of colors available
        int noColors = Colors.getNoColors();

        // Start the recursive graph coloring process. Begin with node -1 (indicating the start).
        // A solution array of size equal to the number of nodes is passed, initialized with 0s.
        // MPI ID is set to 0 for the main process initially.
        int[] codes = graphColoringRec(-1, graph, noColors, new int[graph.getNoNodes()], 0);

        // If no solution is found (first node is -1) and no solution has been found in any workers
        if (codes[0] == -1 && !solutionFound)
            throw new RuntimeException("No solution found");
    }

    private static int[] graphColoringRec(int solutionNode, Graph graph, int noColors, int[] solution, int mpiId) throws InterruptedException {
        int noNodes = graph.getNoNodes();

        // Check if the current solution is valid for the node that we just colored
        if (!isSolution(solutionNode, solution, graph)) {
            // If invalid, return an array filled with -1 (invalid solution)
            return getInvalidSolution(noNodes);
        }

        // If the solution has colored all the nodes, return the solution
        if (solutionNode + 1 == graph.getNoNodes()) {
            // If the MPI ID is 0 (main process), print the solution
            if (mpiId == 0) {
                System.out.println("Found solution: " + Colors.getNodesToColors(solution));
                solutionFound = true; // Mark the solution as found
            }
            return solution; // Return the solution if all nodes are colored correctly
        }

        // Increment the node index to process the next node
        int changeNode = solutionNode + 1;

        // Distribute work to other worker processes by sending them the current node and solution
        for (int currentCode = 1; currentCode <= noColors; currentCode++) {
            // Skip sending work to the current process (mpiId)
            if (currentCode != mpiId) {
                // Prepare a message (buffer) with the node index and the updated solution
                int[] nodeBuf = new int[]{changeNode};
                int[] codesBuf = getArrayCopy(solution);
                codesBuf[changeNode] = currentCode;

                // Combine the buffers and send them via MPI to the worker process
                int[] buf = new int[nodeBuf.length + codesBuf.length];
                System.arraycopy(nodeBuf, 0, buf, 0, nodeBuf.length);
                System.arraycopy(codesBuf, 0, buf, nodeBuf.length, codesBuf.length);
                MPI.COMM_WORLD.Isend(buf, 0, buf.length, MPI.INT, currentCode, 0);
            }
        }

        // Now handle the case where this process is coloring the current node (mpiId == currentCode)
        if (mpiId != 0) {
            // Make a copy of the current solution
            int[] nextCodes = getArrayCopy(solution);
            // Color the node with this process' ID (mpiId)
            nextCodes[changeNode] = mpiId;
            // Call the recursive coloring function for the next node
            int[] result = graphColoringRec(changeNode, graph, noColors, nextCodes, mpiId);
            // If a valid solution is found, return it
            if (result[0] != -1) {
                return result;
            }
        }

        // Collect results from other workers and check if any worker found a solution
        for (int currentCode = 1; currentCode <= noColors; currentCode++) {
            if (currentCode != mpiId) {
                // Receive the result (solution) from the worker process
                int[] buf = new int[noNodes + 1];
                MPI.COMM_WORLD.Recv(buf, 0, noNodes + 1, MPI.INT, currentCode, 0);

                // The first element of the buffer is the node index, and the rest is the coloring solution
                int prevNode = buf[0];
                int[] codes = new int[noNodes];
                System.arraycopy(buf, 1, codes, 0, codes.length);

                // If this process isn't the main one, recursively try to color the graph with this worker's solution
                if (mpiId != 0) {
                    int[] result = graphColoringRec(prevNode, graph, noColors, codes, mpiId);
                    if (result[0] != -1) {
                        return result;
                    }
                } else if (codes[0] != -1) {
                    // If the main process receives a valid solution, print it and mark it as found
                    System.out.println("Worker " + currentCode + " yielded solution: " + Colors.getNodesToColors(codes));
                    solutionFound = true;
                    // Continue searching for other solutions (not a termination condition)
                }
            }
        }

        // If no valid solution is found, return an invalid solution
        return getInvalidSolution(noNodes);
    }

    public static void graphColoringWorker(int mpiId, Graph graph) throws InterruptedException {
        int noNodes = graph.getNoNodes();
        int noColors = Colors.getNoColors();

        // Receive the initial solution array, which includes the current node and partial solution
        int[] initialSolution = new int[noNodes + 1];
        MPI.COMM_WORLD.Recv(initialSolution, 0, noNodes + 1, MPI.INT, 0, 0);

        // Get the node index and the current coloring solution
        int prevNode = initialSolution[0];
        int[] initialCodes = new int[noNodes];
        System.arraycopy(initialSolution, 1, initialCodes, 0, initialCodes.length);

        // Try to recursively solve the coloring from this worker's perspective
        int[] newCodes = graphColoringRec(prevNode, graph, noColors, initialCodes, mpiId);

        // Send the updated solution back to the main process
        int[] buf = new int[noNodes + 1];
        buf[0] = graph.getNoNodes() - 1;
        System.arraycopy(newCodes, 0, buf, 1, newCodes.length);

        MPI.COMM_WORLD.Isend(buf, 0, noNodes + 1, MPI.INT, 0, 0);
    }

    private static boolean isSolution(int node, int[] solution, Graph graph) {
        // Check if the current solution is valid for the given node
        for (int currentNode = 0; currentNode < node; currentNode++) {
            // If there is an edge and the nodes have the same color, the solution is invalid
            if (graph.isEdge(node, currentNode) && solution[node] == solution[currentNode])
                return false;
        }
        return true;
    }


    private static int[] getInvalidSolution(int length) {

        int[] array = new int[length];
        Arrays.fill(array, -1);

        return array;
    }

    private static int[] getArrayCopy(int[] array) {

        return Arrays.copyOf(array, array.length);
    }
}
