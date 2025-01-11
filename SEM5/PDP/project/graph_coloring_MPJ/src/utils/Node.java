package utils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Node {
    private final Integer id;
    private final Set<Node> adjacentNodes;

    public Node(Integer id) {
        this.id = id;
        this.adjacentNodes = new HashSet<>();
    }

    public Integer getId() {
        return id;
    }

    public Set<Node> getAdjacentNodes() {
        return adjacentNodes;
    }

    public boolean isAdjacent(Node node) {

        return adjacentNodes.contains(node);
    }

    public void setAdjacentNode(Node node) {
        this.adjacentNodes.add(node);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (!(o instanceof Node node))
            return false;

        return getId().equals(node.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
