#pragma once
#include "Hospital.h"
class Neonatal :
    public Hospital
{
private:
    double average;

public:
    Neonatal(string n, int nr, double avg) :
        Hospital{ n,nr }, average{ avg } {};

    double getAverage() { return this->average; }

    string toString() override;
    bool efficient() override;

};

