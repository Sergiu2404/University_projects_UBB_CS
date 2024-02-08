#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_CarManager2.h"
#include "Service.h"

class CarManager2 : public QMainWindow
{
    Q_OBJECT

public:
    CarManager2(Service service,QWidget *parent = nullptr);
    ~CarManager2();

    void populateList();
    void useSignals();

    void populateManufacturer();
    void populateCheckBox();
    void populateYear();

    void add();

private:
    Service service;
    Ui::CarManager2Class ui;
};
