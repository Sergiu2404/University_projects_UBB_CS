package utils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Graph {
    private final Set<Node> nodes;
    
    public Graph(int size) {

        nodes = new HashSet<>(size);

        for (int id = 0; id < size; id++) {

            nodes.add(new Node(id));
        }
    }

    public void addEdge(Integer n1, Integer n2) {

        Node node1 = nodes.stream().filter((n) -> Objects.equals(n.getId(), n1)).findFirst().get();
        Node node2 = nodes.stream().filter((n) -> Objects.equals(n.getId(), n2)).findFirst().get();

        node1.setNeighborNode(node2);
        node2.setNeighborNode(node1);
    }

    public boolean isEdge(Integer n1, Integer n2) {

        Node node1 = nodes.stream().filter((n) -> Objects.equals(n.getId(), n1)).findFirst().get();
        Node node2 = nodes.stream().filter((n) -> Objects.equals(n.getId(), n2)).findFirst().get();

        return node1.isNeighbor(node2);
    }

    public int getNoNodes() {

        return nodes.size();
    }

    @Override
    public String toString() {
        return nodes.stream()
                .map(node -> "Node " + (node.getId() + 1) + " -> " + node.getNeighborNodes().stream()
                        .map(Node::getId)
                        .sorted()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", ")))
                .sorted()
                .collect(Collectors.joining("\n"));
    }
}
