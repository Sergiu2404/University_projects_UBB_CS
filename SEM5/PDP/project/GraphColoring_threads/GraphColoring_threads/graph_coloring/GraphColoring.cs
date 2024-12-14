using GraphColoring.entities;
using GraphColoring.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using GraphColoring;

namespace GraphColoring
{
    public class GraphColoring
    {
        public void GraphColoringMain(Graph graph)
        {
            int noColors = Colors.GetNoColors();

            int[] codes = GraphColoringRec(-1, graph, noColors, new int[graph.GetNoNodes()], 0);

            if (codes[0] == -1)
                throw new InvalidOperationException("No solution found!");
        }

        private int[] GraphColoringRec(int solutionNode, Graph graph, int noColors, int[] solution, int mpiId)
        {
            int noNodes = graph.GetNoNodes();

            if (!IsCodeValid(solutionNode, solution, graph))
            {
                return GetInvalidSolution(noNodes);
            }

            if (solutionNode + 1 == graph.GetNoNodes())
            {
                return solution;
            }

            int changeNode = solutionNode + 1;

            for (int currentCode = 1; currentCode <= noColors; currentCode++)
            {
                int destination = currentCode;

                if (currentCode != mpiId)
                {
                    int[] nodeBuf = new int[] { changeNode };
                    int[] codesBuf = solution.ToArray();
                    codesBuf[changeNode] = currentCode;

                    int[] buf = new int[nodeBuf.Length + codesBuf.Length];
                    Array.Copy(nodeBuf, 0, buf, 0, nodeBuf.Length);
                    Array.Copy(codesBuf, 0, buf, nodeBuf.Length, codesBuf.Length);

                    // Simulate MPI.Send (this would be replaced with actual MPI code)
                    // MPI.COMM_WORLD.Isend(buf, 0, buf.Length, MPI.INT, destination, 0);
                }
            }

            int[] result;

            if (mpiId != 0)
            {
                int[] nextCodes = solution.ToArray();
                nextCodes[changeNode] = mpiId;

                result = GraphColoringRec(changeNode, graph, noColors, nextCodes, mpiId);

                if (result[0] != -1)
                {
                    return result;
                }
            }

            for (int currentCode = 1; currentCode <= noColors; currentCode++)
            {
                int source = currentCode;

                if (currentCode != mpiId)
                {
                    int[] buf = new int[noNodes + 1];

                    // Simulate MPI.Recv (this would be replaced with actual MPI code)
                    // MPI.COMM_WORLD.Recv(buf, 0, noNodes + 1, MPI.INT, source, 0);

                    int prevNode = buf[0];
                    int[] codes = new int[noNodes];
                    Array.Copy(buf, 1, codes, 0, codes.Length);

                    if (mpiId != 0)
                    {
                        result = GraphColoringRec(prevNode, graph, noColors, codes, mpiId);
                        if (result[0] != -1)
                        {
                            return result;
                        }
                    }
                    else
                    {
                        if (codes[0] != -1)
                        {
                            Console.WriteLine($"Worker {source} yielded final solution {string.Join(", ", Colors.GetNodesToColors(codes))}");
                        }
                    }
                }
            }

            return GetInvalidSolution(noNodes);
        }

        public void GraphColoringWorker(int mpiId, Graph graph)
        {
            int noNodes = graph.GetNoNodes();
            int noColors = Colors.GetNoColors();


            int[] initialSolution = new int[noNodes + 1];

            // Simulate MPI.Recv (this would be replaced with actual MPI code)
            // MPI.COMM_WORLD.Recv(initialSolution, 0, noNodes + 1, MPI.INT, 0, 0);

            int prevNode = initialSolution[0];
            int[] initialCodes = new int[noNodes];
            Array.Copy(initialSolution, 1, initialCodes, 0, initialCodes.Length);

            int[] newCodes = GraphColoringRec(prevNode, graph, noColors, initialCodes, mpiId);

            int[] buf = new int[noNodes + 1];
            buf[0] = graph.GetNoNodes() - 1;
            Array.Copy(newCodes, 0, buf, 1, newCodes.Length);

            // Simulate MPI.Isend (this would be replaced with actual MPI code)
            // MPI.COMM_WORLD.Isend(buf, 0, noNodes + 1, MPI.INT, 0, 0);
        }

        private bool IsCodeValid(int node, int[] solution, Graph graph)
        {
            for (int currentNode = 0; currentNode < node; currentNode++)
            {
                if (graph.IsEdge(node, currentNode) && solution[node] == solution[currentNode])
                    return false;
            }

            return true;
        }

        private int[] GetInvalidSolution(int length)
        {
            int[] array = new int[length];
            Array.Fill(array, -1);
            return array;
        }
    }
}
