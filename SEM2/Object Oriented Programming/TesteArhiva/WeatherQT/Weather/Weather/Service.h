#pragma once
#include <vector>
#include "Precipitation.h"

class Service {
private:
	vector<Precipitation> intervals;

public:
	Service() {
		this->read_file();
	}

	vector<Precipitation> getAll();
	vector<Precipitation> getAllDescription(vector<string> descriptions);
	//vector<Precipitation>

	void read_file();
};