package utils;

public class SymbolTable {
    private HashTable constantsHashTable;
    private HashTable identifiersHashTable;

    public SymbolTable(Integer size){
        constantsHashTable = new HashTable(size, "Constants HashTable");
        identifiersHashTable = new HashTable(size, "Identifiers HashTable");
        //hashTable = new HashTable(size);
    }

//    public Pair findPositionOfTerm(String term){
//        return hashTable.findPositionOfTerm(term);
//    }

    public HashTable getConstantsHashTable(){ return constantsHashTable; }
    public HashTable getIdentifiersHashTable(){ return identifiersHashTable; }

    public Pair findPositionOfConstant(String constant){
        return constantsHashTable.findPositionOfTerm(constant);
    }

    public Pair findPositionOfIdentifier(String identifier){
        return identifiersHashTable.findPositionOfTerm(identifier);
    }

    public boolean addConstant(String term) { return constantsHashTable.add(term); }
    public boolean addIdentifier(String term) { return identifiersHashTable.add(term); }

    @Override
    public String toString(){
        return constantsHashTable.toString() + "\n" + identifiersHashTable.toString() + "\n";
    }

}
