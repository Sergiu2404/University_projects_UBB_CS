package tests;

import org.junit.Test;

import java.util.*;


public class GrammarAnalyzerTest {
    @Test
    public void testGetNonterminals() {
        System.out.println("\n--- Testing getNonterminals() ---");
        Grammar grammar = new Grammar();
        grammar.hardcodedInput();

        List<String> expectedNonterminals = Arrays.asList("S", "A");
        List<String> actualNonterminals = grammar.getNonterminals();

        if (!expectedNonterminals.equals(actualNonterminals)) {
            System.out.println("getNonterminals() test failed. Expected: " + expectedNonterminals + ", Got: " + actualNonterminals);
        } else {
            System.out.println("getNonterminals() test passed.");
        }
    }
    @Test
    public void testGetTerminals() {
        System.out.println("\n--- Testing getTerminals() ---");
        Grammar grammar = new Grammar();
        grammar.hardcodedInput();

        List<String> expectedTerminals = Arrays.asList("a", "b");
        List<String> actualTerminals = grammar.getTerminals();

        if (!expectedTerminals.equals(actualTerminals)) {
            System.out.println("getTerminals() test failed. Expected: " + expectedTerminals + ", Got: " + actualTerminals);
        } else {
            System.out.println("getTerminals() test passed.");
        }
    }
    @Test
    public void testGetProductions() {
        System.out.println("\n--- Testing getProductions() ---");
        Grammar grammar = new Grammar();
        grammar.hardcodedInput();

        Map<String, List<String>> expectedProductions = new HashMap<>();
        expectedProductions.put("S", Arrays.asList("A A"));
        expectedProductions.put("A", Arrays.asList("a A", "b"));

        Map<String, List<String>> actualProductions = grammar.getProductions();

        if (!expectedProductions.equals(actualProductions)) {
            System.out.println("getProductions() test failed. Expected: " + expectedProductions + ", Got: " + actualProductions);
        } else {
            System.out.println("getProductions() test passed.");
        }
    }

    @Test
    public void testGetStartSymbol() {
        System.out.println("\n--- Testing getStartSymbol() ---");
        Grammar grammar = new Grammar();
        grammar.hardcodedInput();

        String expectedStartSymbol = "S";
        String actualStartSymbol = grammar.getStartSymbol();

        if (!expectedStartSymbol.equals(actualStartSymbol)) {
            System.out.println("getStartSymbol() test failed. Expected: " + expectedStartSymbol + ", Got: " + actualStartSymbol);
        } else {
            System.out.println("getStartSymbol() test passed.");
        }
    }

    @Test
    public void testIsCFG() {
        System.out.println("\n--- Testing isCFG() ---");

        // Test valid CFG
        Grammar validGrammar = new Grammar();
        validGrammar.hardcodedInput();
        if (!validGrammar.isCFG()) {
            System.out.println("isCFG() test failed for valid grammar.");
        } else {
            System.out.println("isCFG() test passed for valid grammar.");
        }

        // Test invalid CFG (Overlap between terminals and nonterminals)
        Grammar invalidGrammar = new Grammar();
        invalidGrammar.hardcodedInput();
        invalidGrammar.terminals.add("S");  // Adding a terminal that is a nonterminal
        if (invalidGrammar.isCFG()) {
            System.out.println("isCFG() test failed for invalid grammar.");
        } else {
            System.out.println("isCFG() test passed for invalid grammar.");
        }
    }


