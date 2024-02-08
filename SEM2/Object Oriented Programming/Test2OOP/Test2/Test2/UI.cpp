#include "UI.h"
#include "Neonatal.h"
#include "Surgery.h"
#include <string>

void UI::printMenu()
{
	cout << "1-add\n"
		"2-show all\n"
		"3-show efficient\n"
		"4-save to file\n"
		"5-exit\n";
}

void UI::runMenu()
{
	Hospital* h1 = new Neonatal("h1 Neonatal", 200, 9); //efficient
	Hospital* h2 = new Surgery("h2 Surgery", 100, 200); //efficient
	Hospital* h3 = new Neonatal("h3 Neonatal",15 ,7.25); //not efficient
	Hospital* h4 = new Surgery("h4 Surgery", 100, 199); //not efficient

	service.add(h3);
	service.add(h2);
	service.add(h1);

	while (true)
	{
		this->printMenu();

		string option;
		cout << "option: ";
		getline(cin, option);

		if (option == "1")
			this->add();
		else if (option == "2")
			this->showAll();
		else if (option == "3")
			this->showEfficient();
		else if (option == "4")
			this->saveFile();
		else if (option == "5")
			break;
		else
			cout << "invalid\n";
	}
}

void UI::add()
{
	cout << "Surgery/Neonatal: ";
	string option;
	getline(cin, option);

	if (option == "Surgery")
	{
		string name;
		int doctorsNumber;
		double average;

		cout << "name: ";
		getline(cin, name);

		cout << "number doctors: ";
		cin >> doctorsNumber;

		cout << "average: ";
		cin >> average;

		cin.ignore();

		Hospital* h = new Surgery(name, doctorsNumber, average);

		if (this->service.add(h) == false)
			cout << "not added\n";
		else {
			this->service.add(h);
			cout << "added\n";
		}
	}
	else
	{
		string name;
		int doctorsNumber;
		int patientsNumber;

		cout << "name: ";
		getline(cin, name);

		cout << "number doctors: ";
		cin >> doctorsNumber;

		cout << "patients number: ";
		cin >> patientsNumber;

		cin.ignore();

		Hospital* h = new Neonatal(name, doctorsNumber, patientsNumber);

		if (this->service.add(h) == false)
			cout << "not added\n";
		else {
			this->service.add(h);
			cout << "added\n";
		}
	}
}

void UI::showAll()
{
	for (auto h : this->service.getAll())
		cout << h->toString() << "\n";
}

void UI::saveFile()
{
	this->service.writeFile("file.txt");
	cout << "printed\n";
}

void UI::showEfficient()
{
	for (auto h : this->service.getEfficient())
		cout << h->toString() << "\n";
}
