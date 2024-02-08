#pragma once
#include "Car.h"
#include <vector>

class Service {
private:
	vector<Car> cars;

public:
	Service() {
		this->read_file();
	}
	void read_file();

	bool add(Car car);
	int find_position(Car car);

	vector<Car> getAll();
	vector<Car> getFiltered(vector<string> colors);
	vector<Car> getManufacturer(string manufacturer);
};