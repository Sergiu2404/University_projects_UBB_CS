public class SymbolTable {
    private HashTable identifierTable;
    private HashTable constantTable;

    public SymbolTable(int initialSize) {
        this.identifierTable = new HashTable(initialSize);
        this.constantTable = new HashTable(initialSize);
    }

    public HashTable getIdentifierTable() {
        return identifierTable;
    }

    public HashTable getConstantTable() {
        return constantTable;
    }

    public Pair findPositionOfTerm(String term, boolean isConstant) {
        return isConstant ? constantTable.findPositionOfTerm(term) : identifierTable.findPositionOfTerm(term);
    }


    public boolean addTerm(String term, boolean isConstant) {
        return isConstant ? constantTable.add(term) : identifierTable.add(term);
    }

    public boolean removeTerm(String term, boolean isConstant) {
        return isConstant ? constantTable.remove(term) : identifierTable.remove(term);
    }
}
