#pragma once
#include "Movie.h"
#include <vector>

class UserRepository
{
private:
	std::vector<Movie> watchList;

public:
	UserRepository() {
		//this->watchList = read_from_file();
	}

	virtual std::vector<Movie> read_from_file();
	virtual void write_to_file();
	virtual void display() const = 0;

	bool add_userRepo(Movie movie);
	bool remove_userRepo(Movie movie);

	int find_position_userRepo(Movie movie);
	std::vector<Movie> get_watchList() { return this->watchList; }

};

