#pragma once
#include <iostream>
#include <fstream>
#include <vector>
#include <string>

#include "Movie.h"

#define FILE_NAME "C:\\Users\\Sergiu\\Desktop\\OOP\\A7\\A7-8\\A7-8\\movies.txt"

class AdministratorRepository {
private:
	std::vector<Movie> adminRepo;
	std::string fileName;

public:
	AdministratorRepository(std::string fileName) : fileName{ fileName }
	{
		this->adminRepo = this->read_from_file();
	}

	AdministratorRepository() {}

	bool add_adminRepo(Movie movie);
	bool remove_adminRepo(Movie movie);
	bool update_adminRepo(Movie movie, Movie newMovie);

	std::vector<Movie> read_from_file();
	void write_to_file();

	std::string get_fileName();

	int find_position_adminRepo(Movie movie);

	std::vector<Movie> get_adminRepo();
};