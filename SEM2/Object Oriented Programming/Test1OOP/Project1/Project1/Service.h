#pragma once
#include "Repo.h"

class Service {
private:
	Repo repo;
public:
	Service(Repo repo) :repo{ repo } {};
	Service() {};

	bool add(Player player);

	void init_service();

	vector<Player> get_players_team(string team);
	vector<Player> get_players_team_test(string team,vector<Player> players);

	int get_total_goals_team(string team);

	vector<Player> get_players();
};