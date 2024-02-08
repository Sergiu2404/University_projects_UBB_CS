/********************************************************************************
** Form generated from reading UI file 'Test.ui'
**
** Created by: Qt User Interface Compiler version 6.5.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_TEST_H
#define UI_TEST_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QListWidget>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_TestClass
{
public:
    QWidget *centralWidget;
    QListWidget *shoppingList;
    QListWidget *categoryList;
    QPushButton *categoryButton;
    QLineEdit *filteredLineEdit;
    QPushButton *filterButton;
    QLineEdit *categoryLineEdit;
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *TestClass)
    {
        if (TestClass->objectName().isEmpty())
            TestClass->setObjectName("TestClass");
        TestClass->resize(600, 400);
        centralWidget = new QWidget(TestClass);
        centralWidget->setObjectName("centralWidget");
        shoppingList = new QListWidget(centralWidget);
        shoppingList->setObjectName("shoppingList");
        shoppingList->setGeometry(QRect(0, 0, 256, 192));
        categoryList = new QListWidget(centralWidget);
        categoryList->setObjectName("categoryList");
        categoryList->setGeometry(QRect(330, 0, 256, 192));
        categoryButton = new QPushButton(centralWidget);
        categoryButton->setObjectName("categoryButton");
        categoryButton->setGeometry(QRect(330, 230, 75, 24));
        filteredLineEdit = new QLineEdit(centralWidget);
        filteredLineEdit->setObjectName("filteredLineEdit");
        filteredLineEdit->setGeometry(QRect(0, 200, 113, 21));
        filterButton = new QPushButton(centralWidget);
        filterButton->setObjectName("filterButton");
        filterButton->setGeometry(QRect(0, 230, 91, 24));
        categoryLineEdit = new QLineEdit(centralWidget);
        categoryLineEdit->setObjectName("categoryLineEdit");
        categoryLineEdit->setGeometry(QRect(330, 200, 113, 21));
        TestClass->setCentralWidget(centralWidget);
        menuBar = new QMenuBar(TestClass);
        menuBar->setObjectName("menuBar");
        menuBar->setGeometry(QRect(0, 0, 600, 22));
        TestClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(TestClass);
        mainToolBar->setObjectName("mainToolBar");
        TestClass->addToolBar(Qt::TopToolBarArea, mainToolBar);
        statusBar = new QStatusBar(TestClass);
        statusBar->setObjectName("statusBar");
        TestClass->setStatusBar(statusBar);

        retranslateUi(TestClass);

        QMetaObject::connectSlotsByName(TestClass);
    } // setupUi

    void retranslateUi(QMainWindow *TestClass)
    {
        TestClass->setWindowTitle(QCoreApplication::translate("TestClass", "Test", nullptr));
        categoryButton->setText(QCoreApplication::translate("TestClass", "Show items", nullptr));
        filterButton->setText(QCoreApplication::translate("TestClass", "Show filtered", nullptr));
    } // retranslateUi

};

namespace Ui {
    class TestClass: public Ui_TestClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_TEST_H
