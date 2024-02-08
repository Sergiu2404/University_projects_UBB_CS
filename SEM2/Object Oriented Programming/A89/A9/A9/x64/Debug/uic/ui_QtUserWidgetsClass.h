/********************************************************************************
** Form generated from reading UI file 'QtUserWidgetsClass.ui'
**
** Created by: Qt User Interface Compiler version 6.5.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_QTUSERWIDGETSCLASS_H
#define UI_QTUSERWIDGETSCLASS_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QLabel>
#include <QtWidgets/QListWidget>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_QtUserWidgetsClassClass
{
public:
    QWidget *centralWidget;
    QLabel *label;
    QListWidget *adminMoviesListWidget;
    QListWidget *watchListWidget;
    QLabel *label_2;
    QLabel *label_3;
    QPushButton *addWatchListButton;
    QPushButton *removeWatchListButton;
    QPushButton *watchTrailerButton;
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *QtUserWidgetsClassClass)
    {
        if (QtUserWidgetsClassClass->objectName().isEmpty())
            QtUserWidgetsClassClass->setObjectName("QtUserWidgetsClassClass");
        QtUserWidgetsClassClass->resize(600, 400);
        centralWidget = new QWidget(QtUserWidgetsClassClass);
        centralWidget->setObjectName("centralWidget");
        label = new QLabel(centralWidget);
        label->setObjectName("label");
        label->setGeometry(QRect(270, 0, 71, 16));
        adminMoviesListWidget = new QListWidget(centralWidget);
        adminMoviesListWidget->setObjectName("adminMoviesListWidget");
        adminMoviesListWidget->setGeometry(QRect(10, 30, 256, 192));
        watchListWidget = new QListWidget(centralWidget);
        watchListWidget->setObjectName("watchListWidget");
        watchListWidget->setGeometry(QRect(340, 30, 256, 192));
        label_2 = new QLabel(centralWidget);
        label_2->setObjectName("label_2");
        label_2->setGeometry(QRect(120, 230, 41, 16));
        label_3 = new QLabel(centralWidget);
        label_3->setObjectName("label_3");
        label_3->setGeometry(QRect(460, 230, 49, 16));
        addWatchListButton = new QPushButton(centralWidget);
        addWatchListButton->setObjectName("addWatchListButton");
        addWatchListButton->setGeometry(QRect(90, 280, 101, 24));
        removeWatchListButton = new QPushButton(centralWidget);
        removeWatchListButton->setObjectName("removeWatchListButton");
        removeWatchListButton->setGeometry(QRect(420, 250, 131, 24));
        watchTrailerButton = new QPushButton(centralWidget);
        watchTrailerButton->setObjectName("watchTrailerButton");
        watchTrailerButton->setGeometry(QRect(100, 250, 75, 24));
        QtUserWidgetsClassClass->setCentralWidget(centralWidget);
        menuBar = new QMenuBar(QtUserWidgetsClassClass);
        menuBar->setObjectName("menuBar");
        menuBar->setGeometry(QRect(0, 0, 600, 22));
        QtUserWidgetsClassClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(QtUserWidgetsClassClass);
        mainToolBar->setObjectName("mainToolBar");
        QtUserWidgetsClassClass->addToolBar(Qt::TopToolBarArea, mainToolBar);
        statusBar = new QStatusBar(QtUserWidgetsClassClass);
        statusBar->setObjectName("statusBar");
        QtUserWidgetsClassClass->setStatusBar(statusBar);

        retranslateUi(QtUserWidgetsClassClass);

        QMetaObject::connectSlotsByName(QtUserWidgetsClassClass);
    } // setupUi

    void retranslateUi(QMainWindow *QtUserWidgetsClassClass)
    {
        QtUserWidgetsClassClass->setWindowTitle(QCoreApplication::translate("QtUserWidgetsClassClass", "QtUserWidgetsClass", nullptr));
        label->setText(QCoreApplication::translate("QtUserWidgetsClassClass", "USER MODE", nullptr));
        label_2->setText(QCoreApplication::translate("QtUserWidgetsClassClass", "movies", nullptr));
        label_3->setText(QCoreApplication::translate("QtUserWidgetsClassClass", "watchlist", nullptr));
        addWatchListButton->setText(QCoreApplication::translate("QtUserWidgetsClassClass", "add to watchlist", nullptr));
        removeWatchListButton->setText(QCoreApplication::translate("QtUserWidgetsClassClass", "remove from watchlist", nullptr));
        watchTrailerButton->setText(QCoreApplication::translate("QtUserWidgetsClassClass", "watch trailer", nullptr));
    } // retranslateUi

};

namespace Ui {
    class QtUserWidgetsClassClass: public Ui_QtUserWidgetsClassClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_QTUSERWIDGETSCLASS_H
