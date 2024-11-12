import java.util.ArrayList;

public class SymbolTable {
    private HashTable identifierTable;
    private HashTable constantTable;

    public SymbolTable(int initialSize) {
        this.identifierTable = new HashTable(initialSize);
        this.constantTable = new HashTable(initialSize);
    }

    public String findByPosition(Pair position, boolean isConstant) {
        try {
            return isConstant ? constantTable.findByPos(position) : identifierTable.findByPos(position);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Invalid position: " + position);
            return null;
        }
    }

    public HashTable getIdentifierTable() {
        return identifierTable;
    }

    public HashTable getConstantTable() {
        return constantTable;
    }

    public int getIdentifierSize() {
        return identifierTable.getSize();
    }

    public int getConstantSize() {
        return constantTable.getSize();
    }

    public Pair findPositionOfTerm(String term, boolean isConstant) {
        return isConstant ? constantTable.findPositionOfTerm(term) : identifierTable.findPositionOfTerm(term);
    }

    public boolean containsTerm(String term, boolean isConstant) {
        return isConstant ? constantTable.containsTerm(term) : identifierTable.containsTerm(term);
    }

    public boolean addTerm(String term, boolean isConstant) {
        return isConstant ? constantTable.add(term) : identifierTable.add(term);
    }

    public boolean removeTerm(String term, boolean isConstant) {
        return isConstant ? constantTable.remove(term) : identifierTable.remove(term);
    }

    public void addIdentifier(String identifier) {
        if (!addTerm(identifier, false)) {
            System.out.println("Identifier already exists: " + identifier);
        }
    }

    public void addConstant(String constant) {
        if (!addTerm(constant, true)) {
            System.out.println("Constant already exists: " + constant);
        }
    }
}
