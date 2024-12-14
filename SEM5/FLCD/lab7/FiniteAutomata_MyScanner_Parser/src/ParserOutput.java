import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParserOutput {
    private Node root;
    private List<String> derivationSteps;
    private Map<String, List<String>> parsingTable;

    public ParserOutput(Grammar grammar) {
        this.root = grammar.getRootNode();
        this.derivationSteps = new ArrayList<>();
        this.parsingTable = new HashMap<>();
        generateParsingTable(grammar);
    }

    public Node getRoot(){ return this.root; }

    public boolean parseSequence(String sequence) {
        Stack<String> stack = new Stack<>();
        stack.push("$"); // Push end marker
        stack.push(root.value); // Push start symbol

        String[] tokens = sequence.split("\\s+");
        int index = 0;

        derivationSteps.clear(); // Clear previous steps
        derivationSteps.add(root.value); // Add start symbol as first step

        while (!stack.isEmpty()) {
            String top = stack.pop();

            if (index >= tokens.length) {
                return false; // Input exhausted before parsing complete
            }

            String currentToken = tokens[index];

            if (top.equals(currentToken)) {
                // Matched terminal, consume input token
                index++;
            } else if (parsingTable.containsKey(top + "," + currentToken)) {
                // Non-terminal: expand using production
                List<String> production = parsingTable.get(top + "," + currentToken);
                if (production.size() > 1) {
                    System.out.println("Conflict detected during parsing.");
                    return false; // Conflict detected
                }
                String[] rhs = production.get(0).split("\\s+");

                // Push production RHS onto stack in reverse order
                for (int i = rhs.length - 1; i >= 0; i--) {
                    if (!rhs[i].equals("ε")) { // Ignore epsilon
                        stack.push(rhs[i]);
                    }
                }

                // Update derivation steps
                String newDerivation = derivationSteps.get(derivationSteps.size() - 1)
                        .replaceFirst(Pattern.quote(top), String.join(" ", rhs));
                derivationSteps.add(newDerivation);
            } else {
                return false; // Error: no rule for top and currentToken
            }
        }

        return index == tokens.length; // Accept if entire input consumed
    }

    // Helper method to generate a string representation of the parsing tree in a father-sibling format
    public void printParsingTreeWithParentSibling(Node node, Node parent, List<Node> siblings) {
        if (node == null) {
            return;
        }

        // Print current node with its parent and sibling
        String sibling = (siblings.size() > 0) ? siblings.get(siblings.size() - 1).value : "null";
        System.out.println(node.value + "\t\t" + (parent != null ? parent.value : "null") + "\t\t" + sibling);

        // Traverse children and process siblings
        List<Node> childSiblings = new ArrayList<>(siblings);
        childSiblings.add(node);

        for (Node child : node.children) {
            printParsingTreeWithParentSibling(child, node, childSiblings);
        }
    }

    // Print the parsing table with parent and sibling relationships
    public void printParsingTreeTable() {
        System.out.println("Node\t\tParent\t\tSibling");
        System.out.println("--------------------------------------------");
        printParsingTreeWithParentSibling(root, null, new ArrayList<>());
    }

    private void generateParsingTable(Grammar grammar) {
        // LL(1) parsing table generation
        Map<String, Set<String>> firstSets = grammar.computeFirst();
        Map<String, Set<String>> followSets = grammar.computeFollow(firstSets);

        for (String nonterminal : grammar.getNonterminals()) {
            List<String> productions = grammar.getProductions().get(nonterminal);

            for (String production : productions) {
                Set<String> firstOfProduction = computeFirstOfProduction(production.split("\\s+"), firstSets);

                for (String terminal : firstOfProduction) {
                    if (!terminal.isEmpty()) {
                        addToParsingTable(nonterminal, terminal, production);
                    }
                }

                if (firstOfProduction.contains("ε")) {
                    for (String followTerminal : followSets.get(nonterminal)) {
                        addToParsingTable(nonterminal, followTerminal, production);
                    }
                }
            }
        }
    }

    private void addToParsingTable(String nonterminal, String terminal, String production) {
        String key = nonterminal + "," + terminal;
        parsingTable.putIfAbsent(key, new ArrayList<>());

        if (!parsingTable.get(key).isEmpty()) {
            System.out.println("Conflict detected in LL(1) parsing table!");
            System.out.println("Row: " + nonterminal + ", Column: " + terminal);
            System.out.println("Existing production: " + parsingTable.get(key).get(0));
            System.out.println("New production: " + production);
        }

        parsingTable.get(key).add(production);
    }

    private Set<String> computeFirstOfProduction(String[] symbols, Map<String, Set<String>> firstSets) {
        Set<String> result = new HashSet<>();

        for (String symbol : symbols) {
            Set<String> firstOfSymbol = new HashSet<>(firstSets.get(symbol));

            boolean containsEpsilon = firstOfSymbol.remove("ε");

            result.addAll(firstOfSymbol);

            if (!containsEpsilon) {
                break;
            }

            if (symbol.equals(symbols[symbols.length - 1])) {
                result.add("ε");
            }
        }

        return result;
    }

    public void printParsingTable() {
        System.out.println("LL(1) Parsing Table:");
        for (Map.Entry<String, List<String>> entry : parsingTable.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public void writeParsingTableToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("LL(1) Parsing Table:");
            for (Map.Entry<String, List<String>> entry : parsingTable.entrySet()) {
                writer.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error writing parsing table to file: " + e.getMessage());
        }
    }

    // Transform parsing tree to a list of strings (derivation steps)
    public List<String> getDerivationSteps() {
        derivationSteps.clear();
        if (root != null) {
            collectDerivationSteps(root, root.value, null);
        }
        return derivationSteps;
    }

    private void collectDerivationSteps(Node node, String currentDerivation, Node previousNode) {
        if (node == null) return;

        if (!derivationSteps.contains(currentDerivation)) {
            derivationSteps.add(currentDerivation);
        }

        if (!node.children.isEmpty()) {
            String childrenValues = node.children.stream()
                    .map(child -> child.value)
                    .collect(Collectors.joining(" "));

            String newDerivation = currentDerivation.replaceFirst(
                    Pattern.quote(node.value),
                    childrenValues
            );

            for (Node child : node.children) {
                collectDerivationSteps(child, newDerivation, node);
            }
        }
    }

    // Print derivation steps to console
    public void printDerivationSteps() {
        List<String> steps = getDerivationSteps();
        System.out.println("Derivation Steps:");
        for (int i = 0; i < steps.size(); i++) {
            System.out.println("Step " + (i + 1) + ": " + steps.get(i));
        }
    }

    // Write derivation steps to file and print parsing tree
    public void writeDerivationStepsToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Derivation Steps:");
            List<String> steps = getDerivationSteps();
            for (int i = 0; i < steps.size(); i++) {
                writer.println("Step " + (i + 1) + ": " + steps.get(i));
            }

            writer.println("\nParsing Tree:");
            printParsingTreeWithParentSibling(root, null, new ArrayList<>());

        } catch (IOException e) {
            System.err.println("Error writing derivation steps to file: " + e.getMessage());
        }
    }

    public void printParsingTreeWithParentSibling(Node node, Node parent, List<Node> siblings, PrintWriter writer) {
        if (node == null) {
            return;
        }

        // Print the current node, parent, and sibling information
        String parentValue = (parent == null) ? "null" : parent.value;
        String siblingValue = (siblings.isEmpty()) ? "null" : siblings.get(siblings.size() - 1).value;

        writer.printf("%-15s%-15s%-15s%n", node.value, parentValue, siblingValue);

        // Process the children of the current node
        List<Node> newSiblings = new ArrayList<>(siblings);
        newSiblings.add(node);
        for (Node child : node.children) {
            printParsingTreeWithParentSibling(child, node, new ArrayList<>(), writer);
        }
    }

}
