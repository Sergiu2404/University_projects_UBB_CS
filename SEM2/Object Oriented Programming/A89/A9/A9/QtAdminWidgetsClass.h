#pragma once

#include <QMainWindow>
#include "ui_QtAdminWidgetsClass.h"
#include "Service.h"

class QtAdminWidgetsClass : public QMainWindow
{
	Q_OBJECT

public:
	QtAdminWidgetsClass(Service service,QWidget *parent = nullptr);
	~QtAdminWidgetsClass();

	void runAdminWindow();

	void populateAdminList();
	void handleSignals();

	void add();
	void remove();
	void update();

	int getSelectedIndex();

private:
	Service service;
	Ui::QtAdminWidgetsClassClass ui;
};
