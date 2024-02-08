#include "UI.h"
#include <iostream>
#include <string>
#include <cstring>
#include <vector>

#include <Windows.h>
#include "shellapi.h"

#define ADMIN_ROLE "1"
#define USER_ROLE "2"
#define EXIT "3"

#define ADMIN_ADD "1"
#define ADMIN_REMOVE "2"
#define ADMIN_UPDATE "3"
#define ADMIN_DISPLAY "4"
#define ADMIN_EXIT "5"

#define USER_SEE "1"
#define USER_REMOVE "2"
#define USER_DISPLAY "3"
#define USER_EXIT "4"

using namespace std;

void UI::print_start_menu()
{
	cout << "STARTING MENU==========\n";
	cout << "1-ADMIN/2-USER/3-EXIT\n";
	cout << "=======================\n";
}

void UI::print_admin_menu()
{
	cout << "ADMINISTRATOR MENU=========================\n";
	cout << "1-ADD/2-REMOVE/3-UPDATE/4-DISPLAY/5-EXIT\n";
	cout << "===========================================\n";
}

void UI::print_user_menu()
{
	cout << "USER MENU=======================================================================\n";
	cout << "1-SEE MOVIES BY GIVEN GENRE/2-REMOVE FROM WATCHLIST/3-DISPLAY WATCHLIST/4-EXIT\n";
	cout << "================================================================================\n";
}


void UI::start_menu()
{
	this->service.initService();

	while (true)
	{
		this->print_start_menu();

		cout << "starting menu: ";
		string loginOption;// = login_option();
		//cin.ignore();
		getline(cin, loginOption);
		
		/*while (loginOption != ADMIN_ROLE && loginOption != USER_ROLE && loginOption != EXIT)
			cin >> loginOption;*/

		if (loginOption == ADMIN_ROLE)
		{
			while(true)
			{
				this->print_admin_menu();

				cout << "enter option: ";
				string adminOption = "";//=admin_option();
			    //cin.ignore();
			    getline(cin, adminOption);

				if (adminOption == ADMIN_ADD)
					this->add_admin_ui();
				else if (adminOption == ADMIN_REMOVE)
					this->remove_admin_ui();
				else if (adminOption == ADMIN_UPDATE)
					this->update_admin_ui();
				else if (adminOption == ADMIN_DISPLAY)
					this->display_admin_ui();
				else if (adminOption == ADMIN_EXIT)
					break;
				else
					cout << "Invalid option\n";
			}
		}
		else if (loginOption == USER_ROLE)
		{
			while (true)
			{
				this->print_user_menu();

				cout << "enter option: ";
				string userOption="";// = user_option();
				getline(cin, userOption);

				if (userOption == USER_SEE)
					this->see_user_ui();
				else if (userOption == USER_REMOVE)
					this->remove_user_ui();
				else if (userOption == USER_DISPLAY)
					this->display_user_ui();
				else if (userOption == USER_EXIT)
					break;
				else
					cout << "Invalid option\n";

			}
		}
		else if (loginOption == EXIT)
			break;
		else
			cout << "Invalid option\n";
	}
}

//void UI::admin_menu()
//{
//}
//
//void UI::user_menu()
//{
//}

void UI::add_admin_ui()
{
	string title, genre, trailer;
	int releaseYear, likesNumber;
	cout << "Title: ";
	getline(cin, title);
	cout << "Genre: ";
	getline(cin,genre);
	cout << "Year of release: ";
	cin >> releaseYear;
	cout << "Number of likes: ";
	cin >> likesNumber;
	cout << "Trailer: ";
	cin.ignore();
	getline(cin, trailer);

	Movie movie = Movie(title,genre,releaseYear,likesNumber,trailer);

	bool result = this->service.add_admin_service(movie);
	if (result == false)
		cout << "Movie has not been added, try again!\n";
	else
		cout << "Movie added succesfully\n";
}

