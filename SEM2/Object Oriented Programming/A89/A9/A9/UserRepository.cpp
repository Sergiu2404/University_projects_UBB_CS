#include "UserRepository.h"
#include "Exceptions.h"
#include <string>
#include <sstream>
#include <fstream>


std::vector<std::string> tokenise_userRepo(std::string& string, char delimiter)
{
	std::vector<std::string> result;
	std::stringstream ss(string);
	std::string(token);

	while (getline(ss, token, delimiter))
		result.push_back(token);

	return result;
}

std::vector<Movie> UserRepository::read_from_file()
{
	std::ifstream file("movies.txt");

	if (!file.is_open())
		throw FileException("[File Exception] reading user repo");

	std::vector<Movie> newWatchlist;
	std::string movieString;
	Movie movie;

	while (getline(file, movieString))
	{
		std::vector<std::string> tokens = tokenise_userRepo(movieString, ',');

		Movie movie{ tokens[0], tokens[1], stoi(tokens[2]), stoi(tokens[3]), tokens[4] };

		newWatchlist.push_back(movie);
	}

	file.close();
	return newWatchlist;
}

void UserRepository::write_to_file()
{
	std::ofstream file("C:\\Users\\Sergiu\\Desktop\\OOP\\A7\\A7 - 8\\A7 - 8\\movies.txt");

	if (!file.is_open())
		throw FileException("[File Exception] writing user repo\n");

	for (auto movie : this->watchList)
		file << movie;

	file.close();
}
bool UserRepository::add_userRepo(Movie movie)
{
	if (this->find_position_userRepo(movie) != -1)
		return false;

	this->watchList.push_back(movie);

	write_to_file();
	return true;
}

bool UserRepository::remove_userRepo(Movie movie)
{
	if (this->find_position_userRepo(movie) == -1)
		return false;

	this->watchList.erase(this->watchList.begin() + this->find_position_userRepo(movie));

	write_to_file();
	return true;
}

int UserRepository::find_position_userRepo(Movie movie)
{
	for (int i = 0; i < this->watchList.size(); i++)
		if (this->watchList[i].get_title() == movie.get_title() && this->watchList[i].get_genre() == movie.get_genre())
			return i;

	return -1;
}
