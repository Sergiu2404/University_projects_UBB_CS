#pragma once
#include <iostream>

using namespace std;
class Shopping {
private:
	string category, name;
	int quantity;

public:
	Shopping(string category, string name, int quantity) :
		category{ category }, name{ name }, quantity{ quantity } {};

	string getCategory() { return this->category; }
	string getName() { return this->name; }
	int getQuantity() { return this->quantity; }
	
	string toString();
};
