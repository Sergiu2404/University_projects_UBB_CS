#include "Movie.h"
#include <sstream>
#include <vector>
#include <string>

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

void Movie::set_title(std::string title)
{
	this->title = title;
}

void Movie::set_genre(std::string genre)
{
	this->genre = genre;
}

void Movie::set_releaseYear(int releaseYear)
{
	this->releaseYear = releaseYear;
}

void Movie::set_likesNumber(int likesNumber)
{
	this->likesNumber = likesNumber;
}

void Movie::set_trailer(std::string trailer)
{
	this->trailer = trailer;
}

std::string Movie::representation()
{
	std::ostringstream output;
	output << "Title: " << this->get_title() << "; ";
	output << "Genre: " << this->get_genre() << "; ";
	output << "Year of release: " << this->get_releaseYear() << "; ";
	output << "Number of likes: " << this->get_likesNumber() << "; ";
	output << "Trailer link: " << this->get_trailer() << "\n";
	return output.str();
}


std::vector<std::string> tokenize(std::string& string,  char delimiter)
{
	std::vector<std::string> result;
	std::stringstream ss(string);
	std::string(token);

	while (getline(ss, token, delimiter))
		result.push_back(token);

	return result;
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

std::istream& operator>>(std::istream& inputStream, Movie movie)
{
	std::string line;
	getline(inputStream, line);

	std::vector<std::string> tokens = tokenize(line, ',');

	/*if (tokens.size() != 5)
		return inputStream;*/

	movie.set_title(tokens[0]);
	movie.set_genre(tokens[1]);
	movie.set_releaseYear(stoi(tokens[2]));
	movie.set_likesNumber(stoi(tokens[3]));
	movie.set_trailer(tokens[4]);

	return inputStream;
}

std::ostream& operator<<(std::ostream& outputStream, Movie movie)
{
	outputStream << movie.get_title() << "," << movie.get_genre() << "," << movie.get_releaseYear() << "," << movie.get_likesNumber() << "," << movie.get_trailer() << "\n";

	return outputStream;
}
