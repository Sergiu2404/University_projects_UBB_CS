#pragma once
#include "country.h"
#include "dynamicArray.h"
#include "dynamicArrayOperations.h"

typedef struct {
	DynamicArray* array;
	DynamicArrayOperations* undoArray;
	DynamicArrayOperations* redoArray;
	
}Repository;

/* create a repository
*/
Repository* createRepository();

int checkExistenceOfACountry(Repository* repository, char* name);

int getLengthOfArray(Repository* repository);


int addCountry(Repository* repository, char* name, char* country,int population);


int deleteCountry(Repository* repository, char* name);

int updateCountry(Repository* repository, char* name, int population);
void destroyRepository(Repository* repository);
void initialiseRepository(Repository* repository);


DynamicArray* getCountryArray(Repository* repository);

void undoOperation(Repository* repository);
void redoOperation(Repository* repository);
