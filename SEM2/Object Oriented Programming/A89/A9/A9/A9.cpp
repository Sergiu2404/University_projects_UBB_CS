#include "A9.h"
#include <QMessageBox>
#include "QtUserWidgetsClass.h"
#include "QtAdminWidgetsClass.h"

A9::A9(Service& service,QWidget *parent)
    :service{ service }, QMainWindow(parent)
{
    ui.setupUi(this);
    this->chooseType();

}

A9::~A9()
{}

void A9::chooseType()
{
    std::string option;
    QObject::connect(this->ui.startButton, &QPushButton::clicked, this, [this,option]() {

        std::string option = this->ui.startLineEdit->text().toStdString();
        if (option == "user")
        {
            //this->hide();
            //AdministratorRepository adminRepo;
            //UserRepository* userRepo = new CSVUserRepository{};
            //Service service{userRepo,adminRepo};

            //Service service;
            //QtUserWidgetsClass userWindow{ service };
            QtUserWidgetsClass* userWindow = new QtUserWidgetsClass{service};
            userWindow->populateMoviesList();
            userWindow->show();

         
        }
        else if (option == "admin") {
            //this->hide();
            //Service service;
            //QtAdminWidgetsClass adminWindow{ service };
           // AdministratorRepository adminRepo;
            //UserRepository* userRepo = new CSVUserRepository{};
           // Service service{ userRepo,adminRepo };
            QtAdminWidgetsClass* adminWindow = new QtAdminWidgetsClass{service};
            adminWindow->populateAdminList();
            adminWindow->show();
        }
        else {
            QMessageBox::critical(nullptr, "Error", "invalid option");
        }
    });
}

void A9::userWindow()
{
    Service service;
    QtUserWidgetsClass userWindow{ service };
    userWindow.show();
}

void A9::adminWindow()
{
    Service service;
    QtAdminWidgetsClass adminWindow{ service };
    adminWindow.show();
}
