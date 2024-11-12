#pragma once
using namespace std;
#include <string>
#include <vector>
#include "Pair.h"

class HashTable {
	int size;
	vector<vector<string>> hashTable;

	int hash(const string& key);

public:
	HashTable(int size);

	string findByPosition(Pair position);
	int getSize();
	Pair findPositionOfTerm(const string& term);
	bool containsTerm(const string& term);
	bool add(const string& term);

	string toString();
};