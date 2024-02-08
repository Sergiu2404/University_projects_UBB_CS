
#pragma once
#include <string.h>
#include <stdio.h>
#include <stdlib.h>

typedef struct
{
    char* name;
    char* continent;
    int population;
    
} Country;



Country* createCountry(char* name, char* continent, int population);

void destroyCountry(Country* country);
char* getName(Country* country);
char* getContinent(Country* country);

int getPopulation(Country* country);


void transformToString(Country country, char stringCountry[]);