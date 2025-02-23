package threads_coloring;

import utils.Colors;
import utils.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class GraphColoringFuture {
    public static int[] graphColoringWorker(int id, Graph graph, int node, int[] initialSolution) throws InterruptedException, ExecutionException {

        int noColors = Colors.getNoColors();
        return graphColoringRec(id, graph, node, initialSolution, noColors);
    }


    private static int[] graphColoringRec(int id, Graph graph, int node, int[] solution, int noColors) throws InterruptedException, ExecutionException, ExecutionException {

        int noNodes = graph.getNoNodes();

        if (!isSolution(node, solution, graph)) {
            return getInvalidSolution(noNodes);
        }

        // return a solution if valid and complete until its last index
        if (node+1 == graph.getNoNodes()) {
            return solution;
        }

        //next index in the solution to be changed
        int changeNode = node + 1;


        List<Callable<int[]>> children = new ArrayList<>();


        //Send
        for(int currentCode = 1; currentCode <= noColors; currentCode++){

            if(currentCode != id){

                int[] codes = getArrayCopy(solution);
                codes[changeNode] = currentCode;


                int finalCurrentCode = currentCode;
                children.add(() -> graphColoringRec(finalCurrentCode, graph, changeNode, codes, noColors));

            } else {

                int[] nextCodes = getArrayCopy(solution);
                nextCodes[changeNode] = id;


                int[] result = graphColoringRec(id, graph, changeNode, nextCodes, noColors);

                if(result[0] != -1){
                    return result;
                }
            }
        }


        ExecutorService executor = Executors.newFixedThreadPool(noColors - 1);

        //Receive
        List<Future<int[]>> futures = executor.invokeAll(children);

        for(var future: futures){

            var result = future.get();
            if(result[0] != -1){
                return result;
            }
        }

        executor.shutdown();

        return getInvalidSolution(noColors);
    }
    
    private static boolean isSolution(int node, int[] solution, Graph graph) {

        for (int currentNode = 0; currentNode < node; currentNode++) {

            if (graph.isEdge(node, currentNode) && solution[node] == solution[currentNode]) return false;
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
