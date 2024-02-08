#pragma once
#include <iostream>
#include <vector>
#include "Movie.h"

class UserRepository {
private:
	std::vector<Movie> userRepo;
public:

	bool add_userRepo(Movie movie);
	bool remove_userRepo(Movie movie);

	int find_position_userRepo(Movie movie);
	std::vector<Movie> get_userRepo();
};