#include "Movie.h"
#include <sstream>
#include <vector>


std::vector<std::string> tokenize(std::string& string, char delimiter)
{
	std::vector<std::string> result;
	std::stringstream ss(string);
	std::string(token);

	while (getline(ss, token, delimiter))
		result.push_back(token);

	return result;
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

