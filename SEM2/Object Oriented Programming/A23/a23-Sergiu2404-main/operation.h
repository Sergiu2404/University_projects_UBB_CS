#pragma once
#include "country.h"

typedef struct {
	char command[20];
	Country country;
}Operation;

Operation* createOperation(char* command,Country country);

char* getCommand(Operation* operation);
Country getCountry(Operation* operation);