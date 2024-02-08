#pragma once
#include <iostream>



class Movie {
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


	std::string get_title() const;
	std::string get_genre() const;
	int get_releaseYear() const;
	int get_likesNumber() const;
	std::string get_trailer() const;

	void set_title(std::string Title);
	void set_genre(std::string Genre);
	void set_releaseYear(int releaseYear);
	void set_likesNumber(int likesNumber);
	void set_trailer(std::string trailer);

	std::string representation();

	friend std::istream& operator>>(std::istream& inputStream, Movie movie);
	friend std::ostream& operator<<(std::ostream& outputStream, Movie movie);
	//inline Movie& operator=(Movie& movie);
};

