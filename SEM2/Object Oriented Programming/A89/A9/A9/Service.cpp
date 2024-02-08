#include "Service.h"
#include <fstream>
#include <vector>

bool Service::add_admin_service(Movie movie)
{
    return this->adminRepo->add_adminRepo(movie);
}

bool Service::add_user_service(Movie movie)
{
    return this->userRepo->add_userRepo(movie);
}

bool Service::remove_user_service(Movie movie)
{
    return this->userRepo->remove_userRepo(movie);
}

bool Service::remove_admin_service(Movie movie)
{
    return this->adminRepo->remove_adminRepo(movie);
}

bool Service::update_admin_service(Movie movie, Movie newMovie)
{
    return this->adminRepo->update_adminRepo(movie, newMovie);
}




//std::vector<Movie> Service::read_file()
//{
//    std::vector<Movie> adminMovies = this->adminRepo.read_from_file();
//    return adminMovies;
//}




std::vector<Movie> Service::get_adminRepo()
{
    return this->adminRepo->get_movies();
}

std::vector<Movie> Service::get_userRepo()
{
    return this->userRepo->get_watchList();
}

void Service::init_service()
{
}
