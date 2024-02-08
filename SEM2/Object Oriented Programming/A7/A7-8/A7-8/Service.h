#pragma once
#include <iostream>

#include "Movie.h"
#include "AdministratorRepository.h"
#include "UserRepository.h"

class Service {
private:
	AdministratorRepository adminRepo;
	UserRepository *userRepo;
public:
	Service(std::string fileName, UserRepository* userRepo, AdministratorRepository adminRepo) :adminRepo{ adminRepo}, userRepo{ userRepo }
	{
		std::cout << fileName;
	}
	Service() {}

	bool add_admin_service(Movie movie);
	bool remove_admin_service(Movie movie);
	bool update_admin_service(Movie movie, Movie newMovie);

	bool add_user_service(Movie movie);
	bool remove_user_service(Movie movie);

	std::vector<Movie> get_adminRepo();
	std::vector<Movie> get_userRepo();

	AdministratorRepository get_admin_repo();
	UserRepository* get_user_repo();
	void init_service();
};