#include "UserRepository.h"

bool UserRepository::add_userRepo(Movie movie)
{
	if (this->find_movie_position(movie) != -1)
		return false;
	this->watchList.add(movie);
	return true;
}

bool UserRepository::remove_userRepo(Movie movie)
{
	int position = this->find_movie_position(movie);
	if (position == -1)
		return false;
	this->watchList.remove(position);
	return true;
}

DynamicArray<Movie> UserRepository::get_watchList()
{
	return this->watchList;
}

int UserRepository::get_watchList_size()
{
	return this->watchList.get_size();
}

int UserRepository::find_movie_position(Movie movie)
{
	for (int i = 0; i < this->get_watchList_size(); i++)
		if (this->watchList.get_element(i).get_genre() == movie.get_genre() && this->watchList.get_element(i).get_title() == movie.get_title())
			return i;
	return -1;
}
