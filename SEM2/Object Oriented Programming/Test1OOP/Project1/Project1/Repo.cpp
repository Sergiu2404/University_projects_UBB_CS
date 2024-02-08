#include "Repo.h"

bool Repo::add(Player player)
{
	if (this->find_position(player) != -1)
		return false;
	this->players.push_back(player);
	return true;
}

int Repo::find_position(Player player)
{
	for (int i = 0; i < this->get_players().size(); i++)
		if (this->get_players()[i].get_name() == player.get_name())
			return i;
	return -1;
}

vector<Player> Repo::get_players()
{
	return this->players;
}
