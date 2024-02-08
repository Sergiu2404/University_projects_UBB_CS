#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_Weather.h"
#include "Service.h"

class Weather : public QMainWindow
{
    Q_OBJECT

public:
    Weather(Service service,QWidget *parent = nullptr);
    ~Weather();

    void populateList();
    void useSignals();

    void populateFilteredList();
    void populateFilteredBox();

private:
    Ui::WeatherClass ui;
    Service service;
};
