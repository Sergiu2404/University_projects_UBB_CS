package mpi_coloring;

import mpi.MPI;
import utils.Colors;
import utils.Graph;

import java.util.Arrays;

public class GraphColoring {
    private static boolean solutionFound = false;

    public static void graphColoringMain(Graph graph) throws InterruptedException {
        int noColors = Colors.getNoColors();

        // start rec graph coloring process, begin with node -1 (indicating the start)
        // s sol array of size equal to the number of nodes is passed, initialized with 0s
        // MPI ID is 0 for the main process initially.
        int[] codes = graphColoringRec(-1, graph, noColors, new int[graph.getNoNodes()], 0);

        // if no solution is found (first node is -1) and no solution has been found in any workers
        if (codes[0] == -1 && !solutionFound)
            throw new RuntimeException("No solution found");
    }

    private static int[] graphColoringRec(int solutionNode, Graph graph, int noColors, int[] solution, int mpiId) throws InterruptedException {
        int noNodes = graph.getNoNodes();

        // check if current solution is valid for the node that we just colored
        if (!isSolution(solutionNode, solution, graph)) {
            // if invalid return array filled with -1 (invalid sol)
            return getInvalidSolution(noNodes);
        }

        // If sol has colored all the nodes, return the sol
        if (solutionNode + 1 == graph.getNoNodes()) {
            // if MPI ID 0, print the sol
            if (mpiId == 0) {
                System.out.println("Found solution: " + Colors.getNodesToColors(solution));
                solutionFound = true;
            }
            return solution; // ret solution if all nodes are colored correctly
        }

        // inc the node index to process the next node
        int changeNode = solutionNode + 1;

        // share work to other worker processes by sending them the current node and solution
        for (int currentCode = 1; currentCode <= noColors; currentCode++) {
            // dont send work to the current process mpiId
            if (currentCode != mpiId) {
                // message (buffer) with the node index and the updated solution
                int[] nodeBuf = new int[]{changeNode};
                int[] codesBuf = getArrayCopy(solution);
                codesBuf[changeNode] = currentCode;

                // combine the buffers and send them via MPI to the worker process
                int[] buf = new int[nodeBuf.length + codesBuf.length];
                System.arraycopy(nodeBuf, 0, buf, 0, nodeBuf.length);
                System.arraycopy(codesBuf, 0, buf, nodeBuf.length, codesBuf.length);
                MPI.COMM_WORLD.Isend(buf, 0, buf.length, MPI.INT, currentCode, 0);
            }
        }

        // handle case where this proc is coloring the current node (mpiId == currentCode)
        if (mpiId != 0) {
            int[] nextCodes = getArrayCopy(solution);
            // Color the node with id mpiId
            nextCodes[changeNode] = mpiId;
            // call recursive coloring function for the next node
            int[] result = graphColoringRec(changeNode, graph, noColors, nextCodes, mpiId);

            if (result[0] != -1) {
                return result;
            }
        }

        // collect results from other workers and check if any worker found a solution
        for (int currentCode = 1; currentCode <= noColors; currentCode++) {
            if (currentCode != mpiId) {
                // receive the result (solution) from the worker process
                int[] buf = new int[noNodes + 1];
                MPI.COMM_WORLD.Recv(buf, 0, noNodes + 1, MPI.INT, currentCode, 0);

                // the first element of the buffer is the node index, and the rest is the coloring solution
                int prevNode = buf[0];
                int[] codes = new int[noNodes];
                System.arraycopy(buf, 1, codes, 0, codes.length);

                // if this process isnt the main, recursively try to color the graph with this worker's sol
                if (mpiId != 0) {
                    int[] result = graphColoringRec(prevNode, graph, noColors, codes, mpiId);
                    if (result[0] != -1) {
                        return result;
                    }
                } else if (codes[0] != -1) {
                    System.out.println("Worker " + currentCode + " yielded solution: " + Colors.getNodesToColors(codes));
                    solutionFound = true;
                }
            }
        }

        return getInvalidSolution(noNodes);
    }

    public static void graphColoringWorker(int mpiId, Graph graph) throws InterruptedException {
        int noNodes = graph.getNoNodes();
        int noColors = Colors.getNoColors();

        int[] initialSolution = new int[noNodes + 1];
        MPI.COMM_WORLD.Recv(initialSolution, 0, noNodes + 1, MPI.INT, 0, 0);

        int prevNode = initialSolution[0];
        int[] initialCodes = new int[noNodes];
        System.arraycopy(initialSolution, 1, initialCodes, 0, initialCodes.length);

        int[] newCodes = graphColoringRec(prevNode, graph, noColors, initialCodes, mpiId);

        int[] buf = new int[noNodes + 1];
        buf[0] = graph.getNoNodes() - 1;
        System.arraycopy(newCodes, 0, buf, 1, newCodes.length);

        MPI.COMM_WORLD.Isend(buf, 0, noNodes + 1, MPI.INT, 0, 0);
    }

    private static boolean isSolution(int node, int[] solution, Graph graph) {
        for (int currentNode = 0; currentNode < node; currentNode++) {
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
