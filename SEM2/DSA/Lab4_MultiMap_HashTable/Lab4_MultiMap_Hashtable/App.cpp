#include <iostream>
#include "MultiMap.h"
#include "ExtendedTest.h"
#include "ShortTest.h"
#include "MultiMapIterator.h"
#include <cassert>

using namespace std;


int main() {

	MultiMap m;
	m.add(1, 100);
	m.add(2, 200);
	m.add(3, 300);
	m.add(1, 500);
	m.add(4, 100);
	m.add(5, 200);
	m.add(6, 100);

	assert(m.size() == 7);
	assert(m.mostFrequent() == 100);
	std::cout << "mostFrequent() tested\n";

	testAll();
	testAllExtended();
	cout << "End" << endl;
	system("pause");

}
