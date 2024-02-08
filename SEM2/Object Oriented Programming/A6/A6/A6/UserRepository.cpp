#include "UserRepository.h"

bool UserRepository::add_userRepo(Movie movie)
{
	if (this->find_position_userRepo(movie) != -1)
		return false;

	this->userRepo.push_back(movie);
	return true;
}

bool UserRepository::remove_userRepo(Movie movie)
{
	if (this->find_position_userRepo(movie) == -1)
		return false;

	this->userRepo.erase(this->userRepo.begin() + this->find_position_userRepo(movie));
	return true;
}

int UserRepository::find_position_userRepo(Movie movie)
{
	for (int i = 0; i < this->userRepo.size(); i++)
		if (this->userRepo[i].get_title() == movie.get_title() && this->userRepo[i].get_genre() == movie.get_genre())
			return i;

	return -1;
}

std::vector<Movie> UserRepository::get_userRepo()
{
	return this->userRepo;
}
