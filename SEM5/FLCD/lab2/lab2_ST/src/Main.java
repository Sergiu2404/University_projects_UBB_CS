public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(5);

        symbolTable.addIdentifier("x");
        System.out.println(String.format("Identifier added: %b", symbolTable.containsTerm("x", false)));
        Pair idPosition = symbolTable.findPositionOfTerm("x", false);
        System.out.println("Position of 'x': " + idPosition);

        symbolTable.addIdentifier("y");
        System.out.println(String.format("Identifier added: %b", symbolTable.containsTerm("y", false)));
        System.out.println("Position of 'y': " + idPosition);

        symbolTable.addConstant("PI");
        System.out.println(String.format("Constant added: %b", symbolTable.containsTerm("PI", true)));
        Pair constPosition = symbolTable.findPositionOfTerm("PI", true);
        System.out.println("Position of 'PI': " + constPosition);

        // Adding more elements for checking resize
        symbolTable.addIdentifier("z");
        System.out.println(String.format("Identifier added: %b", symbolTable.containsTerm("z", false)));
        System.out.println("Position of 'z': " + idPosition);

        symbolTable.addConstant("E");
        System.out.println(String.format("Constant added: %b", symbolTable.containsTerm("E", true)));
        System.out.println("Position of 'E': " + constPosition);

        symbolTable.addIdentifier("a");
        symbolTable.addIdentifier("b");
        symbolTable.addIdentifier("c");
        symbolTable.addIdentifier("d");
        symbolTable.addIdentifier("e");

        System.out.println("Addig identifies to fill to max capacity to see if resize funciton works:");
        System.out.println("Table after adding more identifiers:");
        System.out.println(symbolTable.getIdentifierTable());

        System.out.println("Adding constants:");
        symbolTable.addConstant("X_CONST");
        symbolTable.addConstant("Y_CONST");

        System.out.println("Table after adding constants:");
        System.out.println(symbolTable.getConstantTable());

        System.out.println("Final identifier table:");
        System.out.println(symbolTable.getIdentifierTable());
        System.out.println("Final constant table:");
        System.out.println(symbolTable.getConstantTable());
    }
}
