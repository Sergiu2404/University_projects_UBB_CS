import java.util.ArrayList;

public class HashTable implements SymbolTableInterface {
    private Integer size;
    private ArrayList<ArrayList<String>> table;

    public HashTable(Integer size){
        this.size = size;
        this.table = new ArrayList<>();
        for(int i = 0; i < size; i++){
            this.table.add(new ArrayList<>());
        }
    }

    @Override
    public String findByPos(Pair pos){
        if(this.table.size() <= pos.getFirst() || this.table.get(pos.getFirst()).size() <= pos.getSecond()){
            throw new IndexOutOfBoundsException("Invalid position");
        }
        return this.table.get(pos.getFirst()).get(pos.getSecond());
    }

    @Override
    public Integer getSize(){
        return size;
    }

    @Override
    public Pair findPositionOfTerm(String term){
        int pos = hash(term);
        if(!table.get(pos).isEmpty()){
            ArrayList<String> elems = this.table.get(pos);
            for(int i = 0; i < elems.size(); i++){
                if(elems.get(i).equals(term)){
                    return new Pair(pos, i);
                }
            }
        }
        return null;
    }

    private Integer hash(String key){
        int sumChars = 0;
        for(char c : key.toCharArray()){
            sumChars += c;
        }
        return sumChars % size;
    }

    @Override
    public boolean containsTerm(String term){
        return this.findPositionOfTerm(term) != null;
    }

    @Override
    public boolean add(String term){
        if(containsTerm(term)){
            return false;
        }
        Integer pos = hash(term);
        ArrayList<String> elems = this.table.get(pos);
        elems.add(term);
        return true;
    }

    @Override
    public String toString() {
        return "HashTable { elements=" + table + ", size=" + size + " }";
    }
}
