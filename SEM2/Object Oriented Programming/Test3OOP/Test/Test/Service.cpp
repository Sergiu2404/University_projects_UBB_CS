#include "Service.h"
#include <algorithm>
#include <fstream>


bool compare_name_category(Shopping s1,Shopping s2) {
	if (s1.getCategory() != s2.getCategory())
		return s1.getCategory() < s2.getCategory();
	else {
		return s1.getName() < s2.getName();
	}
}

bool compare_quantity(Shopping s1, Shopping s2) {
	return s1.getQuantity() > s2.getQuantity();
}

vector<Shopping> Service::getAll()
{
	vector<Shopping> sorted = this->shoppings;
	sort(sorted.begin(), sorted.end(), compare_name_category);
	return sorted;
}

vector<Shopping> Service::getCategory(string c)
{
	vector<Shopping> result;
	for (int i = 0; i < this->getAll().size(); i++)
		if (this->getAll()[i].getCategory() == c)
			result.push_back(this->getAll()[i]);

	sort(result.begin(), result.end(), compare_quantity);
	return result;
}

vector<Shopping> Service::getFiltered(string name)
{
	vector<Shopping> result;

	char name_char[100];
	strcpy(name_char, name.c_str());
	for (int i = 0; i < this->getAll().size(); i++)
		//if (strstr(this->getAll()[i].getName().c_str(), name_char) || strstr(this->getAll()[i].getCategory().c_str(),name_char))
		if (this->getAll()[i].getName().find(name) !=string::npos || this->getAll()[i].getCategory().find(name)!=string::npos)
			result.push_back(this->getAll()[i]);

	return result;
}

void Service::read_file()
{
	ifstream fin("shopping.txt");

	string line;
	while (fin >> line)
	{
		string name = "-1", category = "-1"; int quantity = -1;

		char line_char[100];
		strcpy(line_char, line.c_str());

		char* p = strtok(line_char, "|");

		while (p) {
			if (category == "-1")
				category = string(p);
			else if (name == "-1")
				name = string(p);
			else if (quantity == -1)
				quantity = atoi(p);

			p = strtok(NULL, "|");
		}
		this->shoppings.push_back(Shopping(category,name,quantity));
	}
	fin.close();
}
