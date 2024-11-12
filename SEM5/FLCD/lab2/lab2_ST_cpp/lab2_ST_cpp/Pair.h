#pragma once
using namespace std;
#include <string>

class Pair {
	private:
		int first;
		int second;
	public:
		int getFirst();
		int getSecond();

		Pair();
		Pair(int first, int second);

		string toString();
};