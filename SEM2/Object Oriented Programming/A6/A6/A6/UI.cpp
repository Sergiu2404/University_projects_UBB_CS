#include "UI.h"
#include "Movie.h"
#include <Windows.h>
#include <shellapi.h>

#include <iostream>
#include <string>
#include <cstring>
#include <algorithm>

void UI::print_main_menu()
{
	std::cout << "STARTING MENU==========\n";
	std::cout << "1-ADMIN/2-USER/3-EXIT\n";
	std::cout << "=======================\n";
}

void UI::print_admin_menu()
{
	std::cout << "ADMINISTRATOR MENU=========================\n";
	std::cout << "1-ADD/2-REMOVE/3-UPDATE/4-DISPLAY/5-EXIT\n";
	std::cout << "===========================================\n";
}

void UI::print_user_menu()
{
	std::cout << "USER MENU=======================================================================\n";
	std::cout << "1-SEE MOVIES BY GIVEN GENRE/2-REMOVE FROM WATCHLIST/3-DISPLAY WATCHLIST/4-EXIT\n";
	std::cout << "================================================================================\n";
}


void UI::run_menu()
{
	this->service.init_service();

	while (true)
	{
		this->print_main_menu();

		std::cout << "enter option: ";
		std::string option;
		getline(std::cin, option);

		#define ADMIN_ROLE "1"
		#define USER_ROLE "2"
		#define EXIT "3"

		if (option == ADMIN_ROLE)
		{

			while (true)
			{
			this->print_admin_menu();

			std::cout << "enter option: ";
			std::string option_admin;
			getline(std::cin, option_admin);

			#define ADD_ADMIN "1"
			#define REMOVE_ADMIN "2"
			#define UPDATE_ADMIN "3"
			#define DISPLAY_ADMIN "4"
			#define EXIT_ADMIN "5"

			if (option_admin == ADD_ADMIN)
				this->add_admin_ui();
			else if (option_admin == REMOVE_ADMIN)
				this->remove_admin_ui();
			else if (option_admin == UPDATE_ADMIN)
				this->update_admin_ui();
			else if (option_admin == DISPLAY_ADMIN)
				this->display_admin_ui();
			else if (option_admin == EXIT_ADMIN)
				break;
			else
				std::cout << "Invalid option";
			}

		}
		else if (option == USER_ROLE)
		{
			while (true)
			{
				this->print_user_menu();

				std::cout << "enter option: ";
				std::string option_user;
				getline(std::cin, option_user);

				#define SEE_USER "1"
				#define REMOVE_USER "2"
				#define DISPLAY_USER "3"
				#define EXIT_USER "4"

				if (option_user == SEE_USER)
					this->see_movies_ui();
				else if (option_user == REMOVE_USER)
					this->remove_user_ui();
				else if (option_user == DISPLAY_USER)
					this->display_watchlist_ui();
				else if (option_user == EXIT_USER)
					break;
				else
					std::cout << "Invalid option";
			}
		}
		else if (option == EXIT)
			break;
		else
			std::cout << "Invalid option, try again\n";
	}
}

void UI::add_admin_ui()
{
	std::string title, genre, trailer;
	int releaseYear, likesNumber;
	std::cout << "Title: ";
	getline(std::cin, title);
	std::cout << "Genre: ";
	getline(std::cin, genre);
	std::cout << "Year of release: ";
	std::cin >> releaseYear;
	std::cout << "Number of likes: ";
	std::cin >> likesNumber;
	std::cout << "Trailer: ";
	std::cin.ignore();
	getline(std::cin, trailer);

	Movie movie = Movie(title, genre, releaseYear, likesNumber, trailer);

	bool result = this->service.add_admin_service(movie);
	if (result == false)
		std::cout << "Movie has not been added, try again!\n";
	else
		std::cout << "Movie added succesfully\n";
}

void UI::remove_admin_ui()
{
	std::string title, genre;
	std::cout << "Title: ";
	getline(std::cin, title);
	std::cout << "Genre: ";
	getline(std::cin, genre);

	Movie movie = Movie(title,genre,0,0,"...");

	bool result = this->service.remove_admin_service(movie);

	if (result == false)
		std::cout << "Movie doesnt exist\n";
	else
		std::cout << "Movie removed succesfully\n";
}

