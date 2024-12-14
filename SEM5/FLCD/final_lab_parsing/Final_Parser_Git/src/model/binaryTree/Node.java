package model.binaryTree;

public class Node {
    private String value;
    private Node left;
    private Node right;
    private Node parent;

    public Node() {
    }

    public Node(String value, Node parent) {
        this.value = value;
        this.right = null;
        this.left = null;
        this.parent = parent;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
