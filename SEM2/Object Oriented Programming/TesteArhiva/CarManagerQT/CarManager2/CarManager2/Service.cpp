#include "Service.h"
#include <fstream>
void Service::read_file()
{
	ifstream fin("cars.txt");

	string line;
	while (fin >> line)
	{
		string brand = "-1", model = "-1", color = "-1"; int year = -1;

		char line_char[100];

		strcpy(line_char, line.c_str());

		char* p = strtok(line_char, ",");
		while (p)
		{
			if (brand == "-1")
				brand = string(p);
			else if (model == "-1")
				model = string(p);
			else if (year == -1)
				year = atoi(p);
			else if (color == "-1")
				color = string(p);

			p = strtok(NULL, ",");
		}
		this->cars.push_back(Car(brand, model, year, color));
	}
	fin.close();
}

int Service::find_position(Car car)
{
	for (int i = 0; i < this->getAll().size(); i++)
		if (this->getAll()[i].getModel() == car.getModel() && this->getAll()[i].getYear() == car.getYear())
			return i;

	return -1;
}

bool Service::add(Car car)
{
	if (this->find_position(car) != -1)
		return false;

	this->cars.push_back(car);
	return true;
}

vector<Car> Service::getAll()
{
	return this->cars;
}

vector<Car> Service::getFiltered(vector<string> colors)
{
	vector<Car> result;

	for (auto car : this->cars)
		for (auto color : colors)
			if (car.getColor() == color)
				result.push_back(car);

	return result;
}

vector<Car> Service::getManufacturer(string manufacturer)
{
	vector<Car> result;

	for (auto car : this->cars)
		if (car.getBrand() == manufacturer)
			result.push_back(car);

	return result;
}
