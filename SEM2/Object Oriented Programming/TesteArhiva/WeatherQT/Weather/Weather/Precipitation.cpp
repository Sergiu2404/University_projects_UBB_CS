#include "Precipitation.h"

#include <sstream>

string Precipitation::representation()
{
	ostringstream output;
	output << this->start << ";" << this->end << ";" << this->probability << ";" << this->description;
	return output.str();
}
