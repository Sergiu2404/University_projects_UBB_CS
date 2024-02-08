#pragma once
#include <string>
#include <iostream>

class Movie {
private:
	std::string title;
	std::string genre;
	int releaseYear;
	int likesNumber;
	std::string trailer;

public:
	Movie(std::string title,std::string genre,int releaseYear,int likesNumber,std::string trailer);
	Movie();

	std::string get_title();
	std::string get_genre();
	int get_releaseYear();
	int get_likesNumber();
	std::string get_trailer();

	std::string representation();
};