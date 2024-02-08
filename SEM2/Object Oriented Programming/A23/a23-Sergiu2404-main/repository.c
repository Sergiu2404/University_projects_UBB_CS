#pragma once
#include "repository.h"
#include "country.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "dynamicArray.h"
#include "dynamicArrayOperations.h"

Repository* createRepository()
{
	DynamicArray* array = createDynamicArray(1);
	DynamicArrayOperations* undoArray = createDynamicArrayOperations(1);
	DynamicArrayOperations* redoArray = createDynamicArrayOperations(1);
	Repository* repository = (Repository*)malloc(sizeof(Repository));
	//make sure that the space was allocated
	if (repository == NULL)
		return NULL;

	repository->array = array;
	repository->undoArray = undoArray;
	repository->redoArray = redoArray;
	initialiseRepository(repository);
	return repository;
}

int checkExistenceOfACountry(Repository* repository, char* name)
{
	for (int i = 0; i < repository->array->count; i++)
		if ((strcmp(repository->array->data[i].name, name) == 0))
			return i;
	return -1;
}

int getLengthOfArray(Repository* repository)
{
	return repository->array->count;
}

int addCountry(Repository* repository, char* name,char* continent, int population)
{
	int position;
	position = checkExistenceOfACountry(repository, name);
	if (position == -1)
	{
		Country* country = createCountry(name, continent,population);
		
		add_TElem(repository->array,country);
		Operation* operation = (Operation*)malloc(sizeof(Operation));
		if (operation == NULL)
			return;
		strcpy(operation->command, "add");
		operation->country = *country;
		add_Operation(repository->undoArray, operation);

		
	}
}

int deleteCountry(Repository* repository, char* name)
{
	int position;
	position = checkExistenceOfACountry(repository, name);
	if (position == -1)
		return 0;
	else
	{
		Operation* operation = (Operation*)malloc(sizeof(Operation));
		if (operation == NULL)
			return;
		strcpy(operation->command, "delete");
		operation->country= repository->array->data[position];
		add_Operation(repository->undoArray, operation);
		
		delete_TElem(repository->array, position);
		
		
		
		return 1;
	}
}
	
int updateCountry(Repository* repository, char* name,int population)
{
	int position;
	position = checkExistenceOfACountry(repository, name);
	if (position == -1)
		return 0;
	else
	{
		Country* country = createCountry(name, getContinent(&(repository->array->data[position])), population);
		Operation* operation = (Operation*)malloc(sizeof(Operation));
		if (operation == NULL)
			return;
		strcpy(operation->command, "update");
		operation->country= repository->array->data[position];
		add_Operation(repository->undoArray, operation);
		update_TElem(repository->array, country, position);
		return 1;
	}
}

void destroyRepository(Repository* repository)
{
	if (repository == NULL)
		return;

	destroy(repository->array);
	destroyOperations(repository->undoArray);
	destroyOperations(repository->redoArray);

	free(repository);

}

void swap(int* xp, int* yp)
{
	int aux = *xp;
	*xp = *yp;
	*yp = aux;
}




void initialiseRepository(Repository* repository)
{
	addCountry(repository, "Romania","Europe",19000000);
	addCountry(repository, "Bulgaria","Europe",8000000);
	addCountry(repository, "Serbia","Europe",9000000);
	addCountry(repository, "Brazilia","America de Sud",225000000);
	addCountry(repository, "Camerun","Africa",40000000);
	addCountry(repository, "Ghana","Africa",50000000);
	addCountry(repository, "Thailand","Asia",22000000);
	addCountry(repository, "Irak","Asia",45000000);
	addCountry(repository, "Japan","Asia",80000000);
	addCountry(repository, "Germany","Europe",50000000);
	
}

DynamicArray* getCountryArray(Repository* repository)
{
	return repository->array;
}

