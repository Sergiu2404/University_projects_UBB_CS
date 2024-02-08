#pragma once
#include <iostream>
using namespace std;

class Hospital
{
private:
	string name;
	int doctorsNumber;
public:
	Hospital(string n, int nr) :name{ n }, doctorsNumber{ nr } {};

	string getName() { return this->name; }
	int getDoctorsNumber() { return this->doctorsNumber; }

	virtual string toString() = 0;
	virtual bool efficient() = 0;
};

