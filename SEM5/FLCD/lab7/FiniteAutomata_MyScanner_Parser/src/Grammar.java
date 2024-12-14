import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Node {
    String value;
    Node parent;
    Node sibling;
    List<Node> children;

    Node(String value) {
        this.value = value;
        this.children = new ArrayList<>();
    }
}



public class Grammar {

    private List<String> nonterminals;
    private List<String> terminals;
    private Map<String, List<String>> productions;
    private String startSymbol;

    public Grammar() {
        this.nonterminals = new ArrayList<>();
        this.terminals = new ArrayList<>();
        this.productions = new HashMap<>();
        this.startSymbol = null;
    }

    public String getStartSymbol(){ return startSymbol; }
    public Node getRootNode() {
        if (startSymbol == null) {
            System.out.println("No start symbol defined in the grammar.");
            return null;
        }
        return buildTree(startSymbol);
    }

    public List<String> getNonterminals() {
        return this.nonterminals;
    }

    // Getter for productions
    public Map<String, List<String>> getProductions() {
        return this.productions;
    }

    public void readFromFile(String filePath) {
        try (Scanner fileScanner = new Scanner(new File(filePath))) {
            String[] nonterminalLine = fileScanner.nextLine().trim().split("\\s+");
            String[] terminalLine = fileScanner.nextLine().trim().split("\\s+");
            this.startSymbol = fileScanner.nextLine().trim();

            this.nonterminals.addAll(Arrays.asList(nonterminalLine));
            this.terminals.addAll(Arrays.asList(terminalLine));

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("->");
                if (parts.length == 2) {
                    String left = parts[0].trim();
                    String[] rightAlternatives = parts[1].trim().split("\\|");
                    this.productions.putIfAbsent(left, new ArrayList<>());

                    for (String alternative : rightAlternatives) {
                        this.productions.get(left).add(alternative.trim());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        }
    }


    public void printNonterminals() {
        System.out.println("Nonterminals: " + nonterminals);
    }

    public void printTerminals() {
        System.out.println("Terminals: " + terminals);
    }

    public void printProductions() {
        for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
            String nonterminal = entry.getKey();
            List<String> rules = entry.getValue();
            for (String rule : rules) {
                System.out.println(nonterminal + " -> " + rule);
            }
        }
    }

    public void printProductionsForNonterminal(String nonterminal) {
        if (productions.containsKey(nonterminal)) {
            List<String> rules = productions.get(nonterminal);
            for (String rule : rules) {
                System.out.println(nonterminal + " -> " + rule);
            }
        } else {
            System.out.println("No productions found for " + nonterminal);
        }
    }

    public boolean isCFG() {
        // start symbol must exist
        if (startSymbol == null || !nonterminals.contains(startSymbol)) {
            System.out.println("Invalid or undefined start symbol.");
            return false;
        }

        // a symbol must be either nonterminal or terminal, not both at the same time
        if (!Collections.disjoint(nonterminals, terminals)) {
            System.out.println("Overlap found between terminals and nonterminals.");
            return false;
        }

        // Check if all production rules are valid
        for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
            String left = entry.getKey();
            List<String> rules = entry.getValue();

            //nonterminal must not contain spaces
            if (!nonterminals.contains(left) || left.split("\\s+").length > 1) {
                System.out.println("Invalid left side in production: " + left);
                return false;
            }

            for (String right : rules) {
                String[] symbols = right.split("\\s+");
                for (String symbol : symbols) {
                    // right symbols must be recognizable by terminals or nonterminals
                    if (!terminals.contains(symbol) && !nonterminals.contains(symbol)) {
                        System.out.println("Unrecognized symbol: " + symbol);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Map<String, Set<String>> computeFirst() {
        Map<String, Set<String>> firstSets = new HashMap<>();

        // Initialize FIRST sets for terminals and nonterminals
        for (String terminal : terminals) {
            firstSets.put(terminal, new HashSet<>(Collections.singleton(terminal)));
        }
        for (String nonterminal : nonterminals) {
            firstSets.put(nonterminal, new HashSet<>());
        }

        boolean changed;
        do {
            changed = false;

            for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
                String nonterminal = entry.getKey();
                for (String rule : entry.getValue()) {
                    String[] symbols = rule.split("\\s+");
                    Set<String> firstOfRule = computeFirstOfProduction(symbols, firstSets);

                    // Add new symbols to the FIRST set
                    for (String firstSymbol : firstOfRule) {
                        if (firstSets.get(nonterminal).add(firstSymbol)) {
                            changed = true;
                        }
                    }
                }
            }
        } while (changed);

        // Print FIRST sets
        System.out.println("\nFirst Sets:");
        for (String nonterminal : nonterminals) {
            System.out.println("First(" + nonterminal + ") = " + firstSets.get(nonterminal));
        }

        return firstSets;
    }

    private Set<String> computeFirstOfProduction(String[] symbols, Map<String, Set<String>> firstSets) {
        Set<String> result = new HashSet<>();

        for (String symbol : symbols) {
            Set<String> firstOfSymbol = new HashSet<>(firstSets.get(symbol));

            // Remove epsilon if it exists
            boolean containsEpsilon = firstOfSymbol.remove("ε");

            // Add symbols to result
            result.addAll(firstOfSymbol);

            // If symbol doesn't produce epsilon, stop
            if (!containsEpsilon) {
                break;
            }

            // If last symbol, and all can produce epsilon, add epsilon
            if (symbol.equals(symbols[symbols.length - 1])) {
                result.add("ε");
            }
        }

        return result;
    }

    public Map<String, Set<String>> computeFollow(Map<String, Set<String>> firstSets) {
        Map<String, Set<String>> followSets = new HashMap<>();

        // Initialize FOLLOW sets
        for (String nonterminal : nonterminals) {
            followSets.put(nonterminal, new HashSet<>());
        }

        // Start symbol gets '$'
        followSets.get(startSymbol).add("$");

        boolean changed;
        do {
            changed = false;

            // parse all production entries
            for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
                String left = entry.getKey();

                // every rhs of prod
                for (String rule : entry.getValue()) {
                    String[] symbols = rule.split("\\s+");

                    // parse every term in rhs
                    for (int i = 0; i < symbols.length; i++) {
                        if (nonterminals.contains(symbols[i])) {
                            Set<String> followCurrent = followSets.get(symbols[i]);

                            // Compute FIRST of remaining symbols
                            // nonterminal followed by other symbols, if A followed by B => first(B)
                            if (i + 1 < symbols.length) { //if there are still more symbols left
                                Set<String> firstOfRemainder = computeFirstOfRemainder(
                                        Arrays.copyOfRange(symbols, i + 1, symbols.length),
                                        firstSets
                                );

                                // Add FIRST symbols to the current follow set
                                Set<String> firstToAdd = new HashSet<>(firstOfRemainder);
                                firstToAdd.remove("ε");

                                if (followCurrent.addAll(firstToAdd)) {
                                    changed = true;
                                }

                                // If FIRST of remainder contains epsilon, add FOLLOW of left side
                                if (firstOfRemainder.contains("ε")) {
                                    if (followCurrent.addAll(followSets.get(left))) {
                                        changed = true;
                                    }
                                }
                            } else {
                                // If current symbol is at the end, add FOLLOW of left side
                                if (followCurrent.addAll(followSets.get(left))) {
                                    changed = true;
                                }
                            }
                        }
                    }
                }
            }
        } while (changed);

        return followSets;
    }

    // compute first set of a sequence of symbols
    private Set<String> computeFirstOfRemainder(String[] symbols, Map<String, Set<String>> firstSets) {
        Set<String> result = new HashSet<>();

        for (String symbol : symbols) {
            Set<String> firstOfSymbol = new HashSet<>(firstSets.get(symbol));

            // Remove epsilon if it exists
            boolean containsEpsilon = firstOfSymbol.remove("ε");

            // add entire first set of every symbol to the result
            result.addAll(firstOfSymbol);

            // If symbol doesn't produce epsilon, stop
            if (!containsEpsilon) {
                break;
            }

            // If last symbol, and all can produce epsilon, add epsilon
            if (symbol.equals(symbols[symbols.length - 1])) {
                result.add("ε");
            }
        }

        return result;
    }


    public void printProductionsString(List<String> derivationSteps) {
        System.out.println("Productions String Representation:");
        if (derivationSteps == null || derivationSteps.isEmpty()) {
            System.out.println("No production steps available.");
            return;
        }

        // Join the steps with an arrow to show derivation
        String productionString = String.join(" → ", derivationSteps);
        System.out.println(productionString);
    }

    public void printDerivationsString(List<String> derivationSteps) {
        System.out.println("Derivations String Representation:");
        if (derivationSteps == null || derivationSteps.isEmpty()) {
            System.out.println("No derivation steps available.");
            return;
        }

        // Number and print each derivation step
        for (int i = 0; i < derivationSteps.size(); i++) {
            System.out.printf("Step %d: %s%n", i + 1, derivationSteps.get(i));
        }
    }

    public void printTree() {
        if (startSymbol == null) {
            System.out.println("No grammar loaded or invalid start symbol.");
            return;
        }

        Node root = buildTree(startSymbol);
        printTableRepresentation(root);
    }

    public Node buildTree(String symbol) {
        return buildTree(symbol, new HashSet<>());
    }

    // recursively build parse tree starting form a give symbol
    public Node buildTree(String symbol, Set<String> visited) {
        Node node = new Node(symbol); // create node in tree

        if (terminals.contains(symbol)) {
            // terminal node, no children
            return node;
        }

        if (visited.contains(symbol)) {
            // for preventing infinite recursion for recursive productions in case node already was visited
            node.children.add(new Node("..."));
            return node;
        }

        visited.add(symbol);

        // Nonterminal, expand tree in this direction using productions
        List<String> rules = productions.get(symbol);
        if (rules != null && !rules.isEmpty()) {
            // parse all production alternatives (for LL(1), the right one based on FIRST and input)
            for (String rule : rules) {
                String[] productionSymbols = rule.split("\\s+");

                Node prevChild = null;
                for (String prodSymbol : productionSymbols) {
                    Node child = buildTree(prodSymbol, visited); // create new node and build tree for each symbol in rhs
                    node.children.add(child); // add rhs symbol to children of the current node
                    child.parent = node;

                    // Link siblings
                    if (prevChild != null) {
                        prevChild.sibling = child;
                    }
                    prevChild = child;
                }
            }
        }

        // Remove this symbol from visited set after processing
        visited.remove(symbol);

        return node;
    }
    public void printTableRepresentation(Node root) {
        System.out.println("Tree Representation (Father-Sibling Relation):");
        System.out.println("Node\t\tParent\t\tSibling");
        System.out.println("--------------------------------------------");

        traverseTree(root, null, null);
    }

    private void traverseTree(Node currentNode, Node parent, Node sibling) {
        if (currentNode == null) return;

        System.out.printf("%-15s\t%-15s\t%-15s%n",
                currentNode.value,
                (parent != null ? parent.value : "null"),
                (sibling != null ? sibling.value : "null")
        );

        Node prevChild = null; // for tracking sibling of each child
        for (Node child : currentNode.children) {
            traverseTree(child, currentNode, prevChild);
            prevChild = child;
        }
    }

    public void printGraphicalTree(Node root) {
        System.out.println("Graphical Tree Representation:");
        printTreeRecursive(root, 0);
    }

    private void printTreeRecursive(Node node, int depth) {
        if (node == null) return;

        // Indent based on depth
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indent.append("│   ");
        }

        // Print current node
        System.out.println(indent + "├── " + node.value);

        // Recursively print children
        for (Node child : node.children) {
            printTreeRecursive(child, depth + 1);
        }
    }

}