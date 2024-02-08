#include <stdlib.h>
#include "tests.h"
#include "repository.h"
#include "country.h"
#include "dynamicArray.h"
#include <crtdbg.h>
#include <assert.h>
#include "repository.h"

void testCreateDynamicArray()
{	
    DynamicArray* dynamicArray = createDynamicArray(100);
    assert(dynamicArray->count == 0);
    assert(dynamicArray->max_length == 100);
    destroy(dynamicArray);
}

void test_addTElem()
{
    Country* country = createCountry("a","b",1000);
    DynamicArray* array = createDynamicArray(5);
    add_TElem(array, country);
    assert(strcmp(array->data[0].name, "a") == 0);
    assert(strcmp(array->data[0].continent, "b") == 0);
    assert(array->data[0].population == 1000);
    destroy(array);
}

void test_delete_TELEM()
{
    Country* country1 = createCountry("aa", "bb", 2000);
    Country* country2 = createCountry("aaa", "bbb", 3000);
    DynamicArray* array = createDynamicArray(5);
    add_TElem(array, country1);
    add_TElem(array, country2);
    assert(strcmp(array->data[0].name, "aa") == 0);
    assert(strcmp(array->data[0].continent, "bb") == 0);
    assert(array->data[0].population == 2000);
    delete_TElem(array, 0);
    assert(strcmp(array->data[0].name, "aaa") == 0);
    assert(strcmp(array->data[0].continent, "bbb") == 0);
    assert(array->data[0].population == 3000);
    destroy(array);
}


void test_update_TElem()
{
    Country* country1 = createCountry("aa", "bb", 2000);
    Country* country2 = createCountry("aaa", "bbb", 3000);
    DynamicArray* array = createDynamicArray(5);
    add_TElem(array, country1);
    assert(strcmp(array->data[0].name, "aa") == 0);
    assert(strcmp(array->data[0].continent, "bb") == 0);
    assert(array->data[0].population == 2000);
    update_TElem(array, country1, 0);
    destroy(array);
}





void test_createRepo()
{
    DynamicArray* DA = createDynamicArray(10);
    Repository* repo = createRepository(*DA);
    initialiseRepository(repo);
    addCountry(repo, "Romania", "Europe", 19000000);
    addCountry(repo, "Bulgaria", "Europe", 8000000);
    
    assert(strcmp(repo->array->data[0].name, "Romania") == 0);
    assert(strcmp(repo->array->data[0].continent, "Europe") == 0);
    assert(repo->array->data[0].population == 19000000);

    destroyRepository(repo);
    destroy(DA);
}