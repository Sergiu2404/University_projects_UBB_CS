#pragma once
#include <iostream>
#include <vector>

#include "Movie.h"

class AdministratorRepository {
private:
	std::vector<Movie> adminRepo;

public:
	bool add_adminRepo(Movie movie);
	bool remove_adminRepo(Movie movie);
	bool update_adminRepo(Movie movie, Movie newMovie);

	int find_position_adminRepo(Movie movie);

	std::vector<Movie> get_adminRepo();
};