void UI::remove_admin_ui()
{
	string title, genre;
	cout << "Title: ";
	getline(cin, title);
	cout << "Genre: ";
	getline(cin, genre);

	Movie movie = Movie(title, genre, 0, 0, "...");

	bool result = this->service.remove_admin_service(movie);
	if (result == false)
		cout << "Movie does not exist, try again!\n";
	else
		cout << "Movie removed succesfully\n";
}

void UI::update_admin_ui()
{
	string title, genre,newTitle,newGenre,newTrailer;
	int newReleaseYear, newLikesNumber;
	cout << "Title: ";
	getline(cin, title);
	cout << "Genre: ";
	getline(cin, genre);
	
	cout << "New title: ";
	getline(cin, newTitle);
	cout << "New genre: ";
	getline(cin, newGenre);
	cout << "New year of release: ";
	cin >> newReleaseYear;
	cout << "New number of likes: ";
	cin >> newLikesNumber;
	cout << "New trailer: ";
	cin.ignore();
	getline(cin, newTrailer);

	Movie movie = Movie(title, genre, 0, 0, "...");
	Movie newMovie = Movie(newTitle, newGenre, newReleaseYear, newLikesNumber, newTrailer);

	bool result = this->service.update_admin_service(movie,newMovie);
	if (result == false)
		cout << "Movie pdate failed, try again!\n";
	else
		cout << "Movie updated succesfully\n";
}

void UI::display_admin_ui()
{
    cout << "Actual list of movies: ";
	for (int i = 0; i < this->service.get_service_size(); i++)
		cout << this->service.get_adminRepo().get_movies().get_element(i).representation();
}

