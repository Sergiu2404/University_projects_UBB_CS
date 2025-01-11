import threads_coloring.GraphColoringFuture;
import utils.Colors;
import utils.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main_Threads {
    static int nodeCount = 5;
    static int coloringDegree = 3;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        final String[] c = {"red", "green", "blue", "purple", "amber", "yellow", "black", "white", "orange", "pink"};

        var graph = new Graph(nodeCount);

        graph.setEdge(0,1);
        graph.setEdge(1,2);
        graph.setEdge(1,4);
        graph.setEdge(2,0);
        graph.setEdge(2,3);
        graph.setEdge(3,1);
        graph.setEdge(3,4);
        graph.setEdge(4,0);

        Colors.setNoColors(coloringDegree);

        System.out.println("graph: " + "\n" + graph);
        System.out.println("============================");

        for (int i = 1; i <= coloringDegree; i++) Colors.setColorName(i, c[i-1]);

        var executor = Executors.newFixedThreadPool(coloringDegree);

        List<Callable<int[]>> callables = new ArrayList<>();

        for(int i = 1; i <= coloringDegree; i++){

            int[] index_0_solution = new int[nodeCount];

            index_0_solution[0] = i;

            int finalI = i;

            callables.add(() -> GraphColoringFuture.graphColoringWorker(finalI, graph, 0, index_0_solution));

        }

        List<Future<int[]>> futures = executor.invokeAll(callables);

        for(var future: futures){
            System.out.println("Final solution: " + Colors.getNodesToColors(future.get()));
        }

        executor.shutdown();

    }
}
