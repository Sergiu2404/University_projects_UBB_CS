#include "Tests.h"
#include "Service.h"
#include "AdministratorRepository.h"
#include "UserRepository.h"
#include "DynamicArray.h"
#include "Movie.h"

#include <iostream>
#include <cassert>



void Tests::run_tests()
{
	test_add();
	test_remove();
	test_update();

	test_representation();
	test_dynamicArray();
	test_userRepo();
	std::cout << "Tests ended\n";
}




void Tests::test_add()
{
	Service service;

	service.add_admin_service(Movie("1", ".", 0, 0, "..."));
	service.add_user_service(Movie("2", ".", 0, 0, "..."));

	assert(service.add_user_service(Movie("2", ".", 0, 0, "...")) == false);

	assert(service.get_adminRepo().find_movie_position(Movie("1", ".", 0, 0, "..."))==0);
	assert(service.get_userRepo().find_movie_position(Movie("2", ".", 0, 0, "...")) == 0);
	assert(service.get_service_size() == 1);
	assert(service.get_user_service_size() == 1);
	assert(service.get_userRepo().get_watchList_size() == 1);
	assert(service.get_adminRepo().get_movies_size() == 1);

	bool result = service.add_admin_service(Movie("1", ".", 0, 0, "..."));
	assert(result==false);
	std::cout << "Add tested\n";
}

void Tests::test_remove()
{
	Service service;

	service.add_admin_service(Movie("1", ".", 0, 0, "..."));
	service.add_admin_service(Movie("2", "..", 0, 0, "..."));
	service.add_user_service(Movie("3", ".", 0, 0, "..."));
	service.add_user_service(Movie("4", "..", 0, 0, "..."));

	assert(service.remove_user_service(Movie("5", ".", 0, 0, "...")) == false);
	assert(service.remove_admin_service(Movie("5", ".", 0, 0, "...")) == false);

	service.remove_admin_service(Movie("1", ".", 0, 0, "..."));
	service.remove_user_service(Movie("3", ".", 0, 0, "..."));

	assert(service.get_service_size() == 1);
	assert(service.get_adminRepo().get_movies_size() == 1);
	assert(service.get_user_service_size() == 1);
	assert(service.get_userRepo().get_watchList_size() == 1);

	bool result = service.remove_admin_service(Movie("2", "..", 0, 0, "..."));
	bool result2 = service.remove_user_service(Movie("4", "..", 0, 0, "..."));

	assert(result == true);
	assert(result == true);
	std::cout << "Remove tested\n";
}

void Tests::test_update()
{
	Service service;

	service.add_admin_service(Movie("1", ".", 0, 0, "..."));
	service.add_admin_service(Movie("2", "..", 0, 0, "..."));
	service.add_user_service(Movie("3", ".", 0, 0, "..."));
	service.add_user_service(Movie("4", "..", 0, 0, "..."));

	service.update_admin_service(Movie("1", ".", 0, 0, "..."), Movie("11", "..", 1, 1, "abcd"));

	assert(service.get_adminRepo().get_movies().get_element(0).get_title() == "11");
	assert(service.get_adminRepo().get_movies().get_element(0).get_genre() == "..");
	assert(service.get_adminRepo().get_movies().get_element(0).get_likesNumber() == 1);
	assert(service.get_adminRepo().get_movies().get_element(0).get_releaseYear() == 1);

	bool result= service.update_admin_service(Movie("2", "..", 0, 0, "..."), Movie("12", "...", 1, 1, "abcd"));
	assert(result == true);
	bool result2 = service.update_admin_service(Movie("2", "..", 0, 0, "..."), Movie("12", "...", 1, 1, "abcd"));
	assert(result2 == false);

	std::cout << "Update tested";
}

void Tests::test_representation()
{
	Movie movie = Movie("title", "genre", 0, 0, "...");
	assert(movie.representation() == "Title: title |Genre: genre |Year: 0 |Likes: 0 |Trailer: ...\n");
	std::cout << "Movie representation tested\n";
}

void Tests::test_dynamicArray()
{
	DynamicArray<Movie> dynamicArray = DynamicArray<Movie>(2);

	dynamicArray.add(Movie("1", ".", 0, 0, "..."));
	dynamicArray.add(Movie("2", ".", 1, 1, "..."));
	dynamicArray.add(Movie("3", ".", 2, 2, "..."));

	dynamicArray.remove(0);

	assert(dynamicArray.get_size() == 2);

	dynamicArray.update(0, Movie("4", "..", 100, 100, "A"));

	assert(dynamicArray.get_element(0).get_title() == "4");

	std::cout << "Dynamic array tested\n";
}

void Tests::test_userRepo()
{
	UserRepository userRepo;
	DynamicArray<Movie> dynamicArray;

	userRepo.add_userRepo(Movie("1", ".", 0, 0, "..."));
	userRepo.add_userRepo(Movie("2", ".", 0, 0, "..."));

	assert(userRepo.get_watchList().get_size() == 2);
	assert(userRepo.get_watchList().get_element(0).get_title() == "1");

	userRepo.add_userRepo(Movie("3", ".", 0, 0, "..."));
	dynamicArray.add(Movie("4", ".", 0, 0, "..."));

	assert(userRepo.get_watchList_size() == 3);

	userRepo.remove_userRepo(Movie("1", ".", 0, 0, "..."));

	assert(userRepo.get_watchList_size() == 2);
	assert(userRepo.find_movie_position(Movie("3", ".", 0, 0, "...")) == 1);

	std::cout << "User repository tested\n";
}


