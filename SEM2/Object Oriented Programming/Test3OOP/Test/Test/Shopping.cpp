#include "Shopping.h"
#include <sstream>
string Shopping::toString()
{
	ostringstream o;
	o << this->category << "|" << this->name << "|" << this->quantity;
	return o.str();
}
