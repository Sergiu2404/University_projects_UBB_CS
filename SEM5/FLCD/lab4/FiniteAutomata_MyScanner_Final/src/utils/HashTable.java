package utils;

import java.util.ArrayList;

public class HashTable {
    private Integer size;
    private ArrayList<ArrayList<String>> table;
    private String type;

    public HashTable(Integer size, String type) {
        this.size = size;
        this.table = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.table.add(new ArrayList<>());
        }

        this.type = type;
    }

    public Pair<Integer, Integer> findPositionOfTerm(String elem) {
        int pos = hash(elem);
        if (!table.get(pos).isEmpty()) {
            ArrayList<String> elems = this.table.get(pos);
            for (int i = 0; i < elems.size(); i++) {
                if (elems.get(i).equals(elem)) {
                    return new Pair<>(pos, i);
                }
            }
        }
        return null;
    }

    private Integer hash(String key) {
        int sum_chars = 0;
        char[] key_characters = key.toCharArray();
        for (char c : key_characters) {
            sum_chars += c;
        }
        return sum_chars % size;
    }

    public boolean containsTerm(String elem) {
        return this.findPositionOfTerm(elem) != null;
    }

    public boolean add(String elem) {
        if (containsTerm(elem)) {
            return false;
        }
        Integer pos = hash(elem);
        ArrayList<String> elems = this.table.get(pos);
        elems.add(elem);
        return true;
    }

    public boolean remove(String elem) {
        Pair<Integer, Integer> pos = findPositionOfTerm(elem);
        if (pos == null) {
            return false; // Element not found
        }
        int bucketIndex = pos.getKey();
        int elementIndex = pos.getValue();
        this.table.get(bucketIndex).remove((int) elementIndex);
        return true;
    }

    @Override
    public String toString() {
        StringBuilder computedString = new StringBuilder();
        computedString.append(type).append(": ");
        for (int i = 0; i < this.table.size(); i++) {
            if (this.table.get(i).size() > 0) {
                computedString.append(i);
                computedString.append(" - ");
                computedString.append(this.table.get(i));
                computedString.append("\n");
            }
        }
        return computedString.toString();
    }
}
