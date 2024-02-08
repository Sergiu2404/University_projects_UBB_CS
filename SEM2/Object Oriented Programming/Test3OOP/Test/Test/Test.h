#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_Test.h"
#include "Service.h"

class Test : public QMainWindow
{
    Q_OBJECT

public:
    Test(Service service,QWidget *parent = nullptr);
    ~Test();

    void populateList();
    void useSignals();

    void populateCategory();
    void populateFiltered();

private:
    Service service;
    Ui::TestClass ui;
};
