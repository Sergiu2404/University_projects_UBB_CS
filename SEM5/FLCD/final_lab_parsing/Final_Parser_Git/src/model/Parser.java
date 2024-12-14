package model;

import javafx.util.Pair;

import java.util.*;

public class Parser {
    private Grammar grammar;
    private Map<String, Set<String>> firstSet;
    private Map<String, Set<String>> followSet;
    private ParseTable parseTable = new ParseTable();
    private static Stack<List<String>> rules = new Stack<>();
    private Map<Pair<String, List<String>>, Integer> productionsNumbered = new HashMap<>();
    private Stack<String> alpha = new Stack<>();
    private Stack<String> beta = new Stack<>();
    private Stack<String> pi = new Stack<>();

    public Parser() {
        this.grammar = new Grammar();
        this.firstSet = new HashMap<>();
        this.followSet = new HashMap<>();
        generateSets();
    }

    private void generateSets() {
        generateFirstSet();
        generateFollowSet();
        createParseTable();
    }

    private void generateFirstSet() {
        for (String nonTerminal : grammar.getNonTerminals()) {
            firstSet.put(nonTerminal, this.firstOf(nonTerminal));
        }
    }

    private Set<String> firstOf(String nonTerminal) {
        if (firstSet.containsKey(nonTerminal))
            return firstSet.get(nonTerminal);
        Set<String> temp = new HashSet<>();
        Set<String> terminals = grammar.getTerminals();
        for (Production production : grammar.getProductionsForNonterminal(nonTerminal))
            for (List<String> rule : production.getRules()) {
                String firstSymbol = rule.get(0);
                if (firstSymbol.equals("ε"))
                    temp.add("ε");
                else if (terminals.contains(firstSymbol))
                    temp.add(firstSymbol);
                else
                    temp.addAll(firstOf(firstSymbol));
            }
        return temp;
    }

    private void generateFollowSet() {
        for (String nonTerminal : grammar.getNonTerminals()) {
            followSet.put(nonTerminal, this.followOf(nonTerminal, nonTerminal));
        }
    }

    private Set<String> followOf(String nonTerminal, String initialNonTerminal) {
        if (followSet.containsKey(nonTerminal))
            return followSet.get(nonTerminal);
        Set<String> temp = new HashSet<>();
        Set<String> terminals = grammar.getTerminals();

        if (nonTerminal.equals(grammar.getStartingSymbol()))
            temp.add("$");

        for (Production production : grammar.getProductionsContainingNonterminal(nonTerminal)) {
            String productionStart = production.getStart();
            for (List<String> rule : production.getRules()){
                List<String> ruleConflict = new ArrayList<>();
                ruleConflict.add(nonTerminal);
                ruleConflict.addAll(rule);
                if (rule.contains(nonTerminal) && !rules.contains(ruleConflict)) {
                    rules.push(ruleConflict);
                    int indexNonTerminal = rule.indexOf(nonTerminal);
                    temp.addAll(followOperation(nonTerminal, temp, terminals, productionStart, rule, indexNonTerminal, initialNonTerminal));

//                    // For cases like: N -> E 36 E, when E is the nonTerminal so we have 2 possibilities: 36 goes in follow(E) and also follow(N)
                    List<String> sublist = rule.subList(indexNonTerminal + 1, rule.size());
                    if (sublist.contains(nonTerminal))
                        temp.addAll(followOperation(nonTerminal, temp, terminals, productionStart, rule, indexNonTerminal + 1 + sublist.indexOf(nonTerminal), initialNonTerminal));

                    rules.pop();
                }
            }
        }

        return temp;
    }

    private Set<String> followOperation(String nonTerminal, Set<String> temp, Set<String> terminals, String productionStart, List<String> rule, int indexNonTerminal, String initialNonTerminal) {
        if (indexNonTerminal == rule.size() - 1) {
            if (productionStart.equals(nonTerminal))
                return temp;
            if (!initialNonTerminal.equals(productionStart)){
                temp.addAll(followOf(productionStart, initialNonTerminal));
            }
        }
        else
        {
            String nextSymbol = rule.get(indexNonTerminal + 1);
            if (terminals.contains(nextSymbol))
                temp.add(nextSymbol);
            else{
                if (!initialNonTerminal.equals(nextSymbol)) {
                    Set<String> fists = new HashSet<>(firstSet.get(nextSymbol));
                    if (fists.contains("ε")) {
                        temp.addAll(followOf(nextSymbol, initialNonTerminal));
                        fists.remove("ε");
                    }
                    temp.addAll(fists);
                }
            }
        }
        return temp;
    }