    @Test
    public void testFirstSetComputation() {
        System.out.println("\n--- Testing FIRST Set Computation ---");
        Grammar grammar = new Grammar();
        grammar.hardcodedInput();

        Map<String, Set<String>> firstSets = grammar.computeFirst();

        // Manual test for First Sets
        boolean firstSetTestPassed = true;

        // Test FIRST(S)
        Set<String> expectedFirstS = new HashSet<>(Arrays.asList("a", "b"));
        if (!compareSetContents(firstSets.get("S"), expectedFirstS)) {
            System.out.println("FIRST(S) test failed");
            firstSetTestPassed = false;
        }

        // Test FIRST(A)
        Set<String> expectedFirstA = new HashSet<>(Arrays.asList("a", "b"));
        if (!compareSetContents(firstSets.get("A"), expectedFirstA)) {
            System.out.println("FIRST(A) test failed");
            firstSetTestPassed = false;
        }

        // Print First Sets
        System.out.println("FIRST Sets:");
        for (String key : firstSets.keySet()) {
            System.out.println("FIRST(" + key + ") = " + firstSets.get(key));
        }

        if (firstSetTestPassed) {
            System.out.println("FIRST Set Computation: ALL TESTS PASSED");
        } else {
            System.out.println("FIRST Set Computation: SOME TESTS FAILED");
        }
    }

    @Test
    public void testFollowSetComputation() {
        System.out.println("\n--- Testing FOLLOW Set Computation ---");
        Grammar grammar = new Grammar();
        grammar.hardcodedInput();

        Map<String, Set<String>> firstSets = grammar.computeFirst();
        Map<String, Set<String>> followSets = grammar.computeFollow(firstSets);

        // Manual test for Follow Sets
        boolean followSetTestPassed = true;

        // Test FOLLOW(S)
        Set<String> expectedFollowS = new HashSet<>(Arrays.asList("$"));
        if (!compareSetContents(followSets.get("S"), expectedFollowS)) {
            System.out.println("FOLLOW(S) test failed");
            followSetTestPassed = false;
        }

        // Test FOLLOW(A)
        Set<String> expectedFollowA = new HashSet<>(Arrays.asList("a", "b", "$"));
        if (!compareSetContents(followSets.get("A"), expectedFollowA)) {
            System.out.println("FOLLOW(A) test failed");
            followSetTestPassed = false;
        }

        // Print Follow Sets
        System.out.println("FOLLOW Sets:");
        for (String key : followSets.keySet()) {
            System.out.println("FOLLOW(" + key + ") = " + followSets.get(key));
        }

        if (followSetTestPassed) {
            System.out.println("FOLLOW Set Computation: ALL TESTS PASSED");
        } else {
            System.out.println("FOLLOW Set Computation: SOME TESTS FAILED");
        }
    }

    // Helper method to compare set contents without order dependency
    private boolean compareSetContents(Set<String> actual, Set<String> expected) {
        if (actual == null || expected == null) {
            return false;
        }
        return actual.containsAll(expected) && expected.containsAll(actual);
    }

    class Grammar {
        private List<String> nonterminals;
        private List<String> terminals;
        private Map<String, List<String>> productions;
        private String startSymbol;

        public Grammar() {
            this.nonterminals = new ArrayList<>();
            this.terminals = new ArrayList<>();
            this.productions = new HashMap<>();
        }

        public void hardcodedInput() {
            // Hardcoding grammar components
            this.nonterminals.addAll(Arrays.asList("S", "A"));
            this.terminals.addAll(Arrays.asList("a", "b"));
            this.startSymbol = "S";

            this.productions.put("S", Arrays.asList("A A"));
            this.productions.put("A", Arrays.asList("a A", "b"));
        }

        public List<String> getNonterminals() {
            return nonterminals;
        }

        public List<String> getTerminals() {
            return terminals;
        }

        public Map<String, List<String>> getProductions() {
            return productions;
        }

        public String getStartSymbol() {
            return startSymbol;
        }

        public boolean isCFG() {
            // Validate CFG rules
            if (startSymbol == null || !nonterminals.contains(startSymbol)) {
                System.out.println("Invalid or undefined start symbol.");
                return false;
            }

            if (!Collections.disjoint(nonterminals, terminals)) {
                System.out.println("Overlap between terminals and nonterminals.");
                return false;
            }

            for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
                String left = entry.getKey();
                if (!nonterminals.contains(left)) {
                    System.out.println("Invalid left side in production: " + left);
                    return false;
                }

                for (String right : entry.getValue()) {
                    for (String symbol : right.split("\\s+")) {
                        if (!terminals.contains(symbol) && !nonterminals.contains(symbol)) {
                            System.out.println("Unrecognized symbol in production: " + symbol);
                            return false;
                        }
                    }
                }
            }

