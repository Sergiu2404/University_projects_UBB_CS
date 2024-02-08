#include "operation.h"
#include <stdlib.h>
Operation* createOperation(char* command, Country country)
{
	Operation* operation = (Operation*)malloc(sizeof(Operation));
	if (operation == NULL)
		return NULL;

	
	strcpy(operation->command, command);
	operation->country = country;
}

char* getCommand(Operation* operation)
{
	return operation->command;
}

Country getCountry(Operation* operation)
{
	return operation->country;
}
