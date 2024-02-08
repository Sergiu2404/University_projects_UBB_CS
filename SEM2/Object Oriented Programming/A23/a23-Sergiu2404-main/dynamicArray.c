#pragma once
#include "dynamicArray.h"
#include <stdlib.h>

DynamicArray* createDynamicArray(int capacity)
{
    DynamicArray* dynamicArray = (DynamicArray*)malloc(sizeof(DynamicArray));
    // make sure that the space was allocated
    if (dynamicArray == NULL)
        return NULL;

    dynamicArray->max_length = capacity;
    dynamicArray->count = 0;

    // allocate space for the elements
    dynamicArray->data = (TElem*)malloc(capacity * sizeof(TElem));
    if (dynamicArray->data == NULL)
        return NULL;

    return dynamicArray;
}


void destroy(DynamicArray* dynamicArray)
{
    if (dynamicArray == NULL)
        return;

    free(dynamicArray->data);
    free(dynamicArray);
}

void resize(DynamicArray* dynamicArray)
{
    if (dynamicArray == NULL)
        return;

    dynamicArray->max_length *= 2;
    TElem* aux = realloc(dynamicArray->data, dynamicArray->max_length * sizeof(TElem));
    if (aux == NULL)
        return; // array cannot be resized (TODO - return error code and check when resizing)
    dynamicArray->data = aux;
}

void delete_TElem(DynamicArray* array, int position)
{
    for (int i = position; i <= array->count - 1; i++)
        array->data[i] = array->data[i + 1];

    array->count--;
}

void add_TElem(DynamicArray* array, TElem* TElem)
{
    if (array == NULL)
        return;
    if (array->data == NULL)
        return;

    if (array->max_length == array->count)
        resize(array);
    array->data[array->count] = *TElem;
    array->count++;

}

void update_TElem(DynamicArray* array, TElem* TElem, int position)
{
    array->data[position] = *TElem;
}