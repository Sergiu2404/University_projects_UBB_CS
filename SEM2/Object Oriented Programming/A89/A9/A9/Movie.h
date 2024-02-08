#pragma once
#include <iostream>

class Movie
{
protected:
	std::string title, genre, trailer;
	int releaseYear, likesNumber;

public:
	Movie(std::string title, std::string genre, int releaseYear, int likesNumber, std::string trailer) :title{ title },
		genre{ genre },
		releaseYear{ releaseYear },
		likesNumber{ likesNumber },
		trailer{ trailer } {}
	Movie() {}


	std::string get_title() const { return this->title; }
	std::string get_genre() const { return this->genre; }
	int get_releaseYear() const { return this->releaseYear; }
	int get_likesNumber() const { return this->likesNumber; }
	std::string get_trailer() const { return this->trailer; }

	void set_title(std::string Title) { this->title = Title; }
	void set_genre(std::string Genre) { this->genre = Genre; }
	void set_releaseYear(int releaseYear) { this->releaseYear = releaseYear; }
	void set_likesNumber(int likesNumber) { this->likesNumber = likesNumber; }
	void set_trailer(std::string trailer) { this->trailer = trailer; }

	std::string representation();

	friend std::istream& operator>>(std::istream& inputStream, Movie movie);
	friend std::ostream& operator<<(std::ostream& outputStream, Movie movie);
};

