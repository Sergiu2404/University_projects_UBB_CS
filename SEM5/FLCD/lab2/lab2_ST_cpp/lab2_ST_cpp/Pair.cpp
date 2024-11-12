#include "Pair.h"

// Get first value
int Pair::getFirst() {
    return this->first;
}

// Get second value
int Pair::getSecond() {
    return this->second;
}

// ToString equivalent
std::string Pair::toString() {
    return "Pair{first=" + std::to_string(first) + ", second=" + std::to_string(second) + "}";
}

// Constructor
Pair::Pair(int first, int second) {
    this->first = first;
    this->second = second;
}

// Default constructor
Pair::Pair() {
    this->first = -1;
    this->second = -1;
}
