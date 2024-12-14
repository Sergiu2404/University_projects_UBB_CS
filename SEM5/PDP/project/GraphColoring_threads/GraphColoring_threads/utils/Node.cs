using System;
using System.Collections.Generic;

public class Node
{
    public int Id { get; }
    private readonly HashSet<Node> adjacentNodes;

    public Node(int id)
    {
        Id = id;
        adjacentNodes = new HashSet<Node>();
    }

    public HashSet<Node> GetAdjacentNodes()
    {
        return adjacentNodes;
    }

    public bool IsAdjacent(Node node)
    {
        return adjacentNodes.Contains(node);
    }

    public void SetAdjacentNode(Node node)
    {
        adjacentNodes.Add(node);
    }

    public override bool Equals(object obj)
    {
        if (this == obj)
            return true;

        if (obj is not Node node)
            return false;

        return Id == node.Id;
    }

    public override int GetHashCode()
    {
        return Id.GetHashCode();
    }
}
