#include "Service.h"
#include <algorithm>

bool Service::add(Player player)
{
	return this->repo.add(player);
}

void Service::init_service()
{
	Player player1 = Player("Neagu", "ROU", "CSM", 100);
	Player player2 = Player("Gulden", "NOR", "SWE", 80);
	Player player3 = Player("Ilina", "FRA", "CSM", 10);
	Player player4 = Player("Allison", "RUS", "CSM", 70);
	Player player5 = Player("Nerea", "ESP", "CSM", 55); 

	this->repo.add(player1);
	this->repo.add(player2);
	this->repo.add(player3);
	this->repo.add(player4);
	this->repo.add(player5);

}
bool compare_goals(Player& player1, Player& player2)
{
	return player1.get_goals() < player2.get_goals();
}

vector<Player> Service::get_players_team(string team)
{
	vector<Player> players;
	for (int i = 0; i < this->get_players().size(); i++)
		if (this->get_players()[i].get_team() == team)
			players.push_back(this->get_players()[i]);

	sort(players.begin(), players.end(), compare_goals);

	return players;
}

vector<Player> Service::get_players_team_test(string team,vector<Player> players)
{
	vector<Player> sorted;
	for (int i = 0; i < players.size(); i++)
		if (players[i].get_team() == team)
			sorted.push_back(players[i]);

	sort(sorted.begin(), sorted.end(), compare_goals);

	return sorted;
}

int Service::get_total_goals_team(string team)
{
	int goals = 0;
	for (int i = 0; i < this->get_players().size(); i++)
		if (this->get_players()[i].get_team() == team)
			goals += this->get_players()[i].get_goals();

	return goals;
}

vector<Player> Service::get_players()
{
	return this->repo.get_players();
}