            return true;
        }

        public Map<String, Set<String>> computeFirst() {
            Map<String, Set<String>> firstSets = new HashMap<>();

            // Initialize first sets for terminals
            for (String terminal : terminals) {
                firstSets.put(terminal, new HashSet<>(Arrays.asList(terminal)));
            }

            // Initialize first sets for nonterminals
            for (String nonterminal : nonterminals) {
                firstSets.put(nonterminal, new HashSet<>());
            }

            boolean changed;
            do {
                changed = false;
                for (String nonterminal : nonterminals) {
                    for (String rule : productions.get(nonterminal)) {
                        String[] symbols = rule.split("\\s+");
                        Set<String> firstOfRule = computeFirstOfProduction(symbols, firstSets);

                        // Add all new elements from firstOfRule to the nonterminal's first set
                        for (String first : firstOfRule) {
                            if (!firstSets.get(nonterminal).contains(first)) {
                                firstSets.get(nonterminal).add(first);
                                changed = true;
                            }
                        }
                    }
                }
            } while (changed);

            return firstSets;
        }

        private Set<String> computeFirstOfProduction(String[] symbols, Map<String, Set<String>> firstSets) {
            Set<String> result = new HashSet<>();
            for (String symbol : symbols) {
                Set<String> firstOfSymbol = new HashSet<>(firstSets.get(symbol));
                boolean hasEpsilon = firstOfSymbol.remove("ε");
                result.addAll(firstOfSymbol);
                if (!hasEpsilon) break;
            }
            return result;
        }

        public Map<String, Set<String>> computeFollow(Map<String, Set<String>> firstSets) {
            Map<String, Set<String>> followSets = new HashMap<>();

            // Initialize follow sets for nonterminals
            for (String nonterminal : nonterminals) {
                followSets.put(nonterminal, new HashSet<>());
            }

            // Add $ to the start symbol's follow set
            followSets.get(startSymbol).add("$");

            boolean changed;
            do {
                changed = false;
                for (String nonterminal : nonterminals) {
                    for (String rule : productions.get(nonterminal)) {
                        String[] symbols = rule.split("\\s+");
                        for (int i = 0; i < symbols.length; i++) {
                            if (!nonterminals.contains(symbols[i])) continue;

                            Set<String> followCurrent = followSets.get(symbols[i]);

                            if (i + 1 < symbols.length) {
                                // Compute FIRST of remaining symbols
                                Set<String> firstOfNext = computeFirstOfProduction(
                                        Arrays.copyOfRange(symbols, i + 1, symbols.length),
                                        firstSets
                                );

                                // Add FIRST of next symbols (except epsilon)
                                Set<String> toAdd = new HashSet<>(firstOfNext);
                                toAdd.remove("ε");

                                // Add all new elements to follow set
                                for (String first : toAdd) {
                                    if (!followCurrent.contains(first)) {
                                        followCurrent.add(first);
                                        changed = true;
                                    }
                                }

                                // If FIRST contains epsilon, add FOLLOW of current nonterminal
                                if (firstOfNext.contains("ε")) {
                                    for (String follow : followSets.get(nonterminal)) {
                                        if (!followCurrent.contains(follow)) {
                                            followCurrent.add(follow);
                                            changed = true;
                                        }
                                    }
                                }
                            } else {
                                // If it's the last symbol, add FOLLOW of current nonterminal
                                for (String follow : followSets.get(nonterminal)) {
                                    if (!followCurrent.contains(follow)) {
                                        followCurrent.add(follow);
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
    }

    public void run() {
        testFirstSetComputation();
        testFollowSetComputation();

        // Test for Grammar Class Methods
        testGetNonterminals();
        testGetTerminals();
        testGetProductions();
        testGetStartSymbol();
        testIsCFG();
    }
}