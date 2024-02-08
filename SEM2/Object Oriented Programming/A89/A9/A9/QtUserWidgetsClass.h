#pragma once

#include <QMainWindow>
#include "ui_QtUserWidgetsClass.h"
#include "Service.h"
#include "CSVUserRepository.h"

class QtUserWidgetsClass : public QMainWindow
{
	Q_OBJECT

public:
	QtUserWidgetsClass(Service service,QWidget *parent = nullptr);
	~QtUserWidgetsClass();

	void runUserWindow();

	void populateMoviesList();
	void handleSignals();
	void populateWatchList();
	void repopulateWatchList();

	void addToWatchList();
	void removeFromWatchList();
	void watchTrailer();

	void write_to_file();
	std::vector<Movie> read_from_file();

	int getSelectedIndex();
	int getSelectedIndexWatchList();

private:
	std::vector<Movie> watchList;
	/*AdministratorRepository adminRepo;
	UserRepository* csvRepo = new CSVUserRepository{};
	Service service{adminRepo,csvRepo};*/
	Service service;
	Ui::QtUserWidgetsClassClass ui;
};
