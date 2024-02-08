/********************************************************************************
** Form generated from reading UI file 'Weather.ui'
**
** Created by: Qt User Interface Compiler version 6.5.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_WEATHER_H
#define UI_WEATHER_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QCheckBox>
#include <QtWidgets/QListWidget>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QSlider>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_WeatherClass
{
public:
    QWidget *centralWidget;
    QListWidget *weatherList;
    QCheckBox *checkBox;
    QCheckBox *checkBox_2;
    QCheckBox *checkBox_3;
    QCheckBox *checkBox_4;
    QCheckBox *checkBox_5;
    QListWidget *filteredList;
    QSlider *horizontalSlider;
    QPushButton *searchButton;
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *WeatherClass)
    {
        if (WeatherClass->objectName().isEmpty())
            WeatherClass->setObjectName("WeatherClass");
        WeatherClass->resize(600, 400);
        centralWidget = new QWidget(WeatherClass);
        centralWidget->setObjectName("centralWidget");
        weatherList = new QListWidget(centralWidget);
        weatherList->setObjectName("weatherList");
        weatherList->setGeometry(QRect(10, 0, 256, 192));
        checkBox = new QCheckBox(centralWidget);
        checkBox->setObjectName("checkBox");
        checkBox->setGeometry(QRect(20, 240, 75, 20));
        checkBox_2 = new QCheckBox(centralWidget);
        checkBox_2->setObjectName("checkBox_2");
        checkBox_2->setGeometry(QRect(90, 240, 75, 20));
        checkBox_3 = new QCheckBox(centralWidget);
        checkBox_3->setObjectName("checkBox_3");
        checkBox_3->setGeometry(QRect(150, 240, 75, 20));
        checkBox_4 = new QCheckBox(centralWidget);
        checkBox_4->setObjectName("checkBox_4");
        checkBox_4->setGeometry(QRect(230, 240, 75, 20));
        checkBox_5 = new QCheckBox(centralWidget);
        checkBox_5->setObjectName("checkBox_5");
        checkBox_5->setGeometry(QRect(304, 240, 91, 20));
        filteredList = new QListWidget(centralWidget);
        filteredList->setObjectName("filteredList");
        filteredList->setGeometry(QRect(320, 0, 256, 192));
        horizontalSlider = new QSlider(centralWidget);
        horizontalSlider->setObjectName("horizontalSlider");
        horizontalSlider->setGeometry(QRect(20, 300, 160, 18));
        horizontalSlider->setMaximum(100);
        horizontalSlider->setValue(100);
        horizontalSlider->setOrientation(Qt::Horizontal);
        searchButton = new QPushButton(centralWidget);
        searchButton->setObjectName("searchButton");
        searchButton->setGeometry(QRect(200, 300, 75, 24));
        WeatherClass->setCentralWidget(centralWidget);
        menuBar = new QMenuBar(WeatherClass);
        menuBar->setObjectName("menuBar");
        menuBar->setGeometry(QRect(0, 0, 600, 22));
        WeatherClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(WeatherClass);
        mainToolBar->setObjectName("mainToolBar");
        WeatherClass->addToolBar(Qt::TopToolBarArea, mainToolBar);
        statusBar = new QStatusBar(WeatherClass);
        statusBar->setObjectName("statusBar");
        WeatherClass->setStatusBar(statusBar);

        retranslateUi(WeatherClass);

        QMetaObject::connectSlotsByName(WeatherClass);
    } // setupUi

    void retranslateUi(QMainWindow *WeatherClass)
    {
        WeatherClass->setWindowTitle(QCoreApplication::translate("WeatherClass", "Weather", nullptr));
        checkBox->setText(QCoreApplication::translate("WeatherClass", "sunny", nullptr));
        checkBox_2->setText(QCoreApplication::translate("WeatherClass", "rain", nullptr));
        checkBox_3->setText(QCoreApplication::translate("WeatherClass", "foggy", nullptr));
        checkBox_4->setText(QCoreApplication::translate("WeatherClass", "overcast", nullptr));
        checkBox_5->setText(QCoreApplication::translate("WeatherClass", "thunderstorm", nullptr));
        searchButton->setText(QCoreApplication::translate("WeatherClass", "search", nullptr));
    } // retranslateUi

};

namespace Ui {
    class WeatherClass: public Ui_WeatherClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_WEATHER_H
