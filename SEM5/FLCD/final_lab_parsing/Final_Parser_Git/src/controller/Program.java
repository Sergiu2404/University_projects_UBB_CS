package controller;

import model.*;
import model.Scanner;

import java.util.*;

public class Program {
    private Parser parser = new Parser();
    private Scanner scanner = new Scanner();

    public Map<String, Set<String>> getFirstSet() {
        return parser.getFirstSet();
    }

    public List<String> getNonTerminals() {
        return parser.getGrammar().getNonTerminals();
    }

    public Set<String> getTerminals() {
        return parser.getGrammar().getTerminals();
    }

    public List<Production> getProductions() {
        return parser.getGrammar().getProductions();
    }

    public List<Production> getProductionsForNonterminal(String nonTerminal) {
        return parser.getGrammar().getProductionsForNonterminal(nonTerminal);
    }

    public String getStartingSymbol() {
        return parser.getGrammar().getStartingSymbol();
    }

    public Map<String, Set<String>> getFollowSet() {
        return parser.getFollowSet();
    }

    public ParseTable getParseTable() {
        return parser.getParseTable();
    }

    public void parse(List<String> w) {
        boolean result = parser.parse(w);
        if (result) {
            System.out.println("Sequence " + w + " accepted.");
            Stack<String> pi = parser.getPi();
            System.out.println(pi);
            System.out.println(displayPiProductions(pi));
        } else {
            System.out.println("Sequence " + w + " is not accepted.");
        }
    }

    private String displayPiProductions(Stack<String> pi) {
        StringBuilder sb = new StringBuilder();

        for (String productionIndexString : pi) {
            if (productionIndexString.equals("ε")) {
                continue;
            }
            Integer productionIndex = Integer.parseInt(productionIndexString);
            parser.getProductionsNumbered().forEach((key, value) ->{
                if (productionIndex.equals(value))
                    sb.append(value).append(": ").append(key.getKey()).append(" -> ").append(key.getValue()).append("\n");
            });
        }

        return sb.toString();
    }



    public List<Pair<Integer, Integer>> scanSourceCode() {
        List<String> errors = scanner.run();

        if (errors.size() == 0) {
            List<Pair<Integer, Integer>> pif = scanner.getPif();
            System.out.println(pif);
            scanner.displayPIFReadable(pif);

            List<String> w = new ArrayList<>();
            pif.forEach(elem -> w.add(String.valueOf(elem.getKey())));

            System.out.println(w);
            this.parse(w);



            return pif;
        } else {
            for (String error : errors) {
                System.out.println(error);
            }
        }

        return null;
    }

    public void parsePIF() {
        List<Pair<Integer, Integer>> pif = scanSourceCode();
        if (pif != null) {
            System.out.println(parser.parseSource(pif));
        }
    }
}
