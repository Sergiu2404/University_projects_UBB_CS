#include "HTMLUserRepository.h"
#include "Exceptions.h"
#include <sstream>
#include "Movie.h"
#include <fstream>

void strip(std::string string)
{
	while (!string.empty() && (string.back() == ' ' || string.back() == '\t' || string.back() == '\n'))
		string.pop_back();
	reverse(string.begin(), string.end());

	while (!string.empty() && (string.back() == ' ' || string.back() == '\t' || string.back() == '\n'))
		string.pop_back();
	reverse(string.begin(), string.end());
}

void html_strip(std::string string)
{
	strip(string);

	string = string.substr(0, string.size() - 5);
	reverse(string.begin(), string.end());

	string = string.substr(0, string.size() - 4);
	reverse(string.begin(), string.end());

	strip(string);
}

void link_strip(std::string string)
{
	string = string.substr(0, string.size() - 4);
	reverse(string.begin(), string.end());

	string = string.substr(0, string.size() - 9);
	reverse(string.begin(), string.end());


	string = string.substr(0, string.size() - ((string.size() - 2) / 2) - 2);
}



std::vector<Movie> HTMLUserRepository::read_from_file()
{
	std::ifstream file("watchList.html");

	if (!file.is_open()) {
		throw FileException("[File Exception]\n");
	}

	std::vector<Movie> new_watchlist;
	std::string line;

	for (int i = 1; i <= 7; i++)
		getline(file, line);


	do
	{
		getline(file, line);
		strip(line);
		if (line != "<tr>")
			break;

		std::string title, genre, releaseYear, likesNumber, trailer;

		getline(file, title);
		html_strip(title);

		getline(file, genre);
		html_strip(genre);

		getline(file, releaseYear);
		html_strip(releaseYear);

		getline(file, likesNumber);
		html_strip(likesNumber);

		getline(file, trailer);
		html_strip(trailer);
		link_strip(trailer);

		getline(file, line);
		new_watchlist.push_back(Movie(title, genre, stoi(releaseYear), stoi(likesNumber), trailer));

	} while (true);

	file.close();

	return new_watchlist;
}

void HTMLUserRepository::write_to_file()
{
	std::ofstream file("watchList.html");

	if (!file.is_open())
		throw FileException("[File Exception] writing html\n");


	file << "<!DOCTYPE html>\n<html>\n<head>\n<title>Your Current WatchList</title>\n</head>\n"
		"<table style=\"color:black;border:3px solid;\">\n";

	for (auto movie : this->get_watchList())
		file << "<tr>\n"
		<< "<td>" << movie.get_title() << "</td>\n"
		<< "<td>" << movie.get_genre() << "</td>\n"
		<< "<td>" << movie.get_releaseYear() << "</td>\n"
		<< "<td>" << movie.get_likesNumber() << "</td>\n"
		<< "<td><a href=\"" << movie.get_trailer() << "\">" << movie.get_trailer() << "</a></td>\n"
		<< "</tr>\n";

	file << "</table>\n</body>\n</html>\n";
	file.close();
}



void HTMLUserRepository::display() const
{
	std::string string = "start \"\" \"C:\\Users\\Sergiu\\Desktop\\OOP\\A7\\A7-8\\A7-8\\watchList.html\"";
	system(string.c_str());
}