    private void createParseTable() {
        numberingProductions();

        List<String> columnSymbols = new LinkedList<>(grammar.getTerminals());
        columnSymbols.add("$");

        // M(a, a) = pop
        // M($, $) = acc

        parseTable.put(new Pair<>("$", "$"), new Pair<>(Collections.singletonList("acc"), -1));
        for (String terminal: grammar.getTerminals())
            parseTable.put(new Pair<>(terminal, terminal), new Pair<>(Collections.singletonList("pop"), -1));



//        1) M(A, a) = (α, i), if:
//            a) a ∈ first(α)
//            b) a != ε
//            c) A -> α production with index i
//
//        2) M(A, b) = (α, i), if:
//            a) ε ∈ first(α)
//            b) whichever b ∈ follow(A)
//            c) A -> α production with index i
        productionsNumbered.forEach((key, value) -> {
            String rowSymbol = key.getKey();
            List<String> rule = key.getValue();
            Pair<List<String>, Integer> parseTableValue = new Pair<>(rule, value);

            for (String columnSymbol : columnSymbols) {
                Pair<String, String> parseTableKey = new Pair<>(rowSymbol, columnSymbol);

                // if our column-terminal is exactly first of rule
                if (rule.get(0).equals(columnSymbol) && !columnSymbol.equals("ε"))
                    parseTable.put(parseTableKey, parseTableValue);

                // if the first symbol is a non-terminal and it's first contain our column-terminal
                else if (grammar.getNonTerminals().contains(rule.get(0)) && firstSet.get(rule.get(0)).contains(columnSymbol)) {
                    if (!parseTable.containsKey(parseTableKey)) {
                        parseTable.put(parseTableKey, parseTableValue);
                    }
                }
                else {
                    // if the first symbol is ε then everything if FOLLOW(rowSymbol) will be in parse table
                    if (rule.get(0).equals("ε")) {
                        for (String b : followSet.get(rowSymbol))
                            parseTable.put(new Pair<>(rowSymbol, b), parseTableValue);

                    // if ε is in FIRST(rule)
                    } else {
                        Set<String> firsts = new HashSet<>();
                        for (String symbol : rule)
                            if (grammar.getNonTerminals().contains(symbol))
                                firsts.addAll(firstSet.get(symbol));
                        if (firsts.contains("ε")) {
                            for (String b : firstSet.get(rowSymbol)) {
                                if (b.equals("ε"))
                                    b = "$";
                                parseTableKey = new Pair<>(rowSymbol, b);
                                if (!parseTable.containsKey(parseTableKey)) {
                                    parseTable.put(parseTableKey, parseTableValue);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public boolean parse(List<String> w) {
        initializeStacks(w);

        boolean go = true;
        boolean result = true;

        while (go) {
            String betaHead = beta.peek();
            String alphaHead = alpha.peek();

            if (betaHead.equals("$") && alphaHead.equals("$")) {
                return result;
            }

            Pair<String, String> heads = new Pair<>(betaHead, alphaHead);
            Pair<List<String>, Integer> parseTableEntry = parseTable.get(heads);

            if (parseTableEntry == null) {
                heads = new Pair<>(betaHead, "ε");
                parseTableEntry = parseTable.get(heads);
                if (parseTableEntry != null) {
                    beta.pop();
                    continue;
                }

            }

            if (parseTableEntry == null) {
                go = false;
                result = false;
            } else {
                List<String> production = parseTableEntry.getKey();
                Integer productionPos = parseTableEntry.getValue();

                if (productionPos == -1 && production.get(0).equals("acc")) {
                    go = false;
                } else if (productionPos == -1 && production.get(0).equals("pop")) {
                    beta.pop();
                    alpha.pop();
                } else {
                    beta.pop();
                    if (!production.get(0).equals("ε")) {
                        pushAsChars(production, beta);
                    }
                    pi.push(productionPos.toString());
                }
            }
        }

        return result;
    }

    public boolean parseSource(List<model.Pair<Integer, Integer>> pif) {
        List<String> sequence = new LinkedList<>();
        for (model.Pair<Integer, Integer> pifEntry : pif) {
            sequence.add(String.valueOf(pifEntry.getKey()));
        }

        return this.parse(sequence);
    }

    private void initializeStacks(List<String> w) {
        alpha.clear();
        alpha.push("$");
        pushAsChars(w, alpha);

        beta.clear();
        beta.push("$");
        beta.push(grammar.getStartingSymbol());

        pi.clear();
        pi.push("ε");
    }

    private void pushAsChars(List<String> sequence, Stack<String> stack) {
        for (int i = sequence.size() - 1; i >= 0; i--) {
            stack.push(sequence.get(i));
        }
    }

    private void numberingProductions() {
        int index = 1;
        for (Production production: grammar.getProductions())
            for (List<String> rule: production.getRules())
                productionsNumbered.put(new Pair<>(production.getStart(), rule), index++);
    }

    public Grammar getGrammar() {
        return grammar;
    }

    public Map<String, Set<String>> getFirstSet() {
        return firstSet;
    }

    public Map<String, Set<String>> getFollowSet() {
        return followSet;
    }

    public ParseTable getParseTable() {
        return parseTable;
    }

    public Stack<String> getPi() {
        return pi;
    }

    public Map<Pair<String, List<String>>, Integer> getProductionsNumbered() {
        return productionsNumbered;
    }
}
