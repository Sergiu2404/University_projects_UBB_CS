#include "ui.h"
#include "dynamicArray.h"
#include "country.h"
#include "repository.h"
#include "service.h"
#include <string.h>
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>



Console* createConsole(Service* service)
{
    Console* console = (Console*)malloc(sizeof(Console));

    if (console == NULL)
        return NULL;

    console->service = service;

    return console;
}

void destroyConsole(Console* console)
{
    if (console == NULL)
        return;

    destroyRepository(console->service->repository);
    destroyService(console->service);
    

    free(console);
}
void menu()
{
    printf("MENU==================================================================================================================\n");
    printf("1. Print countries                                                                                                   =\n");
    printf("2. Add a country                                                                                                     =\n");
    printf("3: Delete a country                                                                                                  =\n");
    printf("4: Update a country                                                                                                  =\n");
    printf("5: search countriess by a given substring                                                                            =\n");
    printf("6: search countries by continent                                                                                     =\n");
    printf("7: undo                                                                                                              =\n"); 
    printf("8: redo                                                                                                              =\n");
    printf("9: exit                                                                                                              =\n");
    printf("Choose your option from above:                                                                                       =\n");
    printf("MENU==================================================================================================================");
}

void startMenu(Console* console)
{
    int option;



    while (1) {
        menu();
        printf("\ngive option number: ");
        scanf("%d", &option);
        getchar();
        if (option == 9) {
            return;
        }
        if (option == 1)
        {
            DynamicArray* arrayExample = getCountryArrayService(console->service);
            
            for (int i = 0; i < arrayExample->count; i++)
            {
                char buffer[100];
                transformToString(arrayExample->data[i], buffer);
                puts(buffer);
            }

        }
        if (option == 2)
        {
            char name[100];
            char continent[100];
            int population;
            printf("Choose a name: ");
            gets(name);
            printf("Choose a continent: ");
            gets(continent);
            printf("Choose a population: ");
            scanf("%d", &population);
            addCountryService(console->service,name,continent,population);
            
  
        }
        if (option == 3)
        {
            char name[100];
            printf("Choose a name: ");
            gets(name);

            deleteCountryService(console->service, name);
        }
        if (option == 4)
        {
            char name[100];
            int population;
            printf("Choose a name: ");
            gets(name);
            printf("Choose a new population: ");
            scanf("%d", &population);
            updateCountryService(console->service, name, population);
        }
        
        if (option == 5)
        {
            char string[101];
            printf("Choose a subString: ");
            //scanf("%s", string);
            gets(string);
            DynamicArray* new_array = searchElementBySubStringService(console->service, string);
            for (int i = 0; i < new_array->count; i++)
            {
                char buffer[100];
                transformToString(new_array->data[i], buffer);
                puts(buffer);
            }
        }
        if (option == 6)
        {
            char string[101];
            int population;
            printf("Choose a continent: ");
            gets(string);
            printf("Choose a minimum number of population and sort remained countries by population: ");
            scanf("%d", &population);
            
            DynamicArray* new_array = searchElementByContinentService(console->service, string,population);
            for (int i = 0; i < new_array->count; i++)
            {
                char buffer[100];
                transformToString(new_array->data[i], buffer);
                puts(buffer);
            }
        }
        if (option == 7)
        {
            undoOperationService(console->service);
           
        }

        if (option == 8)
        {
            redoOperationService(console->service);
        }

    }
}
