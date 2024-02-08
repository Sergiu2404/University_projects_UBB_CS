#pragma once
#include <iostream>


class Movie {
private:
	std::string title,genre,trailer;
	int releaseYear, likesNumber;

public:
	Movie(std::string title, std::string genre, int releaseYear, int likesNumber, std::string trailer):title{ title },
		genre{ genre },
		releaseYear{ releaseYear },
		likesNumber{ likesNumber },
		trailer{ trailer } {};
	std::string get_title() const;
	std::string get_genre() const;
	int get_releaseYear() const;
	int get_likesNumber() const;
	std::string get_trailer() const;

	std::string representation();

	//inline Movie& operator=(Movie& movie);
};