#pragma once
#include "Hospital.h"
class Surgery :
    public Hospital
{
private:
    int patientsNumber;
public:
    Surgery(string n, int nr, int nrp) :
        Hospital{ n,nr }, patientsNumber{ nrp } {};

    int getPatientsNumber() { return this->patientsNumber; }

    string toString() override;
    bool efficient() override;
};

