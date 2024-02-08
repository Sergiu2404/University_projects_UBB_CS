#include "Tests.h"
#include <iostream>
#include <cassert>
#include "Repo.h"
#include "Service.h"
using namespace std;

void Tests::test_all()
{
	test_add();
	test_sort();
}

void Tests::test_add()
{
	//test add in function repo
	Repo repo;

	Player player1 = Player("Neagu", "ROU", "CSM", 100);
	Player player2 = Player("Gulden", "NOR", "SWE", 80);
	Player player3 = Player("Gulden", "FRA", "CSM", 10);
	Player player4 = Player("Allison", "RUS", "CSM", 70);
	Player player5 = Player("Neagu", "ESP", "CSM", 55);

	repo.add(player1);
	repo.add(player5);

	assert(repo.get_players().size() == 1); //test if players with same name were added
	assert(repo.get_players()[0].get_goals() == 100);

	//test add in function service
	Service service;

	service.add(player2);
	service.add(player3);

	assert(service.get_players().size() == 1); //test if players with same name were added
	assert(service.get_players()[0].get_goals() == 80);
	cout << "add tested\n";
}

void Tests::test_sort()
{
	Service service;
	
	vector<Player> players;
	players.push_back(Player("Neagu", "ROU", "CSM", 100));
	players.push_back(Player("s", "ESP", "CSM", 55));

	vector<Player> sorted=service.get_players_team_test("CSM", players);

	assert(sorted.size() == 2); //test if players were retrieved
	assert(sorted[0].get_goals() == 55); //test if players were sorted
	cout << "sort tested\n";
}
