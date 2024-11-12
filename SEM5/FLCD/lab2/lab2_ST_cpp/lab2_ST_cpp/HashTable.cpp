#include "HashTable.h"
#include <stdexcept>
#include <sstream>

// Hash function
int HashTable::hash(const std::string& key) {
    int sumChars = 0;
    for (char c : key) {
        sumChars += c;
    }
    return sumChars % size;
}

// Constructor
HashTable::HashTable(int size) {
    this->size = size;  // Fix: Use this->size to assign to the member variable
    hashTable.resize(size);
}

// Find by position
std::string HashTable::findByPosition(Pair position) {
    if (hashTable.size() <= position.getFirst() || hashTable[position.getFirst()].size() <= position.getSecond()) {
        throw std::out_of_range("Invalid position");
    }
    return hashTable[position.getFirst()][position.getSecond()];
}

// Get size
int HashTable::getSize() {
    return size;
}

// Find position of a term
Pair HashTable::findPositionOfTerm(const std::string& term) {
    int position = hash(term);
    if (!hashTable[position].empty()) {
        for (int i = 0; i < hashTable[position].size(); i++) {
            if (hashTable[position][i] == term)
                return Pair(position, i);
        }
    }
    return Pair(-1, -1);
}

// Check if a term exists
bool HashTable::containsTerm(const std::string& term) {
    Pair pos = findPositionOfTerm(term);
    return pos.getFirst() != -1 && pos.getSecond() != -1;
}

// Add a term
bool HashTable::add(const std::string& term) {
    if (containsTerm(term)) {
        return false;
    }
    int pos = hash(term);
    hashTable[pos].push_back(term);
    return true;
}

// ToString equivalent
std::string HashTable::toString() {
    std::stringstream ss;
    ss << "SymbolTable { elements= [";
    for (int i = 0; i < hashTable.size(); ++i) {
        ss << "[";
        for (int j = 0; j < hashTable[i].size(); ++j) {
            ss << hashTable[i][j];
            if (j != hashTable[i].size() - 1) {
                ss << ", ";
            }
        }
        ss << "]";
        if (i != hashTable.size() - 1) {
            ss << ", ";
        }
    }
    ss << "], size = " << size << " }";
    return ss.str();
}
