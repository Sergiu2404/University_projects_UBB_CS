import java.util.ArrayList;

public class HashTable {
    private Integer size;
    private Integer count;
    private ArrayList<ArrayList<String>> table;

    public HashTable(Integer size) {
        this.size = size;
        this.count = 0;
        this.table = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.table.add(new ArrayList<>());
        }
    }

    private Integer hash(String key) {
        int sum_chars = 0;
        char[] key_characters = key.toCharArray();
        for (char c : key_characters) {
            sum_chars += c;
        }
        return sum_chars % size;
    }

    public boolean add(String term) {
        if (containsTerm(term)) {
            return false;
        }

        if (loadFactor() >= 0.7) {
            resize();
        }

        Integer pos = hash(term);
        ArrayList<String> elems = this.table.get(pos);
        elems.add(term);
        count++;
        return true;
    }

    public boolean remove(String term) {
        Integer pos = hash(term);
        ArrayList<String> elems = this.table.get(pos);
        boolean removed = elems.remove(term);
        if (removed) {
            count--;
        }
        return removed;
    }

    private double loadFactor() {
        return (double) count / size;
    }

    private void resize() {
        Integer newSize = size * 2;
        ArrayList<ArrayList<String>> newTable = new ArrayList<>();

        for (int i = 0; i < newSize; i++) {
            newTable.add(new ArrayList<>());
        }

        for (ArrayList<String> bucket : table) {
            for (String term : bucket) {
                int newPos = hash(term) % newSize;
                newTable.get(newPos).add(term);
            }
        }

        this.table = newTable;
        this.size = newSize;
    }

    public boolean containsTerm(String term) {
        return this.findPositionOfTerm(term) != null;
    }

    public Pair<Integer, Integer> findPositionOfTerm(String term) {
        int pos = hash(term);
        if (!table.get(pos).isEmpty()) {
            ArrayList<String> elems = this.table.get(pos);
            for (int i = 0; i < elems.size(); i++) {
                if (elems.get(i).equals(term)) {
                    return new Pair<>(pos, i);
                }
            }
        }
        return null;
    }

    public String findByPos(Pair<Integer, Integer> position) {
        if (position.getFirst() < 0 || position.getFirst() >= size ||
                position.getSecond() < 0 || position.getSecond() >= table.get(position.getFirst()).size()) {
            throw new IndexOutOfBoundsException("Invalid position");
        }
        return table.get(position.getFirst()).get(position.getSecond());
    }

    public Integer getCount() {
        return count;
    }

    public Integer getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "HashTable { " + "elements=" + table + ", size=" + size + ", count=" + count + " }";
    }
}
