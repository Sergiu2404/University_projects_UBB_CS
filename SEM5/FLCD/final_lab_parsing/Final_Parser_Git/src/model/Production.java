package model;

import java.util.List;

public class Production {
    private String start;
    private List<List<String>> rules;

    Production(String start, List<List<String>> rules) {
        this.start = start;
        this.rules = rules;
    }

    List<List<String>> getRules() {
        return rules;
    }

    String getStart() {
        return start;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(start + " -> ");
        for (List<String> rule : rules) {
            for (String element: rule)
                sb.append(element).append(" ");
            sb.append("| ");
        }
        sb.replace(sb.length() - 3, sb.length() - 1, "");
        return sb.toString();
    }
}
