public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(5);

        symbolTable.addIdentifier("x");
        System.out.println(String.format("Identifier added: %b", symbolTable.containsTerm("x", false)));
        Pair idPosition = symbolTable.findPositionOfTerm("x1", false);
        System.out.println("Position of 'x1': " + idPosition);

        symbolTable.addIdentifier("y");
        System.out.println(String.format("Identifier added: %b", symbolTable.containsTerm("y", false)));
        System.out.println("Position of 'y': " + symbolTable.findPositionOfTerm("y", false));

        symbolTable.addConstant("PI");
        System.out.println(String.format("Constant added: %b", symbolTable.containsTerm("PI", true)));
        Pair constPosition = symbolTable.findPositionOfTerm("PI", true);
        System.out.println("Position of 'PI': " + constPosition);

        System.out.println("Removing identifier 'x': " + symbolTable.removeTerm("x", false));
        System.out.println(String.format("Identifier 'x' exists: %b", symbolTable.containsTerm("x", false)));

        System.out.println("Removing constant 'PI': " + symbolTable.removeTerm("PI", true));
        System.out.println(String.format("Constant 'PI' exists: %b", symbolTable.containsTerm("PI", true)));

        // Adding more elements for checking resize
        symbolTable.addIdentifier("z");
        System.out.println(String.format("Identifier added: %b", symbolTable.containsTerm("z", false)));
        System.out.println("Position of 'z': " + symbolTable.findPositionOfTerm("z", false));

        symbolTable.addConstant("E");
        System.out.println(String.format("Constant added: %b", symbolTable.containsTerm("E", true)));
        System.out.println("Position of 'E': " + symbolTable.findPositionOfTerm("E", true));

        System.out.println("Adding identifiers to fill to max capacity to see if resize function works:");

        symbolTable.addIdentifier("a");
        symbolTable.addIdentifier("b");
        symbolTable.addIdentifier("c");
        symbolTable.addIdentifier("d");
        symbolTable.addIdentifier("e");

        System.out.println("Table after adding more identifiers:");
        System.out.println(symbolTable.getIdentifierTable());

        System.out.println("Adding constants X_CONST and Y_CONST:");
        symbolTable.addConstant("X_CONST");
        symbolTable.addConstant("Y_CONST");

        System.out.println("Table after adding constants:");
        System.out.println(symbolTable.getConstantTable());

        System.out.println("Final identifier:");
        System.out.println(symbolTable.getIdentifierTable());
        System.out.println("Final constant:");
        System.out.println(symbolTable.getConstantTable());
    }
}
