#include "Hospital.h"
#include "Neonatal.h"
#include "Surgery.h"
#include "Service.h"
#include "UI.h"


int main()
{
	

	Service service;
	UI ui = UI{ service };
	ui.runMenu();

	/*vector<Hospital*> hospitals=service.getAll();

	for(auto h:hospitals)
		cout << h->toString()<<"\n";*/
	return 0;
}