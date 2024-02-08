#pragma once
#include <vector>
#include "Player.h"
class Repo {
private:
	vector<Player> players;
public:
	bool add(Player player);

	int find_position(Player player);

	vector<Player> get_players();

};