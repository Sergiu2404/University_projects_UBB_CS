#include "Car.h"
#include <sstream>
string Car::toString()
{
	ostringstream o;
	o << this->brand << "," << this->model << "," << this->year << "," << this->color;
	return o.str();
}
