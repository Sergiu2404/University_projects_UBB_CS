#include "service.h"
#include <stdlib.h>
#include <string.h>

Service* createService(Repository* repository)
{
    Service* service = (Service*)malloc(sizeof(Service));

    if (service == NULL)
        return NULL;
    service->repository = repository;
    return service;
}

void addCountryService(Service* service, char* name, char* continent, int population)
{
    addCountry(service->repository, name, continent,population);
}

DynamicArray* getCountryArrayService(Service* service)
{
    return getCountryArray(service->repository);
}

int deleteCountryService(Service* service, char* name)
{
    if (deleteCountry(service->repository, name) == 0)
        return 0;
    else
        return 1;
}

int updateCountryService(Service* service, char* name, int population)
{
    if (updateCountry(service->repository, name, population) == 0)
        return 0;
    else
        return 1;
}
void destroyService(Service* service)
{
    if (service == NULL)
        return;

    free(service);
}

DynamicArray* searchElementBySubStringService(Service* service, char* string)
{
    DynamicArray* new_array = getCountryArrayService(service);
        if (strlen(string) == 0)
            return new_array;
        else
        {
            DynamicArray* dynamicArray = createDynamicArray(1);
            for (int i = 0; i < new_array->count; i++)
            {
                if (strstr(new_array->data[i].name, string) != NULL)
                    add_TElem(dynamicArray, &new_array->data[i]);
            }

           
            return dynamicArray;
        }

    
}

void swap2(int* object1, int* object2)
{
    int aux = *object1;
    *object1 = *object2;
    *object2 = aux;
}




DynamicArray* searchElementByContinentService(Service* service, char* continent, int population)
{
    DynamicArray* newDynamicArray = getCountryArrayService(service);
    if (strlen(continent) == 0)
    {
        return newDynamicArray;
    }
    else
    {
        DynamicArray* dynamicArray = createDynamicArray(1);
        for (int i = 0; i < newDynamicArray->count; i++)
        {
            if (strcmp(newDynamicArray->data[i].continent, continent) ==0 && newDynamicArray->data[i].population > population)
                add_TElem(dynamicArray, &newDynamicArray->data[i]);
        }

        for (int i = 0; i < dynamicArray->count - 1; i++) {
            for (int j = i + 1; j < dynamicArray->count; j++) {
                if (dynamicArray->data[i].population > dynamicArray->data[j].population) {

                    swap2(&dynamicArray->data[i].name, &dynamicArray->data[j].name);
                    swap2(&dynamicArray->data[i].population, &dynamicArray->data[j].population);
                }
            }
        }

        return dynamicArray;
    }
}

void undoOperationService(Service* service)
{
    undoOperation(service->repository);
}

void redoOperationService(Service* service)
{
    redoOperation(service->repository);
}
