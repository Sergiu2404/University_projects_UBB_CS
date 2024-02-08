#pragma once
#include "Shopping.h"
#include <vector>
class Service {
private:
	vector<Shopping> shoppings;

public:
	Service() {
		this->read_file();
	}

	vector<Shopping> getAll();
	vector<Shopping> getCategory(string c);
	vector<Shopping> getFiltered(string name);

	void read_file();
};