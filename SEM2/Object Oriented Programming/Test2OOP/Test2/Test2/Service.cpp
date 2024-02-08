#include "Service.h"
#include <fstream>
#include <algorithm>

bool compare(Hospital* h1, Hospital* h2)
{
    return h1->getName() < h2->getName();
}



bool Service::add(Hospital* h)
{
    if (this->find_position(h) != -1)
        return false;
    this->hospitals.push_back(h);
    return true;
}

int Service::find_position(Hospital* h)
{
    for (int i = 0; i < this->getAll().size(); i++)
        if (this->getAll()[i]->toString() == h->toString())
            return i;
    return -1;
}

vector<Hospital*> Service::sortHospitals()
{
    vector<Hospital*> result = this->getAll();

    sort(result.begin(), result.end(), compare);

    return result;
}

vector<Hospital*> Service::getAll()
{
    return this->hospitals;
}

vector<Hospital*> Service::getEfficient()
{
    vector<Hospital*> result;
    for (int i = 0; i < this->getAll().size(); i++)
        if (this->getAll()[i]->efficient() == true)
            result.push_back(this->getAll()[i]);
    return result;
}

void Service::writeFile(string filename)
{
    vector<Hospital*> sorted;
    sorted = this->getAll();

    sort(sorted.begin(), sorted.end(), compare);

    ofstream fout(filename);

    for (auto hospital : sorted) {
        string isEfficient;
        if (hospital->efficient() == true)
            isEfficient = "true";
        else isEfficient = "false";
        fout << "type: " << typeid(*hospital).name() << ", " << hospital->toString() << ", efficient: " << isEfficient << "\n";
    }

    fout.close();
}
