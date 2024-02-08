#include <stdlib.h>
#include <crtdbg.h>
#include <iostream>
#include <string>
//#include <cstring>

#include "UI.h"
#include "Tests.h"
#include "AdministratorRepository.h"
#include "Service.h"
#include <string>
#include "Exceptions.h"
#include "CSVUserRepository.h"
#include "HTMLUserRepository.h"
#include <iostream>
int main() {

    /*Test test;
    test.run_tests();*/

    std::string answer;

    while (true)
    {
        std::cout << "Choose type of file to store data(CSV/HTML): ";
        getline(std::cin, answer);

#define CSV "CSV"
#define HTML "HTML"

        if (answer != CSV && answer != HTML)
            std::cout << "Invalid input, try again\n";
        else
            break;
    }   


    try {
        if (answer == CSV) {
            AdministratorRepository adminRepo{"C:\\Users\\Sergiu\\Desktop\\OOP\\A7\\A7-8\\A7-8\\movies.txt"};
            UserRepository *userRepo = new CSVUserRepository();

            Service service = Service{ "C:\\Users\\Sergiu\\Desktop\\OOP\\A7\\A7-8\\A7-8\\movies.txt" , userRepo, adminRepo };
            UI ui = UI{ service };
            ui.run_menu();

            delete userRepo;
        }
        else {
            AdministratorRepository adminRepo{ "C:\\Users\\Sergiu\\Desktop\\OOP\\A7\\A7-8\\A7-8\\movies.txt" };
            UserRepository *userRepo = new HTMLUserRepository();//"C:\\Users\\Sergiu\\Desktop\\OOP\\A7\\A7-8\\A7-8\\watchList.html"
            Service service = Service{ "C:\\Users\\Sergiu\\Desktop\\OOP\\A7\\A7-8\\A7-8\\movies.txt", userRepo , adminRepo };
            UI ui = UI{ service };
            ui.run_menu();

            delete userRepo;
        }
    }
    catch (FileException& fe) {
        std::cout << fe.print_message();
    }

    /*adminRepo.add_adminRepo(Movie("1", "1", 0, 0, "..."));
    adminRepo.add_adminRepo(Movie("2", "2", 0, 0, "..."));
    adminRepo.add_adminRepo(Movie("1", "1", 0, 0, "..."));
    adminRepo.update_adminRepo(Movie("2", "2", 0, 0, "..."), Movie("200", "200", 0, 0, "..."));

    for (int i = 0; i < adminRepo.get_adminRepo().size(); i++)
        std::cout << adminRepo.get_adminRepo()[i].representation() << "\n";*/
    _CrtDumpMemoryLeaks();
    return 0;
}