#pragma once
#include "Service.h"
class UI {
private:
	Service service;

public:
	UI(Service service) :service{ service } {};

	void print_menu();
	void run_menu();

	void add();
	void display();

	void display_team();
};