#pragma once
#include <iostream>

using namespace std;

class Precipitation {
private:
	int start, end, probability;
	string description;

public:
	Precipitation(int start, int end, int probability, string description) :
		start{ start }, end{ end }, probability{ probability }, description{ description } {};

	string getDescription() { return this->description; }
	int getStart() { return this->start; }
	int getEnd() { return this->end; }
	int getProbability() { return this->probability; }

	string representation();
};