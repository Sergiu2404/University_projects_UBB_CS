
#pragma once
#include "country.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>

Country* createCountry(char* name, char* continent, int population)
{
    Country* country = (Country*) malloc(sizeof(Country));
    if (country == NULL)
        return NULL;
    country->name = (char*) malloc(sizeof(char)*(strlen(name)+1));
    if (country->name == NULL)
        return NULL;
    strcpy(country->name, name); 
    country->continent = (char*)malloc(sizeof(char) * (strlen(continent) + 1));
    if (country->continent == NULL)
        return NULL;
    strcpy(country->continent, continent); 
    country->population = population; 
    return country;  
}


void destroyCountry(Country* country)
{
    if (country == NULL)
        return;

    free(country->name);
    free(country->continent);
    free(country);
}

char* getName(Country* country)
{
    if (country == NULL)
        return NULL;
    return country->name;
}

char* getContinent(Country* country)
{
    if (country == NULL)
        return -1;
    return country->continent;
}

int getPopulation(Country* country)
{
    if (country == NULL)
        return -1;
    return country->population;
}
void transformToString(Country country, char stringCountry[])
{
    sprintf(stringCountry, "%s is placed on %s and has %d people\n", country.name, country.continent, country.population);
}

