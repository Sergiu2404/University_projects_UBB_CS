#include "Movie.h"
#include <sstream>

Movie::Movie(std::string title, std::string genre, int releaseYear, int likesNumber, std::string trailer): title{title},genre{genre},releaseYear{releaseYear},likesNumber{likesNumber},trailer{trailer}
{
}

Movie::Movie()
{
	this->title = "";
	this->genre = "";
	this->releaseYear = 0;
	this->likesNumber = 0;
	this->trailer = "";
}

std::string Movie::get_title()
{
	return this->title;
}

std::string Movie::get_genre()
{
	return this->genre;
}

int Movie::get_releaseYear()
{
	return this->releaseYear;
}

int Movie::get_likesNumber()
{
	return this->likesNumber;
}

std::string Movie::get_trailer()
{
	return this->trailer;
}

std::string Movie::representation()
{
	std::ostringstream output;

	output << "Title: " << this->title << " |Genre: " << this->genre << " |Year: " << this->releaseYear << " |Likes: " << this->likesNumber << " |Trailer: " << this->trailer<<"\n";
	return output.str();
}
