#pragma once
#include <iostream>
using namespace std;

//class Player {
//private:
//	string name, nationality, team;
//	int goals;
//
//public:
//	Player(string name, string nationality, string team, int goals) :name{ name }, nationality{ nationality }, team{ team }, goals{ goals } {};
//
//	string get_name();
//	string get_nationality();
//	string get_team();
//	int get_goals();
//
//	string representation();
//};
class Player {
private:
	string name, nationality, team;
	int goals;

public:
	Player(string name, string nationality, string team, int goals) :name{ name },
		nationality{ nationality }, team{ team }, goals{ goals } {};

	string get_name();
	string get_nationality();
	string get_team();

	int get_goals();
	string representation();
};