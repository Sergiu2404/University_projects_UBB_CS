using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GraphColoring.utils
{
    public class Graph
    {
        private readonly HashSet<Node> nodes;
        private readonly int threadCount = 16;

        public Graph(int size)
        {
            nodes = new HashSet<Node>(size);

            for (int id = 0; id < size; id++)
            {
                nodes.Add(new Node(id));
            }
        }

        public void SetEdge(int n1, int n2)
        {
            Node node1 = nodes.FirstOrDefault(n => n.Id == n1);
            Node node2 = nodes.FirstOrDefault(n => n.Id == n2);

            node1?.SetAdjacentNode(node2);
            node2?.SetAdjacentNode(node1);
        }

        public bool IsEdge(int n1, int n2)
        {
            Node node1 = nodes.FirstOrDefault(n => n.Id == n1);
            Node node2 = nodes.FirstOrDefault(n => n.Id == n2);

            return node1?.IsAdjacent(node2) ?? false;
        }

        public List<Node> GetNodes()
        {
            return nodes.OrderBy(n => n).ToList();
        }

        public Node GetNodeById(int id)
        {
            return nodes.FirstOrDefault(n => n.Id == id);
        }

        public int GetNoNodes()
        {
            return nodes.Count;
        }
    }
}
