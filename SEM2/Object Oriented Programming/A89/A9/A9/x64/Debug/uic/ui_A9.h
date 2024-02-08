/********************************************************************************
** Form generated from reading UI file 'A9.ui'
**
** Created by: Qt User Interface Compiler version 6.5.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_A9_H
#define UI_A9_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QLabel>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_A9Class
{
public:
    QWidget *centralWidget;
    QLabel *label;
    QLineEdit *startLineEdit;
    QPushButton *startButton;
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *A9Class)
    {
        if (A9Class->objectName().isEmpty())
            A9Class->setObjectName("A9Class");
        A9Class->resize(600, 400);
        centralWidget = new QWidget(A9Class);
        centralWidget->setObjectName("centralWidget");
        label = new QLabel(centralWidget);
        label->setObjectName("label");
        label->setGeometry(QRect(20, 20, 111, 31));
        startLineEdit = new QLineEdit(centralWidget);
        startLineEdit->setObjectName("startLineEdit");
        startLineEdit->setGeometry(QRect(20, 50, 113, 21));
        startButton = new QPushButton(centralWidget);
        startButton->setObjectName("startButton");
        startButton->setGeometry(QRect(20, 80, 75, 24));
        A9Class->setCentralWidget(centralWidget);
        menuBar = new QMenuBar(A9Class);
        menuBar->setObjectName("menuBar");
        menuBar->setGeometry(QRect(0, 0, 600, 22));
        A9Class->setMenuBar(menuBar);
        mainToolBar = new QToolBar(A9Class);
        mainToolBar->setObjectName("mainToolBar");
        A9Class->addToolBar(Qt::TopToolBarArea, mainToolBar);
        statusBar = new QStatusBar(A9Class);
        statusBar->setObjectName("statusBar");
        A9Class->setStatusBar(statusBar);

        retranslateUi(A9Class);

        QMetaObject::connectSlotsByName(A9Class);
    } // setupUi

    void retranslateUi(QMainWindow *A9Class)
    {
        A9Class->setWindowTitle(QCoreApplication::translate("A9Class", "A9", nullptr));
        label->setText(QCoreApplication::translate("A9Class", "choose admin/user", nullptr));
        startButton->setText(QCoreApplication::translate("A9Class", "Go", nullptr));
    } // retranslateUi

};

namespace Ui {
    class A9Class: public Ui_A9Class {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_A9_H
