#include "Test.h"
#include "Service.h"

#include <cassert>

void Test::run_tests()
{
	test_add();
	test_remove();
	test_update();
	test_representation();
	//test_userRepo();
}

void Test::test_add()
{
	Service service;

	service.add_admin_service(Movie("1", "1", 1, 1, "..."));
	service.get_admin_repo().add_adminRepo(Movie("2", "2", 2, 2, "..."));
	assert(service.get_adminRepo().size() == 1);

	assert(service.add_admin_service(Movie("1", "1", 1, 1, "...")) == false);
	assert(service.add_admin_service(Movie("3", "3", 3, 3, "...")) == true);


	service.add_user_service(Movie("1", "1", 1, 1, "..."));
	service.get_user_repo().add_userRepo(Movie("2", "2", 2, 2, "..."));
	assert(service.get_userRepo().size() == 1);

	assert(service.add_user_service(Movie("1", "1", 1, 1, "...")) == false);
	assert(service.add_user_service(Movie("3", "3", 3, 3, "...")) == true);

	std::cout << "Add tested\n";
}

void Test::test_remove()
{
	Service service;

	service.add_admin_service(Movie("1", "1", 1, 1, "..."));
	service.add_admin_service(Movie("2", "2", 2, 2, "..."));
	service.add_admin_service(Movie("3", "3", 3, 3, "..."));

	service.remove_admin_service(Movie("1", "1", 1, 1, "..."));
	service.get_admin_repo().remove_adminRepo(Movie("2", "2", 2, 2, "..."));

	assert(service.get_adminRepo().size() == 2);

	assert(service.remove_admin_service(Movie("3", "3", 3, 3, "..."))==true);
	assert(service.remove_admin_service(Movie("4", "4", 4, 4, "...")) == false);


	service.add_user_service(Movie("1", "1", 1, 1, "..."));
	service.add_user_service(Movie("2", "2", 2, 2, "..."));
	service.add_user_service(Movie("3", "3", 3, 3, "..."));

	service.remove_user_service(Movie("1", "1", 1, 1, "..."));
	service.get_user_repo().remove_userRepo(Movie("2", "2", 2, 2, "..."));

	assert(service.get_userRepo().size() == 2);

	assert(service.remove_user_service(Movie("3", "3", 3, 3, "...")) == true);
	assert(service.remove_user_service(Movie("4", "4", 4, 4, "...")) == false);

	std::cout << "Remove tested\n";
}

void Test::test_update()
{
	Service service;

	service.add_admin_service(Movie("1", "1", 1, 1, "..."));
	service.add_admin_service(Movie("2", "2", 2, 2, "..."));
	service.add_admin_service(Movie("3", "3", 3, 3, "..."));

	service.update_admin_service(Movie("1", "1", 1, 1, "..."),Movie("100","100",100,100,"..."));

	assert(service.get_adminRepo().size() == 3);

	assert(service.get_adminRepo()[0].get_title() == "100");
	assert(service.get_adminRepo()[0].get_genre() == "100");
	assert(service.get_adminRepo()[0].get_likesNumber() == 100);
	assert(service.get_adminRepo()[0].get_releaseYear() == 100);
	assert(service.get_adminRepo()[0].get_trailer() == "...");

	assert(service.update_admin_service(Movie("3", "3", 3, 3, "..."),Movie("300","300",300,300,"...")) == true);
	assert(service.update_admin_service(Movie("4", "4", 4, 4, "..."),Movie("400","400",400,400,"...")) == false);

	std::cout << "Update tested\n";
}

void Test::test_representation()
{
	Movie movie = Movie("Top Gun 2", "action", 2022, 200000, "...");

	assert(movie.representation() == "Title: Top Gun 2 |Genre: action |Year: 2022 |Likes: 200000 |Trailer: ...");

	std::cout << "Movie representation tested\n";
}

//void Test::test_userRepo()
//{
//}
