#pragma once
#include "Movie.h"
#include "DynamicArray.h"

class AdministratorRepository {
private:
	DynamicArray<Movie> movies; //movies is a dynamic array
public:
	int get_movies_size();
	//DynamicArray<Movie> get_movies();

	bool add_admin(Movie movie);
	bool remove_admin(Movie movie);
	bool update_admin(Movie movie,Movie newMovie);
	DynamicArray<Movie> get_movies();
	//Movie get_element(int position);

	int find_movie_position(Movie movie);
};