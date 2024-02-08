#pragma once
#include "operation.h"


typedef Operation TElem2;

typedef struct
{
    TElem2* data;
    int count;
    int max_length;

} DynamicArrayOperations;

///  With this function we create the dynamic array
/// \param capacity the capacity of the array
/// \return returns the dynamic array
DynamicArrayOperations* createDynamicArrayOperations(int capacity);

int getOperationsCount(DynamicArrayOperations* array);

/// With this function we destroy the dynamic array
/// \param array the dynamic array we are about to destroy
void destroyOperations(DynamicArrayOperations* array);

///  With this function we add a new element to the dynamic array
/// \param array the dynamic array to which we add the element
/// \param TElem represents the element we are about to add to the array
void add_Operation(DynamicArrayOperations* array, TElem2* TElem);

///  With this function we delete an element from the dynamic array
/// \param array the dynamic array from which we are about to delete an element
/// \param position represents the position of the element we want to delete
void delete_Operation(DynamicArrayOperations* array, int position);

/// With this function we update an element from the dynamic array
/// \param array the dynamic array to which we update an element
/// \param TElem the new element
/// \param position the position of the element
void update_Operation(DynamicArrayOperations* array, TElem2* TElem, int position);
