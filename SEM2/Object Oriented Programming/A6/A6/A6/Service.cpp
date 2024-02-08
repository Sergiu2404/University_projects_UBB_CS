#include "Service.h"

bool Service::add_admin_service(Movie movie)
{
	return this->adminRepo.add_adminRepo(movie);
}

bool Service::add_user_service(Movie movie)
{
    return this->userRepo.add_userRepo(movie);
}

bool Service::remove_user_service(Movie movie)
{
    return this->userRepo.remove_userRepo(movie);
}

bool Service::remove_admin_service(Movie movie)
{
	return this->adminRepo.remove_adminRepo(movie);
}

bool Service::update_admin_service(Movie movie,Movie newMovie)
{
	return this->adminRepo.update_adminRepo(movie, newMovie);
}

std::vector<Movie> Service::get_adminRepo()
{
	return this->adminRepo.get_adminRepo();
}

std::vector<Movie> Service::get_userRepo()
{
    return this->userRepo.get_userRepo();
}

AdministratorRepository Service::get_admin_repo()
{
    return this->adminRepo;
}

UserRepository Service::get_user_repo()
{
    return this->userRepo;
}

void Service::init_service()
{
    Movie movie1 = Movie("Mission impossible 7", "action", 2023, 1400000, "https://www.youtube.com/watch?v=2m1drlOZSDw");
    Movie movie2 = Movie("Top Gun 2", "action", 2022, 3500000, "https://www.youtube.com/watch?v=giXco2jaZ_4");
    Movie movie3 = Movie("Hacksaw Ridge", "thriller", 2016, 105000, "https://www.youtube.com/watch?v=RdjO0p4GJPA");
    Movie movie4 = Movie("Fast 10", "action", 2023, 1200000, "https://www.youtube.com/watch?v=MeF5QeZ_LLI");
    Movie movie5 = Movie("John Wick 4", "action", 2023, 100000, "https://www.youtube.com/watch?v=yjRHZEUamCc");
    Movie movie6 = Movie("Dunkirk", "thriller", 2017, 120000, "https://www.youtube.com/watch?v=F-eMt3SrfFU");
    Movie movie7 = Movie("The dictator", "comedy", 2012, 90000, "https://www.youtube.com/watch?v=cYplvwBvGA4");

    this->adminRepo.add_adminRepo(movie1);
    this->adminRepo.add_adminRepo(movie2);
    this->adminRepo.add_adminRepo(movie3);
    this->adminRepo.add_adminRepo(movie4);
    this->adminRepo.add_adminRepo(movie5);
    this->adminRepo.add_adminRepo(movie6);
    this->adminRepo.add_adminRepo(movie7);
}
