#include "AdministratorRepository.h"
#include <fstream>
#include <string>
#include <sstream>

std::vector<std::string> tokenise(std::string& string, char delimiter)
{
	std::vector<std::string> result;
	std::stringstream ss(string);
	std::string(token);

	while (getline(ss, token, delimiter))
		result.push_back(token);

	return result;
}


std::vector<Movie> AdministratorRepository::read_from_file()
{
	std::ifstream file("movies.txt");

	std::vector<Movie> movies;
	Movie movie;
	std::string movieString;

	while (getline(file, movieString))
	{
	
		std::vector<std::string> tokens = tokenise(movieString, ',');

		Movie movie{ tokens[0], tokens[1], stoi(tokens[2]), stoi(tokens[3]), tokens[4] };

		movies.push_back(movie);
	}

	file.close();

	/*for (int i = 0; i < movies.size(); i++)
	{
		std::cout << movies[i].get_title() << std::endl;
	}*/

	return movies;
}

bool AdministratorRepository::add_adminRepo(Movie movie)
{

	if (this->find_position_adminRepo(movie) != -1)
		return false;

	this->movies.push_back(movie);
	return true;
}

bool AdministratorRepository::remove_adminRepo(Movie movie)
{

	if (this->find_position_adminRepo(movie) == -1)
		return false;

	this->movies.erase(this->movies.begin() + this->find_position_adminRepo(movie));
	return true;
}

bool AdministratorRepository::update_adminRepo(Movie movie, Movie newMovie)
{

	if (this->find_position_adminRepo(movie) == -1)
		return false;

	this->movies[this->find_position_adminRepo(movie)] = newMovie;
	return true;
}

void AdministratorRepository::write_to_file()
{
    std::ofstream file("movies.txt");

    for (auto movie : this->movies)
        file << movie;

    file.close();
}

int AdministratorRepository::find_position_adminRepo(Movie movie)
{
	for (int i = 0; i < this->movies.size(); i++)
		if (this->movies[i].get_title() == movie.get_title() && this->movies[i].get_genre() == movie.get_genre())
			return i;
	return -1;
}

std::vector<Movie> AdministratorRepository::get_movies()
{
    return this->movies;
}
