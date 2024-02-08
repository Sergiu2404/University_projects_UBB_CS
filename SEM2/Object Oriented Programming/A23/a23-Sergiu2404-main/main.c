#include <stdio.h>
#include "country.h"
#include "repository.h"
#include "dynamicArray.h"
#include "service.h"
#include "ui.h"
#include "tests.h"
#include <assert.h>
#include <crtdbg.h>


int main()
{
	Repository* repository = createRepository();
	Service* service = createService(repository);
	Console* ui = createConsole(service);
	
	
	//TESTS


	testCreateDynamicArray();
	test_addTElem();
	test_delete_TELEM();
	test_update_TElem();
	test_createRepo();
	


	startMenu(ui);
	destroyConsole(ui);
	_CrtDumpMemoryLeaks();
	return 0;
}