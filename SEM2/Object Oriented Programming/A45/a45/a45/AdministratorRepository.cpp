#include "AdministratorRepository.h"


	/*Movie movie1 = Movie("movie1","action",2023,1400000,"https://www.youtube.com/watch?v=2m1drlOZSDw");
	Movie movie2 = Movie("movie2", "action", 2023, 1200000, "https://www.youtube.com/watch?v=MeF5QeZ_LLI");
	Movie movie3 = Movie("movie3", "drama", 2020, 100000, "https://www.youtube.com/watch?v=2m1drlOZSDw");
	Movie movie4 = Movie("movie4", "thriller", 2010, 3500000, "https://www.youtube.com/watch?v=2m1drlOZSDw");
	Movie movie5 = Movie("movie5", "horror", 2001, 105000, "https://www.youtube.com/watch?v=2m1drlOZSDw");
	this->dynamicArray->add_elem(movie1);
	this->dynamicArray->add_elem(movie2);
	this->dynamicArray->add_elem(movie3);
	this->dynamicArray->add_elem(movie4);
	this->dynamicArray->add_elem(movie5);*/

int AdministratorRepository::get_movies_size()
{
	return this->movies.get_size();
}

//DynamicArray<Movie> AdministratorRepository::get_movies()
//{
//	return this->movies.get_movies();
//}

bool AdministratorRepository::add_admin(Movie movie)
{
	int position = this->find_movie_position(movie);
	if (position != -1)
		return false;
	
	this->movies.add(movie);
	return true;
}

bool AdministratorRepository::remove_admin(Movie movie)
{
	int position = this->find_movie_position(movie);
	if(position==-1)
		return false;

	this->movies.remove(position);
	return true;
}

bool AdministratorRepository::update_admin(Movie movie,Movie newMovie)
{
	int position = this->find_movie_position(movie);
	if (position == -1)
		return false;

	this->movies.update(position, newMovie);
	/*this->movies.get_element(position).set_title(movie.get_title());
	this->movies.get_element(position).set_genre(movie.get_genre());
	this->movies.get_element(position).set_releaseYear(movie.get_releaseYear());
	this->movies.get_element(position).set_likesNumber(movie.get_likesNumber());
	this->movies.get_element(position).set_trailer(movie.get_trailer());*/
	return true;
}

DynamicArray<Movie> AdministratorRepository::get_movies()
{
	return this->movies;
}


//Movie AdministratorRepository::get_element(int position)
//{
//	return this->movies.get_element(int position);
//}


int AdministratorRepository::find_movie_position(Movie movie)
{
	for(int i=0;i<this->movies.get_size();i++)
		if (this->movies.get_element(i).get_title() == movie.get_title() && this->movies.get_element(i).get_genre() == movie.get_genre())
			return i;

	return -1;
}