void UI::see_user_ui()
{
	string genre="";// = get_genre();
	int currentMovie = 0, watchedMovie = 0,counter=0;
	cout << "Give movie genre: ";
	//cin.ignore();
	getline(cin, genre);

//	counter = this->service.get_adminRepo().get_movies_size();
//
//	while (currentMovie < counter)
//	{
//		Movie movie = this->service.get_adminRepo().get_movies().get_element(currentMovie);
//
//		if (this->service.get_userRepo().find_movie_position(movie) == -1
//			&& (genre == "" || movie.get_genre() == genre))
//		{
//			//if movie has been already added to the watchlist and if movie genre corresponds
//			cout << "Playing trailer of movie: " << movie.representation();
//
//			//For playing on youtube
//			string trailerLink = movie.get_trailer();
//			//ShellExecuteA(NULL,"open", trailerLink.c_str(), NULL, NULL, SW_SHOWNORMAL);
//
//			cout << "Would you like to add the movie to the watchlist? 1-yes/2-no\n";
//			string option;
//			//cin.ignore();
//			getline(cin, option);
//
//#define OPTION_ADD_YES_STR "yes" //"1"
//#define OPTION_ADD_1 "1"
//			if (option == OPTION_ADD_YES_STR || option == OPTION_ADD_1)
//				this->service.add_user_service(movie);
//
//			cout << "Would you like to watch further? 1-yes/2-no\n";
//			string option2 = "";
//			//cin.ignore();
//			getline(cin, option2);
//#define OPTION_SEE_NO "no" //"2"
//#define OPTION_SEE_2 "2"
//			if (option2 == OPTION_SEE_NO || option2 == OPTION_SEE_2)
//				return;
//
//			watchedMovie++;
//		}
//		currentMovie++;
//
//		if (currentMovie == this->service.get_adminRepo().get_movies_size())
//		{
//			currentMovie = 0;
//			if (watchedMovie == 0)
//			{
//				cout << "No movies left or wrong genre\n";
//				return;
//			}
//		}
//	}

	if (genre == "")
	{
		counter = this->service.get_adminRepo().get_movies_size();

		while (currentMovie < counter)
		{
			Movie movie = this->service.get_adminRepo().get_movies().get_element(currentMovie);

			if (this->service.get_userRepo().find_movie_position(movie) == -1
				&& (genre == "" || movie.get_genre() == genre))
			{
				//if movie has been already added to the watchlist and if movie genre corresponds
				cout << "Playing trailer of movie: " << movie.representation();

				//For playing on youtube
				string trailerLink = "start " + movie.get_trailer();
				const char* start = trailerLink.c_str();
				system(start);
				/*string trailerLink = movie.get_trailer();
				ShellExecuteA(NULL,"open", trailerLink.c_str(), NULL, NULL, SW_SHOWNORMAL);*/

				cout << "Would you like to add the movie to the watchlist? 1-yes/2-no\n";
				string option;
				//cin.ignore();
				getline(cin, option);

				#define OPTION_ADD_YES_STR "yes" //"1"
				#define OPTION_ADD_1 "1"
				if (option == OPTION_ADD_YES_STR || option==OPTION_ADD_1)
					this->service.add_user_service(movie);

				cout << "Would you like to watch further? 1-yes/2-no\n";
				string option2="";
				//cin.ignore();
				getline(cin, option2);
				#define OPTION_SEE_NO "no" //"2"
				#define OPTION_SEE_2 "2"
				if (option2 == OPTION_SEE_NO || option2 == OPTION_SEE_2)
					return;

				watchedMovie++;
			}
			currentMovie++;

			if (currentMovie == this->service.get_adminRepo().get_movies_size())
			{
				currentMovie = 0;
				if (watchedMovie == 0)
				{
					cout << "No movies left\n";
					return;
				}
			}
		}
	}
	else
	{
		counter = 0;
		currentMovie = 0;
		//vector<Movie> newArray;
		DynamicArray<Movie> newArray;
		for (int i = 0; i < this->service.get_adminRepo().get_movies_size();i++)
			if (this->service.get_adminRepo().get_movies().get_element(i).get_genre() == genre)
			{
				newArray.add(this->service.get_adminRepo().get_movies().get_element(i));
				//newArray.push_back(this->service.get_adminRepo().get_movies().get_element(i));
				counter++;
			}

		while (currentMovie < counter)
		{
			Movie movie = newArray.get_element(currentMovie);
			//Movie movie = newArray[currentMovie];
			cout << movie.representation();
			if (this->service.get_userRepo().find_movie_position(movie) == -1 && movie.get_genre() == genre)
			{
				cout << "Playing trailer of movie: " << movie.representation();

				//For playing on youtube
				//string trailerLink = movie.get_trailer();
				string trailerLink = "start " + movie.get_trailer();
				const char* start = trailerLink.c_str();
				system(start);
				//ShellExecuteA(NULL,"open", trailerLink.c_str(), NULL, NULL, SW_SHOWNORMAL);

				cout << "Would you like to add the movie to the watchlist? 1-yes/2-no\n";
				string option;
				getline(cin, option);

				if ((option == OPTION_ADD_YES_STR || option == OPTION_ADD_1) && this->service.get_userRepo().find_movie_position(movie)==-1)
					this->service.add_user_service(movie);

				cout << "Would you like to watch further? 1-yes/2-no\n";
				string option2="";
				getline(cin, option2);
				if (option2 == OPTION_SEE_NO || option == OPTION_SEE_2)
					return;

				watchedMovie++;
			}
			currentMovie++;
			if (currentMovie == counter)
			{
				cout << "No movies left or wrong genre\n";
				return;
			}
		}
	}
}

void UI::remove_user_ui()
{
	string title, genre;
	cout << "Title: ";
	getline(cin, title);
	cout << "Genre: ";
	getline(cin, genre);

	Movie movie = Movie(title, genre, 0, 0, "...");

	if (this->service.remove_user_service(movie) == false)
		cout << "Movie does not exist in the watchlist\n";
	this->service.remove_user_service(movie);
	cout << "Movie removed succesfully\n";
}

void UI::display_user_ui()
{
	cout << "Your watchlist: ";
	for (int i = 0; i < this->service.get_userRepo().get_watchList_size(); i++)
		cout << this->service.get_userRepo().get_watchList().get_element(i).representation();
}
