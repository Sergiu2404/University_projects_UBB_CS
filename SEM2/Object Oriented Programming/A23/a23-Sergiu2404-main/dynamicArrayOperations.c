#pragma once
#include "dynamicArrayOperations.h"
#include <stdlib.h>





void resizeOperation(DynamicArrayOperations* array)
{
    if (array == NULL)
        return;

    array->max_length *= 2;
    TElem2* aux = realloc(array->data, array->max_length * sizeof(TElem2));
    if (aux == NULL)
        return; // array cannot be resized (TODO - return error code and check when resizing)
    array->data = aux;
}





DynamicArrayOperations* createDynamicArrayOperations(int capacity)
{
    DynamicArrayOperations* da = (DynamicArrayOperations*)malloc(sizeof(DynamicArrayOperations));
    // make sure that the space was allocated
    if (da == NULL)
        return NULL;

    da->max_length = capacity;
    da->count = 0;

    // allocate space for the elements
    da->data = (TElem2*)malloc(capacity * sizeof(TElem2));
    if (da->data == NULL)
        return NULL;

    return da;
}

int getOperationsCount(DynamicArrayOperations* array)
{
    return array->count;
}

void destroyOperations(DynamicArrayOperations* array)
{
    if (array == NULL)
        return;

    
    // free the space allocated for the planets
    free(array->data);
   // array->data = NULL;

    // free the space allocated for the dynamic array
    free(array);
}

void add_Operation(DynamicArrayOperations* array, TElem2* TElem)
{
    if (array == NULL)
        return;
    if (array->data == NULL)
        return;

    // resize the array, if necessary
    if (array->max_length == array->count)
        resizeOperation(array);
    array->data[array->count] = *TElem;
    array->count++;

}

void delete_Operation(DynamicArrayOperations* array, int position)
{
    for (int i = position; i <= array->count - 1; i++)
        array->data[i] = array->data[i + 1];

    array->count--;
}

void update_Operation(DynamicArrayOperations* array, TElem2* TElem, int position)
{
    array->data[position] = *TElem;
}
