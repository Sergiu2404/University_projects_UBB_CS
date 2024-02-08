#pragma once
#include "country.h"
#include "repository.h"
#include "service.h"
#include <stdio.h>

typedef struct
{
    Service* service;
} Console;

Console* createConsole(Service* service);

void destroyConsole(Console* console);

void startMenu(Console* console);
