using GraphColoring.entities;
using GraphColoring.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GraphColoring
{
    public class GraphColoringFuture
    {
        public static async Task<int[]> GraphColoringWorker(int id, Graph graph, int node, int[] initialSolution)
        {
            int noColors = Colors.GetNoColors();
            return await GraphColoringRec(id, graph, node, initialSolution, noColors);
        }

        private static async Task<int[]> GraphColoringRec(int id, Graph graph, int node, int[] solution, int noColors)
        {
            int noNodes = graph.GetNoNodes();

            // If a solution is invalid, we invalidate it.
            if (!IsCodeValid(node, solution, graph))
            {
                return GetInvalidSolution(noNodes);
            }

            // If a solution is valid and complete until its last index, we return it.
            if (node + 1 == graph.GetNoNodes())
            {
                return solution;
            }

            // changeNode - The next index in the solution that needs to be changed
            int changeNode = node + 1;

            List<Task<int[]>> children = new List<Task<int[]>>();

            // Send
            for (int currentCode = 1; currentCode <= noColors; currentCode++)
            {
                if (currentCode != id)
                {
                    int[] codes = GetArrayCopy(solution);
                    codes[changeNode] = currentCode;

                    int finalCurrentCode = currentCode;
                    children.Add(Task.Run(() => GraphColoringRec(finalCurrentCode, graph, changeNode, codes, noColors)));
                }
                else
                {
                    int[] nextCodes = GetArrayCopy(solution);
                    nextCodes[changeNode] = id;

                    int[] result = await GraphColoringRec(id, graph, changeNode, nextCodes, noColors);

                    if (result[0] != -1)
                    {
                        return result;
                    }
                }
            }

            // Receive
            var results = await Task.WhenAll(children);

            foreach (var result in results)
            {
                if (result[0] != -1)
                {
                    return result;
                }
            }

            return GetInvalidSolution(noColors);
        }

        private static bool IsCodeValid(int node, int[] solution, Graph graph)
        {
            for (int currentNode = 0; currentNode < node; currentNode++)
            {
                if (graph.IsEdge(node, currentNode) && solution[node] == solution[currentNode]) return false;
            }
            return true;
        }

        private static int[] GetInvalidSolution(int length)
        {
            int[] array = new int[length];
            Array.Fill(array, -1);
            return array;
        }

        private static int[] GetArrayCopy(int[] array)
        {
            return (int[])array.Clone();
        }
    }
}
