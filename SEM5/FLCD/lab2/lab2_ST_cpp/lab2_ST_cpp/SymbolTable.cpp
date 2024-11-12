#include "SymbolTable.h"

// Constructor
SymbolTable::SymbolTable(int size) : hashTable(size) {}

// Find by position
std::string SymbolTable::findByPos(Pair pos) {
    return hashTable.findByPosition(pos);
}

// Get hash table
HashTable SymbolTable::getHashTable() {
    return hashTable;
}

// Get size
int SymbolTable::getSize() {
    return hashTable.getSize();
}

// Find position of term
Pair SymbolTable::findPositionOfTerm(const std::string& term) {
    return hashTable.findPositionOfTerm(term);
}

// Check if the term exists
bool SymbolTable::containsTerm(const std::string& term) {
    return hashTable.containsTerm(term);
}

// Add a term
bool SymbolTable::add(const std::string& term) {
    return hashTable.add(term);
}
