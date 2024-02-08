#pragma once
#include "Service.h"

class UI {
private:
	Service service;

public:
	UI(Service service) : service { service }{};
	void print_start_menu();
	void print_admin_menu();
	void print_user_menu();

	void start_menu();
	/*void admin_menu();
	void user_menu();*/

	void add_admin_ui();
	void remove_admin_ui();
	void update_admin_ui();
	void display_admin_ui();

	void see_user_ui();
	void remove_user_ui();
	void display_user_ui();
	
};