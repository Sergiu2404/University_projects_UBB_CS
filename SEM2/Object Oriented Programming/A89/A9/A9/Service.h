#pragma once
#include "AdministratorRepository.h"
#include "UserRepository.h"


class Service
{
private:
	AdministratorRepository* adminRepo;
	UserRepository* userRepo;
public:
	Service( UserRepository* userRepo, AdministratorRepository* adminRepo) :adminRepo{ adminRepo }, userRepo{ userRepo }
	{}
	Service() {
		//this->read_file();
	}

	bool add_admin_service(Movie movie);
	bool remove_admin_service(Movie movie);
	bool update_admin_service(Movie movie, Movie newMovie);

	//std::vector<Movie> read_file();

	bool add_user_service(Movie movie);
	bool remove_user_service(Movie movie);

	std::vector<Movie> get_adminRepo();
	std::vector<Movie> get_userRepo();

	void init_service();
};

