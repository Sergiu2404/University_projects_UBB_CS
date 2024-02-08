#include "Surgery.h"
#include <sstream>

string Surgery::toString()
{
    ostringstream o;
    o << this->getName() << ", " << this->getDoctorsNumber() << ", " << this->getPatientsNumber();
    return o.str();
}

bool Surgery::efficient()
{
    return (double)this->getPatientsNumber()/(double)this->getDoctorsNumber()>=2;
}
