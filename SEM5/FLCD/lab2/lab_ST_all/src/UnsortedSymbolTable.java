import java.util.ArrayList;

public class UnsortedSymbolTable implements SymbolTableInterface {
    private ArrayList<String> table;

    public UnsortedSymbolTable() {
        this.table = new ArrayList<>();
    }

    @Override
    public boolean add(String term) {
        if (table.contains(term)) return false;
        table.add(term);
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
        return new Pair(index, 0); // Only one dimension, so second is 0
    }

    @Override
    public Integer getSize() {
        return table.size();
    }

    @Override
    public String toString() {
        return "UnsortedSymbolTable{" + "table=" + table + '}';
    }
}
