#pragma once
#include "repository.h"


typedef struct
{
    Repository* repository;
} Service;

Service* createService(Repository* repository);

void addCountryService(Service* service, char* name, char* continent, int population);


DynamicArray* getCountryArrayService(Service* service);

int deleteCountryService(Service* service, char* name);

int updateCountryService(Service* service, char* name, int population);
void destroyService(Service* service);
DynamicArray* searchElementBySubStringService(Service* service, char* string);
DynamicArray* searchElementByContinentService(Service* service, char* continent, int population);

void undoOperationService(Service* service);
void redoOperationService(Service* service);