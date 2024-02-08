#include "UI.h"
#include <string>
void UI::print_menu()
{
	cout << "1-add\n"
		"2-display\n"
		"3-display team and goals\n";
}

void UI::run_menu()
{
	this->service.init_service();

	while (true)
	{
		this->print_menu();

		cout << "option: ";
		int option;
		cin >> option;

#define ADD 1
#define DISPLAY 2
#define DISPLAY_TEAM 3

		if (option == ADD)
			this->add();
		else if (option == DISPLAY)
			this->display();
		else if (option == DISPLAY_TEAM)
			this->display_team();
		else
			cout << "Invalid option!\n";

	}
}

void UI::add()
{
	string name, nationality, team;
	int goals;

	cout << "name: ";
	cin.ignore();
	getline(cin, name);

	cout << "nationality: ";
	getline(cin, nationality);

	cout << "team: ";
	getline(cin, team);

	cout << "goals: ";
	cin >> goals;

	Player player = Player(name, team, nationality, goals);

	if (this->service.add(player) == false)
		cout << "already exists\n";
	else
	{
		this->service.add(player);
		cout << "added\n";
	}
}

void UI::display()
{
	for (int i = 0; i < this->service.get_players().size(); i++)
		cout << this->service.get_players()[i].representation();
}

void UI::display_team()
{
	string team="";
	cout << "team: ";
	cin.ignore();
	getline(cin, team);

	vector<Player> players;
	if (team == "" || team == " ")
		this->display();
	else {
		int goals = 0;
		players=this->service.get_players_team(team);
		goals = this->service.get_total_goals_team(team);
		for (int i = 0; i < players.size(); i++)
			cout << players[i].representation();
		cout << "total number of goals: " << goals;
		if (players.size() == 0)
			cout << "Team does not exist\n";
		cout << "\n";
	}

}
