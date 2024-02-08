#include "Service.h"

bool Service::add_admin_service(Movie movie)
{
    return this->adminRepo.add_admin(movie);
    /*if (this->adminRepo.add_admin(movie) == false)
        return false;

    this->adminRepo.add_admin(movie);
    return true;*/
}

bool Service::remove_admin_service(Movie movie)
{
    return this->adminRepo.remove_admin(movie);
}

bool Service::update_admin_service(Movie movie, Movie newMovie)
{
    return this->adminRepo.update_admin(movie, newMovie);
}

bool Service::add_user_service(Movie movie)
{
    return this->userRepo.add_userRepo(movie);
}

bool Service::remove_user_service(Movie movie)
{
    return this->userRepo.remove_userRepo(movie);
}

UserRepository Service::get_userRepo()
{
    return this->userRepo;
}

int Service::get_user_service_size()
{
    return this->userRepo.get_watchList_size();
}

AdministratorRepository Service::get_adminRepo()
{
    return this->adminRepo;
}

int Service::get_service_size()
{
    return this->adminRepo.get_movies_size();
}

void Service::initService()
{
    Movie movie1 = Movie("Mission impossible 7", "action", 2023, 1400000, "https://www.youtube.com/watch?v=2m1drlOZSDw");
    Movie movie2 = Movie("Top Gun 2", "action", 2022, 3500000, "https://www.youtube.com/watch?v=giXco2jaZ_4");
    Movie movie3 = Movie("Hacksaw Ridge", "thriller", 2016, 105000, "https://www.youtube.com/watch?v=RdjO0p4GJPA");
    Movie movie4 = Movie("Fast 10", "action", 2023, 1200000, "https://www.youtube.com/watch?v=MeF5QeZ_LLI");
    Movie movie5 = Movie("John Wick 4", "action", 2023, 100000, "https://www.youtube.com/watch?v=yjRHZEUamCc");
    Movie movie6 = Movie("Dunkirk", "thriller", 2017, 120000, "https://www.youtube.com/watch?v=F-eMt3SrfFU");
    Movie movie7 = Movie("The dictator", "comedy", 2012, 90000, "https://www.youtube.com/watch?v=cYplvwBvGA4");

    this->adminRepo.add_admin(movie1);
    this->adminRepo.add_admin(movie2);
    this->adminRepo.add_admin(movie3);
    this->adminRepo.add_admin(movie4);
    this->adminRepo.add_admin(movie5);
    this->adminRepo.add_admin(movie6);
    this->adminRepo.add_admin(movie7);
}
