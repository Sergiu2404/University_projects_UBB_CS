#pragma once
#include "AdministratorRepository.h"
#include "UserRepository.h"


class Service {
private:
	AdministratorRepository adminRepo;
	UserRepository userRepo;

public:
	Service(AdministratorRepository adminRepo, UserRepository userRepo) : adminRepo{ adminRepo }, userRepo{ userRepo } {};
	Service() {};

	bool add_admin_service(Movie movie);
	bool remove_admin_service(Movie movie);
	bool update_admin_service(Movie movie, Movie newMovie);

	bool add_user_service(Movie movie);
	bool remove_user_service(Movie movie);

	UserRepository get_userRepo();
	int get_user_service_size();

	AdministratorRepository get_adminRepo();
	int get_service_size();

	void initService();
};