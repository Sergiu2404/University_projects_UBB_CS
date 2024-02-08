#include "CSVUserRepository.h"
#include "Exceptions.h"
#include <fstream>

void CSVUserRepository::write_to_file()
{
	std::ofstream file("watchList.csv");

	if (!file.is_open())
		throw FileException("[File Exception] writing csv repo\n");

	for (auto movie : this->get_watchList())
		file << movie.representation() << "\n";

	file.close();
}

void CSVUserRepository::display() const
{
	std::string notepadString = "notepad\"watchList.csv\"";
	system(notepadString.c_str());
}