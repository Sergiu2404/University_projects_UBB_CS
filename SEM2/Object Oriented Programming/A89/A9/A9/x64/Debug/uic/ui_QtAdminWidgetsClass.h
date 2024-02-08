/********************************************************************************
** Form generated from reading UI file 'QtAdminWidgetsClass.ui'
**
** Created by: Qt User Interface Compiler version 6.5.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_QTADMINWIDGETSCLASS_H
#define UI_QTADMINWIDGETSCLASS_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QLabel>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QListWidget>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_QtAdminWidgetsClassClass
{
public:
    QWidget *centralWidget;
    QLabel *label;
    QListWidget *adminMoviesList;
    QLineEdit *titleLine;
    QLineEdit *genreLine;
    QLineEdit *yearLine;
    QLineEdit *likesLine;
    QLineEdit *trailerLine;
    QLabel *label_2;
    QLabel *label_3;
    QLabel *label_4;
    QLabel *label_5;
    QLabel *label_6;
    QLabel *label_7;
    QLineEdit *updateTitleLine;
    QLineEdit *updateGenreLine;
    QLineEdit *updateYearLine;
    QLineEdit *updateLikesLine;
    QLineEdit *updateTrailerLine;
    QLabel *label_8;
    QLabel *label_9;
    QLabel *label_10;
    QLabel *label_11;
    QLabel *label_12;
    QPushButton *addButton;
    QPushButton *removeButton;
    QPushButton *updateButton;
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *QtAdminWidgetsClassClass)
    {
        if (QtAdminWidgetsClassClass->objectName().isEmpty())
            QtAdminWidgetsClassClass->setObjectName("QtAdminWidgetsClassClass");
        QtAdminWidgetsClassClass->resize(600, 400);
        centralWidget = new QWidget(QtAdminWidgetsClassClass);
        centralWidget->setObjectName("centralWidget");
        label = new QLabel(centralWidget);
        label->setObjectName("label");
        label->setGeometry(QRect(230, 0, 131, 21));
        adminMoviesList = new QListWidget(centralWidget);
        adminMoviesList->setObjectName("adminMoviesList");
        adminMoviesList->setGeometry(QRect(10, 30, 256, 192));
        titleLine = new QLineEdit(centralWidget);
        titleLine->setObjectName("titleLine");
        titleLine->setGeometry(QRect(410, 30, 113, 21));
        genreLine = new QLineEdit(centralWidget);
        genreLine->setObjectName("genreLine");
        genreLine->setGeometry(QRect(410, 60, 113, 21));
        yearLine = new QLineEdit(centralWidget);
        yearLine->setObjectName("yearLine");
        yearLine->setGeometry(QRect(410, 90, 113, 21));
        likesLine = new QLineEdit(centralWidget);
        likesLine->setObjectName("likesLine");
        likesLine->setGeometry(QRect(410, 120, 113, 21));
        trailerLine = new QLineEdit(centralWidget);
        trailerLine->setObjectName("trailerLine");
        trailerLine->setGeometry(QRect(410, 150, 113, 21));
        label_2 = new QLabel(centralWidget);
        label_2->setObjectName("label_2");
        label_2->setGeometry(QRect(330, 30, 41, 20));
        label_3 = new QLabel(centralWidget);
        label_3->setObjectName("label_3");
        label_3->setGeometry(QRect(330, 60, 49, 16));
        label_4 = new QLabel(centralWidget);
        label_4->setObjectName("label_4");
        label_4->setGeometry(QRect(330, 90, 71, 16));
        label_5 = new QLabel(centralWidget);
        label_5->setObjectName("label_5");
        label_5->setGeometry(QRect(330, 120, 71, 16));
        label_6 = new QLabel(centralWidget);
        label_6->setObjectName("label_6");
        label_6->setGeometry(QRect(330, 150, 49, 16));
        label_7 = new QLabel(centralWidget);
        label_7->setObjectName("label_7");
        label_7->setGeometry(QRect(410, 190, 121, 16));
        updateTitleLine = new QLineEdit(centralWidget);
        updateTitleLine->setObjectName("updateTitleLine");
        updateTitleLine->setGeometry(QRect(410, 210, 113, 21));
        updateGenreLine = new QLineEdit(centralWidget);
        updateGenreLine->setObjectName("updateGenreLine");
        updateGenreLine->setGeometry(QRect(410, 230, 113, 21));
        updateYearLine = new QLineEdit(centralWidget);
        updateYearLine->setObjectName("updateYearLine");
        updateYearLine->setGeometry(QRect(410, 250, 113, 21));
        updateLikesLine = new QLineEdit(centralWidget);
        updateLikesLine->setObjectName("updateLikesLine");
        updateLikesLine->setGeometry(QRect(410, 270, 113, 21));
        updateTrailerLine = new QLineEdit(centralWidget);
        updateTrailerLine->setObjectName("updateTrailerLine");
        updateTrailerLine->setGeometry(QRect(410, 290, 113, 21));
        label_8 = new QLabel(centralWidget);
        label_8->setObjectName("label_8");
        label_8->setGeometry(QRect(330, 210, 41, 20));
        label_9 = new QLabel(centralWidget);
        label_9->setObjectName("label_9");
        label_9->setGeometry(QRect(330, 230, 49, 16));
        label_10 = new QLabel(centralWidget);
        label_10->setObjectName("label_10");
        label_10->setGeometry(QRect(330, 250, 71, 16));
        label_11 = new QLabel(centralWidget);
        label_11->setObjectName("label_11");
        label_11->setGeometry(QRect(330, 270, 71, 16));
        label_12 = new QLabel(centralWidget);
        label_12->setObjectName("label_12");
        label_12->setGeometry(QRect(330, 290, 49, 16));
        addButton = new QPushButton(centralWidget);
        addButton->setObjectName("addButton");
        addButton->setGeometry(QRect(10, 260, 75, 24));
        removeButton = new QPushButton(centralWidget);
        removeButton->setObjectName("removeButton");
        removeButton->setGeometry(QRect(100, 260, 75, 24));
        updateButton = new QPushButton(centralWidget);
        updateButton->setObjectName("updateButton");
        updateButton->setGeometry(QRect(190, 260, 75, 24));
        QtAdminWidgetsClassClass->setCentralWidget(centralWidget);
        menuBar = new QMenuBar(QtAdminWidgetsClassClass);
        menuBar->setObjectName("menuBar");
        menuBar->setGeometry(QRect(0, 0, 600, 22));
        QtAdminWidgetsClassClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(QtAdminWidgetsClassClass);
        mainToolBar->setObjectName("mainToolBar");
        QtAdminWidgetsClassClass->addToolBar(Qt::TopToolBarArea, mainToolBar);
        statusBar = new QStatusBar(QtAdminWidgetsClassClass);
        statusBar->setObjectName("statusBar");
        QtAdminWidgetsClassClass->setStatusBar(statusBar);

        retranslateUi(QtAdminWidgetsClassClass);

        QMetaObject::connectSlotsByName(QtAdminWidgetsClassClass);
    } // setupUi

    void retranslateUi(QMainWindow *QtAdminWidgetsClassClass)
    {
        QtAdminWidgetsClassClass->setWindowTitle(QCoreApplication::translate("QtAdminWidgetsClassClass", "QtAdminWidgetsClass", nullptr));
        label->setText(QCoreApplication::translate("QtAdminWidgetsClassClass", "ADMINISTRATOR MODE", nullptr));
        label_2->setText(QCoreApplication::translate("QtAdminWidgetsClassClass", "Title", nullptr));
        label_3->setText(QCoreApplication::translate("QtAdminWidgetsClassClass", "Genre", nullptr));
        label_4->setText(QCoreApplication::translate("QtAdminWidgetsClassClass", "Release year", nullptr));
        label_5->setText(QCoreApplication::translate("QtAdminWidgetsClassClass", "Likes number", nullptr));
        label_6->setText(QCoreApplication::translate("QtAdminWidgetsClassClass", "Trailer", nullptr));
        label_7->setText(QCoreApplication::translate("QtAdminWidgetsClassClass", "Text boxes for update", nullptr));
        label_8->setText(QCoreApplication::translate("QtAdminWidgetsClassClass", "Title", nullptr));
        label_9->setText(QCoreApplication::translate("QtAdminWidgetsClassClass", "Genre", nullptr));
        label_10->setText(QCoreApplication::translate("QtAdminWidgetsClassClass", "Release year", nullptr));
        label_11->setText(QCoreApplication::translate("QtAdminWidgetsClassClass", "Likes number", nullptr));
        label_12->setText(QCoreApplication::translate("QtAdminWidgetsClassClass", "Trailer", nullptr));
        addButton->setText(QCoreApplication::translate("QtAdminWidgetsClassClass", "add", nullptr));
        removeButton->setText(QCoreApplication::translate("QtAdminWidgetsClassClass", "remove", nullptr));
        updateButton->setText(QCoreApplication::translate("QtAdminWidgetsClassClass", "update", nullptr));
    } // retranslateUi

};

namespace Ui {
    class QtAdminWidgetsClassClass: public Ui_QtAdminWidgetsClassClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_QTADMINWIDGETSCLASS_H
