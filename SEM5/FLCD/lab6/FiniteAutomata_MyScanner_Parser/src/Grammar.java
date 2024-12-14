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
        // Check if start symbol exists and is in nonterminals
        if (startSymbol == null || !nonterminals.contains(startSymbol)) {
            System.out.println("Invalid or undefined start symbol.");
            return false;
        }

        // Check for terminal and nonterminal overlap
        if (!Collections.disjoint(nonterminals, terminals)) {
            System.out.println("Overlap found between terminals and nonterminals.");
            return false;
        }

        // Check if all production rules are valid
        for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
            String left = entry.getKey();
            List<String> rules = entry.getValue();

            if (!nonterminals.contains(left) || left.split("\\s+").length > 1) {
                System.out.println("Invalid left side in production: " + left);
                return false;
            }

            for (String right : rules) {
                String[] symbols = right.split("\\s+");
                for (String symbol : symbols) {
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
            boolean containsEpsilon = firstOfSymbol.remove("");

            // Add symbols to result
            result.addAll(firstOfSymbol);

            // If symbol doesn't produce epsilon, stop
            if (!containsEpsilon) {
                break;
            }

            // If last symbol, and all can produce epsilon, add epsilon
            if (symbol.equals(symbols[symbols.length - 1])) {
                result.add("");
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

            for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
                String left = entry.getKey();

                for (String rule : entry.getValue()) {
                    String[] symbols = rule.split("\\s+");

                    for (int i = 0; i < symbols.length; i++) {
                        if (nonterminals.contains(symbols[i])) {
                            Set<String> followCurrent = followSets.get(symbols[i]);

                            // Compute FIRST of remaining symbols
                            if (i + 1 < symbols.length) {
                                Set<String> firstOfRemainder = computeFirstOfRemainder(
                                        Arrays.copyOfRange(symbols, i + 1, symbols.length),
                                        firstSets
                                );

                                // Add FIRST symbols (except epsilon)
                                Set<String> firstToAdd = new HashSet<>(firstOfRemainder);
                                firstToAdd.remove("");

                                if (followCurrent.addAll(firstToAdd)) {
                                    changed = true;
                                }

                                // If FIRST of remainder contains epsilon, add FOLLOW of left side
                                if (firstOfRemainder.contains("")) {
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

    private Set<String> computeFirstOfRemainder(String[] symbols, Map<String, Set<String>> firstSets) {
        Set<String> result = new HashSet<>();

        for (String symbol : symbols) {
            Set<String> firstOfSymbol = new HashSet<>(firstSets.get(symbol));

            // Remove epsilon if it exists
            boolean containsEpsilon = firstOfSymbol.remove("");

            // Add symbols to result
            result.addAll(firstOfSymbol);

            // If symbol doesn't produce epsilon, stop
            if (!containsEpsilon) {
                break;
            }

            // If last symbol, and all can produce epsilon, add epsilon
            if (symbol.equals(symbols[symbols.length - 1])) {
                result.add("");
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

    public Node buildTree(String symbol, Set<String> visited) {
        Node node = new Node(symbol);

        if (terminals.contains(symbol)) {
            // Terminal node, no children
            return node;
        }

        if (visited.contains(symbol)) {
            // Prevent infinite recursion for recursive productions
            node.children.add(new Node("..."));
            return node;
        }

        // Mark this symbol as being visited
        visited.add(symbol);

        // Nonterminal, expand using productions
        List<String> rules = productions.get(symbol);
        if (rules != null && !rules.isEmpty()) {
            // Iterate over all production alternatives (for LL(1), choose the right one based on FIRST and input)
            for (String rule : rules) {
                String[] productionSymbols = rule.split("\\s+");

                Node prevChild = null;
                for (String prodSymbol : productionSymbols) {
                    Node child = buildTree(prodSymbol, visited);
                    node.children.add(child);
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

    private void traverseTree(Node node, Node parent, Node sibling) {
        if (node == null) return;

        // Format the output to align columns
        System.out.printf("%-15s\t%-15s\t%-15s%n",
                node.value,
                (parent != null ? parent.value : "null"),
                (sibling != null ? sibling.value : "null")
        );

        Node prevChild = null;
        for (Node child : node.children) {
            traverseTree(child, node, prevChild);
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