public class BSTSymbolTable implements SymbolTableInterface {
    private class Node {
        String term;
        Node left, right;

        public Node(String term) {
            this.term = term;
            left = right = null;
        }
    }

    private Node root;
    private int size;

    public BSTSymbolTable() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public boolean add(String term) {
        if (containsTerm(term)) return false;
        root = addRecursive(root, term);
        size++;
        return true;
    }

    private Node addRecursive(Node node, String term) {
        if (node == null) {
            return new Node(term);
        }
        if (term.compareTo(node.term) < 0) {
            node.left = addRecursive(node.left, term);
        } else if (term.compareTo(node.term) > 0) {
            node.right = addRecursive(node.right, term);
        }
        return node;
    }

    @Override
    public boolean containsTerm(String term) {
        return containsRecursive(root, term);
    }

    private boolean containsRecursive(Node node, String term) {
        if (node == null) return false;
        if (term.equals(node.term)) return true;
        return term.compareTo(node.term) < 0
                ? containsRecursive(node.left, term)
                : containsRecursive(node.right, term);
    }

    @Override
    public String findByPos(Pair pos) {
        throw new UnsupportedOperationException("BST does not support positional access.");
    }

    @Override
    public Pair findPositionOfTerm(String term) {
        // BSTs are more complex to return positions.
        // Skipping implementation for now.
        return null;
    }

    @Override
    public Integer getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "BSTSymbolTable: " + inOrderTraversal(root);
    }

    private String inOrderTraversal(Node node) {
        if (node == null) return "";
        return inOrderTraversal(node.left) + node.term + " " + inOrderTraversal(node.right);
    }
}
