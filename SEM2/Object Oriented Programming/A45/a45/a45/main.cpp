#include <iostream>


/*
So many movies, so little time. To make sure you do not miss any good movies, you absolutely need a software 
application to help you manage your films and create watch lists. The application can be used in two modes: 
administrator and user. When the application is started, it will offer the option to choose the mode.
Administrator mode: The application will have a database which holds all the movies. You must be able to 
update_admin the database, meaning: add_admin a new movie, delete a movie and update_admin the information of a 
movie. Each Movie has a title, a genre, a year of release, a number of likes and a trailer. The trailer is memorised as a link 
towards an online resource. The administrators will also have the option to see all the movies in the database.
User mode: A user can create a watch list with the movies that she wants to watch. The application will allow the user to:

See the movies in the database having a given genre (if the genre is empty, see all the movies), one by one. When the user 
chooses this option, the data of the first movie (title, genre, year of release, number of likes) is displayed 
and the trailer is played in the browser.
If the user likes the trailer, she can choose to add_admin the movie to her watch list.
If the trailer is not satisfactory, the user can choose not to add_admin the movie to the watch list and to 
continue to the next. In this case, the information corresponding to the next movie is shown and the user is 
again offered the possibility to add_admin it to the watch list. This can continue as long as the user wants, as when 
arriving to the end of the list of movies with the given genre, if the user chooses next, the application will again show the first movie.
Delete a movie from the watch list, after the user watched the movie. When deleting a movie from the watch list, 
the user can also rate the movie (with a like), and in this case, the number of likes the movie has in the repository
will be increased.
See the watch list.
*/
#include "Movie.h"
#include <vector>
#include "AdministratorRepository.h"
#include "Service.h"
#include "UI.h"
#include "Tests.h"
#include <crtdbg.h>


int main()
{
	{
		Tests tests;
		tests.run_tests();

		AdministratorRepository adminRepo = AdministratorRepository();
		UserRepository userRepo = UserRepository();
		Service service = Service{ adminRepo , userRepo };
		UI ui = UI{ service };
		ui.start_menu();
	}

	/*std::vector<Movie> movies;
	Movie movie1 = Movie("movie1","action",2023,1400000,"https://www.youtube.com/watch?v=2m1drlOZSDw");
	Movie movie2 = Movie("movie2", "action", 2023, 1200000, "https://www.youtube.com/watch?v=MeF5QeZ_LLI");
	Movie movie3 = Movie("movie3", "drama", 2020, 100000, "https://www.youtube.com/watch?v=2m1drlOZSDw");
	Movie movie4 = Movie("movie4", "thriller", 2010, 3500000, "https://www.youtube.com/watch?v=2m1drlOZSDw");
	Movie movie5 = Movie("movie5", "action", 2001, 105000, "https://www.youtube.com/watch?v=2m1drlOZSDw");

	service.add_admin_service(movie1);
	service.add_admin_service(movie5);
	service.add_admin_service(movie2);
	service.update_admin_service(movie1,movie4);
	service.add_admin_service(movie3);
	for(int i=0;i< service.get_service_size();i++)
		std::cout<<service.get_adminRepo().get_movies().get_element(i).representation();*/
	_CrtDumpMemoryLeaks();
	return 0;
}