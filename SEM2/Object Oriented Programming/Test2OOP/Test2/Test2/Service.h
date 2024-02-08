#pragma once
#include <vector>
#include "Hospital.h"

class Service
{
private:
	vector<Hospital*> hospitals;

public:
	Service() {};
	Service(vector<Hospital*> h) :hospitals{ h } {};

	bool add(Hospital* h);
	int find_position(Hospital* h);

	vector<Hospital*> sortHospitals();
	vector<Hospital*> getAll();
	vector<Hospital*> getEfficient();

	void writeFile(string filename);
};

