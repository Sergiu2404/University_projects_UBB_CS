#pragma once
#include <iostream>
using namespace std;


class Car {
private:
	string brand, model, color;
	int year;

public:
	Car(string brand, string model, int year, string color) :
		brand{ brand }, model{ model }, year{ year }, color{ color } {};

	string getBrand() { return this->brand; }
	string getModel() { return this->model; }
	string getColor() { return this->color; }
	int getYear() { return this->year; }

	string toString();
};