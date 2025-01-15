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

        final String[] colors = {"purple", "black", "white", "red", "green", "orange", "pink"};
        Colors.setNoColors(coloringDegree);


        var graph = new Graph(nodeCount);

        graph.addEdge(0,1);
        graph.addEdge(1,2);
        graph.addEdge(1,4);
        graph.addEdge(2,0);
        graph.addEdge(2,3);
        graph.addEdge(3,1);
        graph.addEdge(3,4);
        graph.addEdge(4,0);

        System.out.println("graph: " + "\n" + graph);
        System.out.println("============================");

        for (int i = 1; i <= coloringDegree; i++) Colors.setColorName(i, colors[i-1]);

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
