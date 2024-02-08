#include "AdministratorRepository.h"

bool AdministratorRepository::add_adminRepo(Movie movie)
{
	if (this->find_position_adminRepo(movie) != -1)
		return false;
	this->adminRepo.push_back(movie);
	return true;
}

bool AdministratorRepository::remove_adminRepo(Movie movie)
{
	if (this->find_position_adminRepo(movie) == -1)
		return false;

	this->adminRepo.erase(this->adminRepo.begin()+this->find_position_adminRepo(movie));
	return true;
}

bool AdministratorRepository::update_adminRepo(Movie movie,Movie newMovie)
{
	if (this->find_position_adminRepo(movie) == -1)
		return false;
	
	this->adminRepo[this->find_position_adminRepo(movie)] = newMovie;
	/*this->genre = newMovie.genre;
	this->releaseYear = newMovie.releaseYear;
	this->likesNumber = newMovie.likesNumber;
	this->trailer = newMovie.trailer;*/
	return true;
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
