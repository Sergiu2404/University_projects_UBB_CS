#include "Player.h"
#include "Repo.h"
#include "Service.h"
#include "UI.h"
#include "Tests.h"

int main()
{
	Tests tests;
	tests.test_all();
	/*Player p1 = Player("a", "b", "c", 2000);
	cout << p1.representation();*/
	Repo repo = Repo();
	Service service = Service{ repo };
	UI ui = UI{ service };
	ui.run_menu();
	return 0;
}