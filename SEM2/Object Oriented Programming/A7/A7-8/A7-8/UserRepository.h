#pragma once
#include <iostream>
#include <vector>
#include "Movie.h"

#include <fstream>
#include <algorithm>

class UserRepository {
protected:
	std::vector<Movie> userRepo;
	std::string fileName;
public:
	UserRepository(std::string fileName) :fileName{ fileName }
	{
		this->userRepo = read_from_file();
	}

	UserRepository() {}

	virtual std::vector<Movie> read_from_file();
	virtual void write_to_file();
	virtual void display() const = 0;

	bool add_userRepo(Movie movie);
	bool remove_userRepo(Movie movie);
	std::string get_fileName() const;

	int find_position_userRepo(Movie movie);
	std::vector<Movie> get_userRepo();
};