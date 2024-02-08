#include <stdlib.h>
#include <crtdbg.h>
#include <iostream>
#include <string>
//#include <cstring>

#include "UI.h"
#include "Test.h"
#include "AdministratorRepository.h"
#include "Service.h"


int main() {
    
    Test test;
    test.run_tests();

    AdministratorRepository adminRepo = AdministratorRepository();
    UserRepository userRepo = UserRepository();
    Service service = Service{ adminRepo , userRepo };
    UI ui = UI{ service };
    ui.run_menu();

    /*adminRepo.add_adminRepo(Movie("1", "1", 0, 0, "..."));
    adminRepo.add_adminRepo(Movie("2", "2", 0, 0, "..."));
    adminRepo.add_adminRepo(Movie("1", "1", 0, 0, "..."));
    adminRepo.update_adminRepo(Movie("2", "2", 0, 0, "..."), Movie("200", "200", 0, 0, "..."));

    for (int i = 0; i < adminRepo.get_adminRepo().size(); i++)
        std::cout << adminRepo.get_adminRepo()[i].representation() << "\n";*/
    _CrtDumpMemoryLeaks();
    return 0;
}