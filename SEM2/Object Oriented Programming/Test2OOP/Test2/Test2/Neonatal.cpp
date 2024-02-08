#include "Neonatal.h"
#include <sstream>

string Neonatal::toString()
{
    ostringstream o;
    o << this->getName() << ", " << this->getDoctorsNumber() << ", " << this->getAverage();
    return o.str();
}

bool Neonatal::efficient()
{
    return this->getAverage()>8.5;
}
