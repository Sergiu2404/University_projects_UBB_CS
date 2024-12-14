package model.binaryTree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class BinaryTree implements Iterable<Node> {
    private Node root;

    public BinaryTree() {
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    private Node addRecursive(String value, Node parent, Node current) {
        if (current == null) {
            return new Node(value, parent);
        }

        if (value.compareTo(current.getValue()) < 0) {
            current.setLeft(addRecursive(value, current, current.getLeft()));
        } else if (value.compareTo(current.getValue()) > 0) {
            current.setRight(addRecursive(value, current, current.getRight()));
        } else {
            // value already exists
            return current;
        }

        return current;
    }

    public void add(String value) {
        root = addRecursive(value, null, root);
    }

    private boolean containsRecursive(Node current, String value) {
        if (current == null) {
            return false;
        }
        if (value.equals(current.getValue())) {
            return true;
        }
        return value.compareTo(current.getValue()) < 0
                ? containsRecursive(current.getLeft(), value)
                : containsRecursive(current.getRight(), value);
    }

    public boolean contains(String value) {
        return containsRecursive(root, value);
    }

    private String findSmallestValue(Node root) {
        return findLeftmostNode(root).getValue();
    }

    private Node findLeftmostNode(Node root) {
        return root.getLeft() == null ? root : findLeftmostNode(root.getLeft());
    }

    private Node deleteRecursive(Node current, String value) {
        if (current == null) {
            return null;
        }

        if (value == current.getValue()) {
            // Node to delete found

            // if node has no children
            if (current.getLeft() == null && current.getRight() == null) {
                return null;
            }
            // if node has only one child
            else if (current.getRight() == null) {
                return current.getLeft();
            } else if (current.getLeft() == null) {
                return current.getRight();
            } else {
                // if node has two children
                String smallestValue = findSmallestValue(current.getRight());
                current.setValue(smallestValue);
                current.setRight(deleteRecursive(current.getRight(), smallestValue));
                return current;
            }
        }

        if (value.compareTo(current.getValue()) < 0) {
            current.setLeft(deleteRecursive(current.getLeft(), value));
            return current;
        }
        current.setRight(deleteRecursive(current.getRight(), value));
        return current;
    }

    public void delete(String value) {
        root = deleteRecursive(root, value);
    }

    public int indexOf(String value) {
        int index = 0;

        for (Node node : this) {
            if (!node.getValue().equals(value)) {
                index++;
            } else {
                return index;
            }
        }

        return -1;
    }

    public String get(Integer position) {
        int index = 0;
        for (Node node : this) {
            if (index == position) {
                return node.getValue();
            }

            index++;
        }

        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        List<String> values = new ArrayList<>();

        for (Node node : this) {
            values.add(node.getValue());
        }

        return values.toString();
    }

    @Override
    public Iterator<Node> iterator() {
        return new Iterator<Node>() {
            private Node next = findLeftmostNode(root);

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public Node next() {
                if (!hasNext()) throw new NoSuchElementException();
                Node r = next;

                // If you can walk right, walk right, then fully left.
                // Otherwise, walk up until you come from left.
                if (next.getRight() != null) {
                    next = next.getRight();
                    while (next.getLeft() != null)
                        next = next.getLeft();
                    return r;
                }

                while (true) {
                    if (next.getParent() == null) {
                        next = null;
                        return r;
                    }
                    if (next.getParent().getLeft() == next) {
                        next = next.getParent();
                        return r;
                    }
                    next = next.getParent();
                }
            }
        };
    }
}
