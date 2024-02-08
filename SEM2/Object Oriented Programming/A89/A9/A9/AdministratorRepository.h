#pragma once
#include "Movie.h"
#include <vector>

class AdministratorRepository
{
private:
	std::vector<Movie> movies;

public:
	AdministratorRepository() {
		this->movies = read_from_file();
	}

	std::vector<Movie> read_from_file();

	bool add_adminRepo(Movie movie);
	bool remove_adminRepo(Movie movie);
	bool update_adminRepo(Movie movie, Movie newMovie);

	void write_to_file();

	int find_position_adminRepo(Movie movie);

	std::vector<Movie> get_movies();
};

