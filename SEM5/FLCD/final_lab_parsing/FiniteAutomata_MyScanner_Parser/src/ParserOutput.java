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

    private void generateParsingTable(Grammar grammar) {
        // LL(1) parsing table generation
        // conflicts not handled
        Map<String, Set<String>> firstSets = grammar.computeFirst();
        Map<String, Set<String>> followSets = grammar.computeFollow(firstSets);

        //parse every nonterminal
        for (String nonterminal : grammar.getNonterminals()) {
            // get all prod specific to one nonterminal
            List<String> productions = grammar.getProductions().get(nonterminal);

            // parse all the prod of that nonterminal (those split by | )
            for (String production : productions) {
                Set<String> firstOfProduction = computeFirstOfProduction(production.split("\\s+"), firstSets);

                // parse all symbols in first set of the production
                for (String terminal : firstOfProduction) {
                    if (!terminal.isEmpty()) {
                        addToParsingTable(nonterminal, terminal, production);
                    }
                }

                // handle epsilon productions
                if (firstOfProduction.contains("ε")) {
                    for (String followTerminal : followSets.get(nonterminal)) {
                        addToParsingTable(nonterminal, followTerminal, production);
                    }
                }
            }
        }
    }

    // create a table for parsing table and put on row nonterminal and on column terminal
    private void addToParsingTable(String nonterminal, String terminal, String production) {
        String key = nonterminal + "," + terminal;
        parsingTable.putIfAbsent(key, new ArrayList<>());

        // Check for conflict, if prod rule already present at (nonterminal, terminal) => conflict
        // if A -> B and new prod A -> C exists => grammar not LL (1)
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

            // remove epsilon if it exists
            boolean containsEpsilon = firstOfSymbol.remove("ε");

            // add symbols to result
            result.addAll(firstOfSymbol);

            // if symbol doesn't produce epsilon, stop
            if (!containsEpsilon) {
                break;
            }

            // if last symbol, and all can produce epsilon, add epsilon
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

        // add current derivation step if it's not already in the list starting from start symbol added by default
        if (!derivationSteps.contains(currentDerivation)) {
            derivationSteps.add(currentDerivation);
        }

        // if the node has children, expand the current derivation
        if (!node.children.isEmpty()) {
            // collect all children values in a list
            String childrenValues = node.children.stream()
                    .map(child -> child.value)
                    .collect(Collectors.joining(" "));

            // create new derivation by replacing the current node with its children
            String newDerivation = currentDerivation.replaceFirst(
                    Pattern.quote(node.value),
                    childrenValues
            );

            // recursively process all children
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

    // Write derivation steps to file
    public void writeDerivationStepsToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Derivation Steps:");
            List<String> steps = getDerivationSteps();
            for (int i = 0; i < steps.size(); i++) {
                writer.println("Step " + (i + 1) + ": " + steps.get(i));
            }
        } catch (IOException e) {
            System.err.println("Error writing derivation steps to file: " + e.getMessage());
        }
    }
}