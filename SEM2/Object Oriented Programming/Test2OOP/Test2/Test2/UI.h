#pragma once
#include "Service.h"

class UI {
private:
	Service service;

public:
	UI(Service service) :service{ service } {};
	void printMenu();
	void runMenu();

	void add();
	void showAll();
	void saveFile();
	void showEfficient();
};
