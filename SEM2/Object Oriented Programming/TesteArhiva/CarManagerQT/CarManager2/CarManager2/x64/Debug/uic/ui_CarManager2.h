/********************************************************************************
** Form generated from reading UI file 'CarManager2.ui'
**
** Created by: Qt User Interface Compiler version 6.5.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_CARMANAGER2_H
#define UI_CARMANAGER2_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QCheckBox>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QListWidget>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QSlider>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_CarManager2Class
{
public:
    QWidget *centralWidget;
    QListWidget *carsList;
    QPushButton *addButton;
    QPushButton *manufacturerButton;
    QSlider *yearSlider;
    QPushButton *yearButton;
    QLineEdit *manufacturerLineEdit;
    QCheckBox *grey;
    QCheckBox *blue;
    QCheckBox *red;
    QLineEdit *colorLineEdit;
    QLineEdit *yearLineEdit;
    QLineEdit *modelLineEdit;
    QLineEdit *brandLineEdit;
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *CarManager2Class)
    {
        if (CarManager2Class->objectName().isEmpty())
            CarManager2Class->setObjectName("CarManager2Class");
        CarManager2Class->resize(595, 400);
        centralWidget = new QWidget(CarManager2Class);
        centralWidget->setObjectName("centralWidget");
        carsList = new QListWidget(centralWidget);
        carsList->setObjectName("carsList");
        carsList->setGeometry(QRect(0, 0, 256, 192));
        addButton = new QPushButton(centralWidget);
        addButton->setObjectName("addButton");
        addButton->setGeometry(QRect(410, 170, 75, 24));
        manufacturerButton = new QPushButton(centralWidget);
        manufacturerButton->setObjectName("manufacturerButton");
        manufacturerButton->setGeometry(QRect(260, 280, 81, 24));
        yearSlider = new QSlider(centralWidget);
        yearSlider->setObjectName("yearSlider");
        yearSlider->setGeometry(QRect(410, 250, 160, 18));
        yearSlider->setMinimum(1900);
        yearSlider->setMaximum(2023);
        yearSlider->setOrientation(Qt::Horizontal);
        yearButton = new QPushButton(centralWidget);
        yearButton->setObjectName("yearButton");
        yearButton->setGeometry(QRect(420, 280, 75, 24));
        manufacturerLineEdit = new QLineEdit(centralWidget);
        manufacturerLineEdit->setObjectName("manufacturerLineEdit");
        manufacturerLineEdit->setGeometry(QRect(260, 240, 113, 21));
        grey = new QCheckBox(centralWidget);
        grey->setObjectName("grey");
        grey->setGeometry(QRect(10, 200, 75, 20));
        blue = new QCheckBox(centralWidget);
        blue->setObjectName("blue");
        blue->setGeometry(QRect(10, 220, 75, 20));
        red = new QCheckBox(centralWidget);
        red->setObjectName("red");
        red->setGeometry(QRect(10, 240, 75, 20));
        colorLineEdit = new QLineEdit(centralWidget);
        colorLineEdit->setObjectName("colorLineEdit");
        colorLineEdit->setGeometry(QRect(410, 140, 113, 21));
        yearLineEdit = new QLineEdit(centralWidget);
        yearLineEdit->setObjectName("yearLineEdit");
        yearLineEdit->setGeometry(QRect(410, 110, 113, 21));
        modelLineEdit = new QLineEdit(centralWidget);
        modelLineEdit->setObjectName("modelLineEdit");
        modelLineEdit->setGeometry(QRect(410, 80, 113, 21));
        brandLineEdit = new QLineEdit(centralWidget);
        brandLineEdit->setObjectName("brandLineEdit");
        brandLineEdit->setGeometry(QRect(410, 50, 113, 21));
        CarManager2Class->setCentralWidget(centralWidget);
        menuBar = new QMenuBar(CarManager2Class);
        menuBar->setObjectName("menuBar");
        menuBar->setGeometry(QRect(0, 0, 595, 22));
        CarManager2Class->setMenuBar(menuBar);
        mainToolBar = new QToolBar(CarManager2Class);
        mainToolBar->setObjectName("mainToolBar");
        CarManager2Class->addToolBar(Qt::TopToolBarArea, mainToolBar);
        statusBar = new QStatusBar(CarManager2Class);
        statusBar->setObjectName("statusBar");
        CarManager2Class->setStatusBar(statusBar);

        retranslateUi(CarManager2Class);

        QMetaObject::connectSlotsByName(CarManager2Class);
    } // setupUi

    void retranslateUi(QMainWindow *CarManager2Class)
    {
        CarManager2Class->setWindowTitle(QCoreApplication::translate("CarManager2Class", "CarManager2", nullptr));
        addButton->setText(QCoreApplication::translate("CarManager2Class", "add", nullptr));
        manufacturerButton->setText(QCoreApplication::translate("CarManager2Class", "manufacturer", nullptr));
        yearButton->setText(QCoreApplication::translate("CarManager2Class", "year", nullptr));
        grey->setText(QCoreApplication::translate("CarManager2Class", "grey", nullptr));
        blue->setText(QCoreApplication::translate("CarManager2Class", "blue", nullptr));
        red->setText(QCoreApplication::translate("CarManager2Class", "red", nullptr));
    } // retranslateUi

};

namespace Ui {
    class CarManager2Class: public Ui_CarManager2Class {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_CARMANAGER2_H