void undoOperation(Repository* repository)
{
	if (strcmp(getCommand(&repository->undoArray->data[getOperationsCount(repository->undoArray) - 1]), "add") == 0) {
		Operation* operation = (Operation*)malloc(sizeof(Operation));
		if (operation == NULL)
			return;
	
		operation->country = repository->array->data[getLengthOfArray(repository) - 1];
		strcpy(operation->command, "add");
		add_Operation(repository->redoArray, operation);
		delete_Operation(repository->undoArray, getOperationsCount(repository->undoArray) - 1);
		
		delete_TElem(repository->array, getLengthOfArray(repository) - 1);
	}

	if (strcmp(getCommand(&repository->undoArray->data[getOperationsCount(repository->undoArray) - 1]), "delete") == 0) {
		Operation* operation = (Operation*)malloc(sizeof(Operation));
		if (operation == NULL)
			return;
		
		operation->country = repository->undoArray->data[getOperationsCount(repository->undoArray) - 1].country;
		strcpy(operation->command, "delete");
		add_Operation(repository->redoArray, operation);
		add_TElem(repository->array, &repository->undoArray->data[getOperationsCount(repository->undoArray) - 1].country);
		delete_Operation(repository->undoArray, getOperationsCount(repository->undoArray) - 1);
		
	}

	if (strcmp(getCommand(&repository->undoArray->data[getOperationsCount(repository->undoArray) - 1]), "update") == 0) {
		Operation* operation = (Operation*)malloc(sizeof(Operation));
		if (operation == NULL)
			return;
	
		int position = checkExistenceOfACountry(repository, repository->undoArray->data[getOperationsCount(repository->undoArray) - 1].country.name);
		operation->country = repository->array->data[position];
		strcpy(operation->command, "update");
		add_Operation(repository->redoArray, operation);
		update_TElem(repository->array, &repository->undoArray->data[getOperationsCount(repository->undoArray) - 1].country, position);
		delete_Operation(repository->undoArray, getOperationsCount(repository->undoArray) - 1);
	}


}

void redoOperation(Repository* repository)
{
	if (strcmp(getCommand(&repository->redoArray->data[getOperationsCount(repository->redoArray) - 1]), "add") == 0) {
		Operation* operation = (Operation*)malloc(sizeof(Operation));
		if (operation == NULL)
			return;
		operation->country = repository->redoArray->data[getOperationsCount(repository->redoArray) - 1].country;
		strcpy(operation->command, "add");
		add_Operation(repository->undoArray, operation);
		delete_Operation(repository->redoArray, getOperationsCount(repository->redoArray) - 1);

		add_TElem(repository->array, &operation->country);
	}

	if (strcmp(getCommand(&repository->redoArray->data[getOperationsCount(repository->redoArray) - 1]), "delete") == 0) {
		Operation* operation = (Operation*)malloc(sizeof(Operation));
		if (operation == NULL)
			return;
		
		operation->country = repository->redoArray->data[getOperationsCount(repository->redoArray) - 1].country;
		strcpy(operation->command, "delete");
		add_Operation(repository->undoArray, operation);
		int position = checkExistenceOfACountry(repository, operation->country.name);
		delete_TElem(repository->array,position);
		delete_Operation(repository->redoArray, getOperationsCount(repository->redoArray) - 1);

	}

	if (strcmp(getCommand(&repository->redoArray->data[getOperationsCount(repository->redoArray) - 1]), "update") == 0) {
		Operation* operation = (Operation*)malloc(sizeof(Operation));
		if (operation == NULL)
			return;
		
		operation->country = repository->redoArray->data[getOperationsCount(repository->redoArray) - 1].country;

		strcpy(operation->command, "update");
		add_Operation(repository->undoArray, operation);
		int position = checkExistenceOfACountry(repository, operation->country.name);
		update_TElem(repository->array, &operation->country, position);
		delete_Operation(repository->redoArray, getOperationsCount(repository->redoArray) - 1);
	}

}
