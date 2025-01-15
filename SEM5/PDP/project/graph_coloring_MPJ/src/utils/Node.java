package utils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Node {
    private final Integer id;
    private final Set<Node> NeighborNodes;

    public Node(Integer id) {
        this.id = id;
        this.NeighborNodes = new HashSet<>();
    }

    public Integer getId() {
        return id;
    }

    public Set<Node> getNeighborNodes() {
        return NeighborNodes;
    }

    public boolean isNeighbor(Node node) {

        return NeighborNodes.contains(node);
    }

    public void setNeighborNode(Node node) {
        this.NeighborNodes.add(node);
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
