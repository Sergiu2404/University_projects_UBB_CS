#include "AdministratorRepository.h"
#include <fstream>
#include "Exceptions.h"
#include <sstream>
#include "Movie.h"

std::vector<std::string> tokenise(std::string& string, char delimiter)
{
	std::vector<std::string> result;
	std::stringstream ss(string);
	std::string(token);

	while (getline(ss, token, delimiter))
		result.push_back(token);

	return result;
}

bool AdministratorRepository::add_adminRepo(Movie movie)
{
	//this->adminRepo = read_from_file();

	if (this->find_position_adminRepo(movie) != -1)
		return false;

	this->adminRepo.push_back(movie);
	return true;
}

bool AdministratorRepository::remove_adminRepo(Movie movie)
{
	//this->adminRepo = read_from_file();

	if (this->find_position_adminRepo(movie) == -1)
		return false;

	this->adminRepo.erase(this->adminRepo.begin() + this->find_position_adminRepo(movie));
	return true;
}

bool AdministratorRepository::update_adminRepo(Movie movie, Movie newMovie)
{
	//this->adminRepo = read_from_file();

	if (this->find_position_adminRepo(movie) == -1)
		return false;

	this->adminRepo[this->find_position_adminRepo(movie)] = newMovie;
	/*this->genre = newMovie.genre;
	this->releaseYear = newMovie.releaseYear;
	this->likesNumber = newMovie.likesNumber;
	this->trailer = newMovie.trailer;*/
	return true;
}

std::vector<Movie> AdministratorRepository::read_from_file()
{
	std::ifstream file(FILE_NAME);

	if (!file.is_open())
		throw FileException("[File Exception] reading admin repository\n");

	std::vector<Movie> movies;
	Movie movie;
	std::string movieString;

	while (getline(file, movieString))
	{
		//	movies.push_back(movie);
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

void AdministratorRepository::write_to_file()
{
	std::ofstream file(this->get_fileName());

	if (!file.is_open())
		throw FileException("[File Exception] writing admin repo\n");

	for (auto movie : this->adminRepo)
		file << movie;

	file.close();
}

std::string AdministratorRepository::get_fileName()
{
	return this->fileName;
}

int AdministratorRepository::find_position_adminRepo(Movie movie)
{
	for (int i = 0; i < this->adminRepo.size(); i++)
		if (this->adminRepo[i].get_title() == movie.get_title() && this->adminRepo[i].get_genre() == movie.get_genre())
			return i;
	return -1;
}

std::vector<Movie> AdministratorRepository::get_adminRepo()
{
	return this->adminRepo;
}
