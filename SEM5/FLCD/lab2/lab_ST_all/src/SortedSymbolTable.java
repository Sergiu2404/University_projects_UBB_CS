import java.util.ArrayList;
import java.util.Collections;

public class SortedSymbolTable implements SymbolTableInterface {
    private ArrayList<String> table;

    public SortedSymbolTable() {
        this.table = new ArrayList<>();
    }

    @Override
    public boolean add(String term) {
        if (table.contains(term)) return false;
        table.add(term);
        Collections.sort(table);  // Sort after adding
        return true;
    }

    @Override
    public boolean containsTerm(String term) {
        return table.contains(term);
    }

    @Override
    public String findByPos(Pair pos) {
        if (pos.getFirst() >= table.size()) {
            throw new IndexOutOfBoundsException("Invalid position");
        }
        return table.get(pos.getFirst());
    }

    @Override
    public Pair findPositionOfTerm(String term) {
        int index = table.indexOf(term);
        if (index == -1) return null;
        return new Pair(index, 0); // Second value is 0 for 1D
    }

    @Override
    public Integer getSize() {
        return table.size();
    }

    @Override
    public String toString() {
        return "SortedSymbolTable{" + "table=" + table + '}';
    }
}
