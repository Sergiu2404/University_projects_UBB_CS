//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import mpi.*;
import mpi_coloring.GraphColoring;
import utils.Colors;
import utils.Graph;


public class Main {
//    public static void main(String[] args) {
//        MPI.Init(args);
//        int me = MPI.COMM_WORLD.Rank();
//        int size = MPI.COMM_WORLD.Size();
//
//        System.out.println(String.format("Hi from <%s>. Total size is %d", me, size));
//        MPI.Finalize();
//    }


    static int nodeCount = 5;
    static int coloringDegree = 3;
    public static void main(String[] args) throws InterruptedException {

        MPI.Init(args);

        final String[] c = {"red", "green", "blue", "purple", "amber", "yellow", "black", "white", "orange", "pink"};

        int id = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

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

        System.out.println("graph: " + '\n' + graph);
        System.out.println("============================");

        for (int i = 1; i <= coloringDegree; i++) Colors.setColorName(i, c[i-1]);

        // Rationale: Each of the n processes (excluding the main process) will handle
        // the partial solutions ending in color k (1 <= k <= n)
        if (size-1 != Colors.getNoColors()) {
            throw new IllegalArgumentException("Number of processes must be number of colors + 1");
        }

        if (id == 0) {

            try {

                GraphColoring.graphColoringMain(graph);
            }
            catch (Exception gce) {
                gce.printStackTrace();
            }
        }
        else {

            GraphColoring.graphColoringWorker(id, graph);
        }

        MPI.Finalize();
    }
}