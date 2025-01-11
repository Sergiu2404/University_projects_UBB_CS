package mpi_coloring;

import mpi.MPI;
import utils.Colors;
import utils.Graph;

import java.util.Arrays;

public class GraphColoring {
    private static boolean solutionFound = false;

    public static void graphColoringMain(Graph graph) throws InterruptedException {
        int noColors = Colors.getNoColors();
        int[] codes = graphColoringRec(-1, graph, noColors, new int[graph.getNoNodes()], 0);

        if (codes[0] == -1 && !solutionFound)
            throw new RuntimeException("No solution found!");
    }

    private static int[] graphColoringRec(int solutionNode, Graph graph, int noColors, int[] solution, int mpiId) throws InterruptedException {
        int noNodes = graph.getNoNodes();

        if (!isCodeValid(solutionNode, solution, graph)) {
            return getInvalidSolution(noNodes);
        }
        if (solutionNode+1 == graph.getNoNodes()) {
            if (mpiId == 0) {
                System.out.println("Found solution: " + Colors.getNodesToColors(solution));
                solutionFound = true;
            }
            return solution;
        }

        int changeNode = solutionNode + 1;

        // Distribute work to workers
        for (int currentCode = 1; currentCode <= noColors; currentCode++) {
            if (currentCode != mpiId) {
                int[] nodeBuf = new int[]{changeNode};
                int[] codesBuf = getArrayCopy(solution);
                codesBuf[changeNode] = currentCode;

                int[] buf = new int[nodeBuf.length + codesBuf.length];
                System.arraycopy(nodeBuf, 0, buf, 0, nodeBuf.length);
                System.arraycopy(codesBuf, 0, buf, nodeBuf.length, codesBuf.length);

                MPI.COMM_WORLD.Isend(buf, 0, buf.length, MPI.INT, currentCode, 0);
            }
        }

        // Process current color
        if (mpiId != 0) {
            int[] nextCodes = getArrayCopy(solution);
            nextCodes[changeNode] = mpiId;
            int[] result = graphColoringRec(changeNode, graph, noColors, nextCodes, mpiId);
            if (result[0] != -1) {
                return result;
            }
        }

        // Collect results from workers
        for (int currentCode = 1; currentCode <= noColors; currentCode++) {
            if (currentCode != mpiId) {
                int[] buf = new int[noNodes+1];
                MPI.COMM_WORLD.Recv(buf, 0, noNodes+1, MPI.INT, currentCode, 0);

                int prevNode = buf[0];
                int[] codes = new int[noNodes];
                System.arraycopy(buf, 1, codes, 0, codes.length);

                if (mpiId != 0) {
                    int[] result = graphColoringRec(prevNode, graph, noColors, codes, mpiId);
                    if (result[0] != -1) {
                        return result;
                    }
                } else if (codes[0] != -1) {
                    System.out.println("Worker " + currentCode + " yielded solution: " + Colors.getNodesToColors(codes));
                    solutionFound = true;
                    // Continue searching for other solutions
                }
            }
        }

        return getInvalidSolution(noNodes);
    }

    public static void graphColoringWorker(int mpiId, Graph graph) throws InterruptedException {

        int noNodes = graph.getNoNodes();
        int noColors = Colors.getNoColors();

        int[] initialSolution = new int[noNodes+1];
        MPI.COMM_WORLD.Recv(initialSolution, 0, noNodes+1, MPI.INT, 0, 0);

        int prevNode = initialSolution[0];

        int[] initialCodes = new int[noNodes];

        System.arraycopy(initialSolution, 1, initialCodes, 0, initialCodes.length);

        int[] newCodes = graphColoringRec(prevNode, graph, noColors, initialCodes, mpiId);
        int[] buf = new int[noNodes+1];

        buf[0] = graph.getNoNodes()-1;

        System.arraycopy(newCodes, 0, buf, 1, newCodes.length);

        MPI.COMM_WORLD.Isend(buf, 0, noNodes+1, MPI.INT, 0, 0);
    }
    private static boolean isCodeValid(int node, int[] solution, Graph graph) {

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