void UI::update_admin_ui()
{
	std::string title, genre, newTitle, newGenre, newTrailer;
	int newReleaseYear, newLikesNumber;
	std::cout << "Title: ";
	getline(std::cin, title);
	std::cout << "Genre: ";
	getline(std::cin, genre);

	std::cout << "New title: ";
	getline(std::cin, newTitle);
	std::cout << "New genre: ";
	getline(std::cin, newGenre);
	std::cout << "New year of release: ";
	std::cin >> newReleaseYear;
	std::cout << "New number of likes: ";
	std::cin >> newLikesNumber;
	std::cout << "New trailer: ";
	std::cin.ignore();
	getline(std::cin, newTrailer);

	Movie movie = Movie(title, genre, 0, 0, "...");
	Movie newMovie = Movie(newTitle,newGenre,newReleaseYear,newLikesNumber,newTrailer);

	bool result = this->service.update_admin_service(movie, newMovie);
	if (result == false)
		std::cout << "Update did not succeed\n";
	else
		std::cout << "Update succeded\n";
}

bool compare(const Movie& movie1,const Movie& movie2)
{
	return movie1.get_title()<movie2.get_title();
}

void UI::display_admin_ui()
{
	auto repo_copy = this->service.get_adminRepo();

	std::sort(repo_copy.begin(), repo_copy.end(), compare);

	for (auto& movie : repo_copy) {
		std::cout <<movie.representation()<<std::endl;
	}

	/*for (int i = 0; i < this->service.get_adminRepo().size(); i++)
		std::cout << this->service.get_adminRepo()[i].representation() << "\n";*/
	/*for (const auto& movie : this->service.get_adminRepo()) {
		std::cout << movie.get_title() << " " << movie.get_genre() << std::endl;*/
}

void UI::see_movies_ui()
{
	#define ADD_YES "yes"
    #define SEE_NO "no"

	std::string genre="";
	std::cout << "Give genre: ";
	getline(std::cin, genre);

	if (genre == "")
	{
		int currentSize = 0;
		int watchedMovies=0;

		while (currentSize < this->service.get_adminRepo().size())
		{
			Movie movie = this->service.get_adminRepo()[currentSize];

			if (this->service.get_user_repo().find_position_userRepo(movie) == -1)
			{
				std::cout << "Playing " << movie.representation() << "\n";
				std::string trailer = movie.get_trailer();

				ShellExecuteA(NULL, "open", trailer.c_str(), 0, 0, SW_SHOWNORMAL);

				std::cout << "Add movie to watchlist? 1-yes/2-no";
				std::string option1;
				getline(std::cin, option1);

				if (option1 == ADD_YES)
					this->service.add_user_service(movie);

				std::cout << "Do you want to watch further?";
				std::string option2;
				getline(std::cin, option2);

				if (option2 == SEE_NO)
					break;

				watchedMovies++;
			}
			currentSize++;
			if (currentSize == this->service.get_adminRepo().size())
			{
				currentSize = 0;
				if (watchedMovies == 0)
				{
					std::cout << "no movies left\n";
					break;
				}
			}
		}
	}
	else
	{
		int currentSize = 0,counter=0,watchedMovies=0;
		std::vector<Movie> newList;

		for (int i = 0; i < this->service.get_adminRepo().size(); i++)
			if (this->service.get_adminRepo()[i].get_genre() == genre)
				newList.push_back(this->service.get_adminRepo()[i]);
		counter = newList.size();


		while (currentSize < counter)
		{
			Movie movie = newList[currentSize];
			if (this->service.get_user_repo().find_position_userRepo(movie) == -1)
			{
				std::cout << "Playing " << movie.representation() << "\n";
				std::string trailer = movie.get_trailer();

				ShellExecuteA(NULL, "open", trailer.c_str(), 0, 0, SW_SHOWNORMAL);

				std::cout << "Add movie to watchlist? 1-yes/2-no";
				std::string option1;
				getline(std::cin, option1);

				if (option1 == ADD_YES)
					this->service.add_user_service(movie);

				std::cout << "Do you want to watch further? 1-yes/2-no";
				std::string option2;
				getline(std::cin, option2);

				if (option2 == SEE_NO)
					break;

				watchedMovies++;
			}
			currentSize++;
			if (currentSize == this->service.get_adminRepo().size())
			{
				currentSize = 0;
				if (watchedMovies == 0)
				{
					std::cout << "no movies left\n";
					break;
				}
			}
		}
	}
}

void UI::remove_user_ui()
{
	std::string title, genre;
	std::cout << "Title: ";
	getline(std::cin, title);
	std::cout << "Genre: ";
	getline(std::cin, genre);

	Movie movie = Movie(title, genre, 0, 0, "...");
	bool result = this->service.remove_user_service(movie);

	if (result == false)
		std::cout << "Movie doesnt exist\n";
	else
		std::cout << "Movie removed succesfully form watchlist\n";
}

void UI::display_watchlist_ui()
{
	for (auto movie : this->service.get_userRepo())
		std::cout << movie.representation();
	/*for (int i = 0; i < this->service.get_userRepo().size(); i++)
		std::cout <<this->service.get_userRepo()[i].representation() <<"\n";*/
}
