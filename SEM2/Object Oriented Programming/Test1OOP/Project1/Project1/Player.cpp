#include "Player.h"
#include <sstream>

string Player::get_name()
{
	return this->name;
}

string Player::get_nationality()
{
	return this->nationality;
}

string Player::get_team()
{
	return this->team;
}

int Player::get_goals()
{
	return this->goals;
}

string Player::representation()
{
	ostringstream output;
	output << this->name << " | " << this->nationality << " | " << this->team << " | " << this->goals << "\n";
	return output.str();
}
