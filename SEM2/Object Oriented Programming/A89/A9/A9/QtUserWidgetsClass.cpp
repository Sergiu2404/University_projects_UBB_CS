#include "QtUserWidgetsClass.h"
#include <QListWidget>
#include <QMessageBox>

#include <Windows.h>
#include <shellapi.h>
#include <fstream>
#include <string>
#include <sstream>

QtUserWidgetsClass::QtUserWidgetsClass(Service service, QWidget* parent)
	: QMainWindow(parent), service{ service }
{
	ui.setupUi(this);
	if (this->read_from_file().size() != 0)
		this->watchList = this->read_from_file();
	else this->watchList = {};
	this->populateMoviesList();
	this->populateWatchList();
	this->handleSignals();
	//this->runUserWindow();
}

QtUserWidgetsClass::~QtUserWidgetsClass()
{}

void QtUserWidgetsClass::runUserWindow()
{
}


int QtUserWidgetsClass::getSelectedIndex()
{
	QModelIndexList selectedIndexes = this->ui.adminMoviesListWidget->selectionModel()->selectedIndexes();

	if (selectedIndexes.size() == 0)
		return -1;

	int selectedIndex = selectedIndexes.at(0).row();
	return selectedIndex;
}

int QtUserWidgetsClass::getSelectedIndexWatchList()
{
	QModelIndexList selectedIndexes = this->ui.watchListWidget->selectionModel()->selectedIndexes();

	if (selectedIndexes.size() == 0)
		return -1;

	int selectedIndex = selectedIndexes.at(0).row();
	return selectedIndex;
}


void QtUserWidgetsClass::populateMoviesList()
{
	this->ui.adminMoviesListWidget->clear();
	this->ui.watchListWidget->clear();

	for (auto movie : this->service.get_adminRepo())
		this->ui.adminMoviesListWidget->addItem(QString::fromStdString(movie.representation()));
	/*QListWidget* userList = new QListWidget{};
	userList->clear();

	for (auto movie : this->service.get_userRepo())
		userList->addItem(QString::fromStdString(movie.representation()));*/


}

void QtUserWidgetsClass::handleSignals()
{
	QObject::connect(this->ui.addWatchListButton, &QPushButton::clicked, this, &QtUserWidgetsClass::addToWatchList);
	QObject::connect(this->ui.removeWatchListButton, &QPushButton::clicked, this, &QtUserWidgetsClass::removeFromWatchList);
	QObject::connect(this->ui.watchTrailerButton, &QPushButton::clicked, this, &QtUserWidgetsClass::watchTrailer);
}

void QtUserWidgetsClass::populateWatchList()
{
	this->ui.watchListWidget->clear();

	//this->watchList = this->service.get_userRepo();
	for (auto movie : this->watchList) {
		this->ui.watchListWidget->addItem(QString::fromStdString(movie.representation()));
	}
}

void QtUserWidgetsClass::addToWatchList()
{
	int selectedIndex = getSelectedIndex();

	if (selectedIndex < 0)
		QMessageBox::critical(this, "Error", "You need to select a movie from the movies list");
	else {
		Movie movie = this->service.get_adminRepo()[selectedIndex];
		
		this->watchList.push_back(movie);
		//this->service.add_user_service(movie);

		this->populateWatchList();
		this->write_to_file();
	}
}

void QtUserWidgetsClass::removeFromWatchList()
{
	int selectedIndex = getSelectedIndexWatchList();

	if (selectedIndex < 0)
		QMessageBox::critical(this, "Error", "You need to select a movie from the movies list");
	else {
		Movie movie = this->service.get_adminRepo()[selectedIndex];

		this->watchList.erase(this->watchList.begin() + selectedIndex);
		//this->service.remove_user_service(movie);

		this->populateWatchList();
		this->write_to_file();
	}
}

void QtUserWidgetsClass::watchTrailer()
{
	int selectedIndex = getSelectedIndex();

	if (selectedIndex < 0)
		QMessageBox::critical(this, "Error", "You need to select a movie from the movies list");
	else
	{
		Movie movie = this->service.get_adminRepo()[selectedIndex];
		std::string trailer = movie.get_trailer();

		ShellExecuteA(NULL, "open", trailer.c_str(), 0, 0, SW_SHOWNORMAL);

		QMessageBox message(this);
		message.setText("playing...");
		message.show();
	}
}

void QtUserWidgetsClass::write_to_file()
{
	std::ofstream fout("watchlist.csv");
	std::ofstream gout("watchlist.txt");

	for (auto movie : this->watchList)
		fout << movie.representation();

	for (auto movie : this->watchList)
		gout << movie.get_title() << "," << movie.get_genre() << "," << movie.get_releaseYear()
		<< "," << movie.get_likesNumber() << "," << movie.get_trailer() << "\n";

	fout.close();
}

std::vector<std::string> tokeniseQt(std::string& string, char delimiter)
{
	std::vector<std::string> result;
	std::stringstream ss(string);
	std::string(token);

	while (getline(ss, token, delimiter))
		result.push_back(token);

	return result;
}


std::vector<Movie> QtUserWidgetsClass::read_from_file()
{
	std::ifstream fin("watchlist.txt");

	std::vector<Movie> movies;
	Movie movie;
	std::string movieString;

	while (getline(fin, movieString))
	{
		//	movies.push_back(movie);
		std::vector<std::string> tokens = tokeniseQt(movieString, ',');

		Movie movie{ tokens[0], tokens[1], stoi(tokens[2]), stoi(tokens[3]), tokens[4] };

		movies.push_back(movie);
	}

	fin.close();
	return movies;
}
