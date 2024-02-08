#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_A9.h"
#include "Service.h"
class A9 : public QMainWindow
{
    Q_OBJECT

public:
    A9(Service& service,QWidget *parent = nullptr);
    ~A9();

private:
    Ui::A9Class ui;

    void chooseType();
    Service& service;
    void userWindow();

    void adminWindow();
    //void useSignalsAndSlots();
};
