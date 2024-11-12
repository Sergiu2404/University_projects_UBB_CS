#include <iostream>
#include "SymbolTable.h"
#include "Pair.h"

int main() {
    SymbolTable symbolTable(5);

    // Add term "1"
    symbolTable.add("1");
    std::cout << symbolTable.containsTerm("1") << std::endl;  // Expected: 1 (true)
    Pair position = symbolTable.findPositionOfTerm("1");
    std::cout << position.toString() << std::endl;  // Print the position

    // Add term "6"
    symbolTable.add("6");
    std::cout << symbolTable.containsTerm("6") << std::endl;  // Expected: 1 (true)
    std::cout << symbolTable.findPositionOfTerm("6").toString() << std::endl;

    // Add term "5"
    symbolTable.add("5");
    std::cout << symbolTable.containsTerm("5") << std::endl;  // Expected: 1 (true)
    std::cout << symbolTable.findPositionOfTerm("5").toString() << std::endl;

    // Print the symbol table
    std::cout << symbolTable.getHashTable().toString() << std::endl;

    return 0;
}
