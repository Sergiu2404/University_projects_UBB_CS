#include "QtAdminWidgetsClass.h"
#include <QListWidget>
#include <QMessageBox>
#include "QtUserWidgetsClass.h"

QtAdminWidgetsClass::QtAdminWidgetsClass(Service service,QWidget *parent)
	: QMainWindow(parent),service{service}
{
	ui.setupUi(this);
	this->populateAdminList();
	this->handleSignals();
	//this->runAdminWindow();
}

QtAdminWidgetsClass::~QtAdminWidgetsClass()
{}

void QtAdminWidgetsClass::populateAdminList()
{
	this->ui.adminMoviesList->clear();

	for (auto movie : this->service.get_adminRepo())
		this->ui.adminMoviesList->addItem(QString::fromStdString(movie.representation()));
	/*QListWidget* adminList = new QListWidget{};
	adminList->clear();
	
	for (auto movie : this->service.get_adminRepo())
		adminList->addItem(QString::fromStdString(movie.representation()));*/
}

void QtAdminWidgetsClass::handleSignals()
{
	QObject::connect(this->ui.addButton, &QPushButton::clicked, this, &QtAdminWidgetsClass::add);
	QObject::connect(this->ui.removeButton, &QPushButton::clicked, this, &QtAdminWidgetsClass::remove);
	QObject::connect(this->ui.updateButton, &QPushButton::clicked, this, &QtAdminWidgetsClass::update);
}

void QtAdminWidgetsClass::add()
{
	std::string title, genre, trailer,releaseYearString,likesNumberString;
	int releaseYear, likesNumber;

	title = this->ui.titleLine->text().toStdString();
	genre = this->ui.genreLine->text().toStdString();
	/*releaseYear = stoi(this->ui.yearLine->text().toStdString());
	likesNumber = stoi(this->ui.likesLine->text().toStdString());*/
	releaseYearString = this->ui.yearLine->text().toStdString();
	likesNumberString = this->ui.likesLine->text().toStdString();
	trailer = this->ui.trailerLine->text().toStdString();

	int checkYearNumber = 0, checkLikesNumber = 0;
	char releaseYearChar[100], likesNumberChar[100];
	strcpy(releaseYearChar, releaseYearString.c_str());
	strcpy(likesNumberChar, likesNumberString.c_str());
	for (int i = 0; i < strlen(releaseYearChar); i++)
		if (!isdigit(releaseYearChar[i])) {
			//checkYearNumber = 1;
			QMessageBox::critical(this, "Error", "You have not entered proper informations");
			return;
		}

	for (int i = 0; i < strlen(likesNumberChar); i++)
		if (!isdigit(likesNumberChar[i])) {
			//checkLikesNumber = 1;
			QMessageBox::critical(this, "Error", "You have not entered proper informations");
			return;
		}

	if (this->service.add_admin_service(Movie(title, genre, stoi(releaseYearString), stoi(likesNumberString), trailer)) == false||
		(this->ui.titleLine->text().isEmpty() || this->ui.genreLine->text().isEmpty()|| this->ui.yearLine->text().isEmpty() 
			||this->ui.likesLine->text().isEmpty() || this->ui.trailerLine->text().isEmpty())
		||checkYearNumber==1 || checkLikesNumber==1)
		QMessageBox::critical(this, "Error", "Movie already exists or you have not entered proper informations");
	else
	{
		this->service.add_admin_service(Movie(title, genre, stoi(releaseYearString), stoi(likesNumberString), trailer));
		int rowIndex = this->service.get_adminRepo().size() - 1;
		this->ui.adminMoviesList->setCurrentRow(rowIndex);

		this->populateAdminList();
	}

}

int QtAdminWidgetsClass::getSelectedIndex()
{
	QModelIndexList selectedIndexes = this->ui.adminMoviesList->selectionModel()->selectedIndexes();
	int selectedIndex = selectedIndexes.at(0).row();
	return selectedIndex;
}

void QtAdminWidgetsClass::remove()
{
	int selectedIndex = this->getSelectedIndex();

	Movie movie = this->service.get_adminRepo()[selectedIndex];
	if (this->service.remove_admin_service(movie) == false)
		QMessageBox::critical(this, "Error", "Movie does not exist");
	else
	{
		this->service.remove_admin_service(movie);
		this->populateAdminList();
	}
}

void QtAdminWidgetsClass::update()
{
	std::string title, genre, trailer, newTitle, newGenre, newTrailer,releaseYearString,likesNumberString,newReleaseYearString,newLikesNumberString;
	int releaseYear, likesNumber, newReleaseYear, newLikesNumber;

	title = this->ui.titleLine->text().toStdString();
	genre = this->ui.genreLine->text().toStdString();
	try {
		releaseYear = stoi(this->ui.yearLine->text().toStdString());
		likesNumber = stoi(this->ui.likesLine->text().toStdString());
	}
	catch (std::exception& E)
	{
		QMessageBox::critical(this, "Error", "Field informaion was not properly");
		return;
	}
	trailer = this->ui.trailerLine->text().toStdString();

	newTitle = this->ui.updateTitleLine->text().toStdString();
	newGenre = this->ui.updateGenreLine->text().toStdString();
	try {
		newReleaseYear = stoi(this->ui.updateYearLine->text().toStdString());
		newLikesNumber = stoi(this->ui.updateLikesLine->text().toStdString());
	}
	catch (std::exception& E)
	{
		QMessageBox::critical(this, "Error", "Field informaion was not properly");
		return;
	}
	newTrailer = this->ui.updateTrailerLine->text().toStdString();

	Movie oldMovie(title, genre, releaseYear, likesNumber, trailer);
	Movie newMovie(newTitle, newGenre, newReleaseYear, newLikesNumber, newTrailer);

	if (this->service.update_admin_service(oldMovie, newMovie) == false || 
		(this->ui.titleLine->text().isEmpty() || this->ui.genreLine->text().isEmpty() || this->ui.yearLine->text().isEmpty() || this->ui.likesLine->text().isEmpty() || this->ui.trailerLine->text().isEmpty())||
		(this->ui.updateTitleLine->text().isEmpty()|| this->ui.updateGenreLine->text().isEmpty()|| this->ui.updateYearLine->text().isEmpty()|| this->ui.updateLikesLine->text().isEmpty()|| this->ui.updateTrailerLine->text().isEmpty()))
		QMessageBox::critical(this, "Error", "Movie does not exist or field information is empty");
	else
	{
		this->service.update_admin_service(oldMovie, newMovie);
		this->populateAdminList();
	}
}

