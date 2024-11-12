#ifndef SYMBOLTABLE_H
#define SYMBOLTABLE_H

#include "HashTable.h"

class SymbolTable {
private:
    HashTable hashTable;

public:
    SymbolTable(int size);

    std::string findByPos(Pair pos);
    HashTable getHashTable();
    int getSize();
    Pair findPositionOfTerm(const std::string& term);
    bool containsTerm(const std::string& term);
    bool add(const std::string& term);
};

#endif // SYMBOLTABLE_H
