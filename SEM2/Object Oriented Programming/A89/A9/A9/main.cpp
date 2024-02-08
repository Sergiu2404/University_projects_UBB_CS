#include "A9.h"
#include <QtWidgets/QApplication>
#include "CSVUserRepository.h"
#include "Service.h"
int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    AdministratorRepository* adminRepo=new AdministratorRepository;
    UserRepository* userRepo = new CSVUserRepository;
    Service service{ userRepo,adminRepo };
    
    A9 w{ service };
    w.show();

    return a.exec();
}
