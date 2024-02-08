#include "Movie.h"
#include <sstream>

std::string Movie::get_title() const
{
	return this->title;
}

std::string Movie::get_genre() const
{
	return this->genre;
}

int Movie::get_releaseYear() const
{
	return this->releaseYear;
}

int Movie::get_likesNumber() const
{
	return this->likesNumber;
}

std::string Movie::get_trailer() const
{
	return this->trailer;
}

std::string Movie::representation()
{
	std::ostringstream output;
	output << "Title: " << this->get_title() << " |Genre: " << this->get_genre() << " |Year: " << this->get_releaseYear() << " |Likes: " << this->get_likesNumber() << " |Trailer: " << this->get_trailer();
	return output.str();
}

//inline Movie& Movie::operator=(Movie& newMovie)
//{
//	if (this == &newMovie)
//		return *this;
//
//	this->title = newMovie.title;
//	this->genre = newMovie.genre;
//	this->releaseYear = newMovie.releaseYear;
//	this->likesNumber = newMovie.likesNumber;
//	this->trailer = newMovie.trailer;
//
//	return *this;
//}
