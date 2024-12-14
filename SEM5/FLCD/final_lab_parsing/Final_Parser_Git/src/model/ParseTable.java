package model;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseTable {
    private Map<Pair<String, String>, Pair<List<String>, Integer>> table = new HashMap<>();

    public void put(Pair<String, String> key, Pair<List<String>, Integer> value) {
        table.put(key, value);
    }

    public Pair<List<String>, Integer> get(Pair<String, String> key) {
        for (Map.Entry<Pair<String, String>, Pair<List<String>, Integer>> entry : table.entrySet()) {
            if (entry.getValue() != null) {
                Pair<String, String> currentKey = entry.getKey();
                Pair<List<String>, Integer> currentValue = entry.getValue();

                if (currentKey.getKey().equals(key.getKey()) && currentKey.getValue().equals(key.getValue())) {
                    return currentValue;
                }
            }
        }

        return null;
    }

    public boolean containsKey(Pair<String, String> key) {
        boolean result = false;
        for (Pair<String, String> currentKey : table.keySet()) {
            if (currentKey.getKey().equals(key.getKey()) && currentKey.getValue().equals(key.getValue())) {
                result = true;
            }
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Pair<String, String>, Pair<List<String>, Integer>> entry : table.entrySet()) {
            if (entry.getValue() != null) {
                Pair<String, String> key = entry.getKey();
                Pair<List<String>, Integer> value = entry.getValue();

                sb.append("M[").append(key.getKey()).append(",").append(key.getValue()).append("] = [")
                        .append(value.getKey()).append(",").append(value.getValue()).append("]\n");
            }
        }

        return sb.toString();
    }
}
