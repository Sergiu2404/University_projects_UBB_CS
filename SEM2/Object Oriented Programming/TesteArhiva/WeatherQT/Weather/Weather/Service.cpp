#include "Service.h"

#include <fstream>

vector<Precipitation> Service::getAll()
{
	return this->intervals;
}

vector<Precipitation> Service::getAllDescription(vector<string> descriptions)
{
	vector<Precipitation> result;

	for (auto it : this->intervals)
		for (auto description : descriptions)
			if (it.getDescription() == description)
				result.push_back(it);

	return result;
}

void Service::read_file()
{
	ifstream fin("weather.txt");

	string line;

	while (fin >> line)
	{
		string description = "-1"; int start = -1, end = -1, probability = -1;

		char line_char[100];
		strcpy(line_char, line.c_str());

		char* p = strtok(line_char, ";");

		while (p != NULL)
		{
			if (start == -1)
				start = atoi(p);
			else if (end == -1)
				end = atoi(p);
			else if (probability == -1)
				probability = atoi(p);
			else if (description == "-1")
				description = string(p);

			p = strtok(NULL, ";");
		}

		this->intervals.push_back(Precipitation(start, end, probability, description));

	}
	fin.close();
}
