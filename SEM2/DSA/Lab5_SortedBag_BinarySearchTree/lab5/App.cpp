#include "SortedBag.h"
#include "SortedBagIterator.h"
#include <iostream>
#include "ShortTest.h"
#include "ExtendedTest.h"
#include <cassert>

using namespace std;


bool first_relation(TComp e1, TComp e2) {
	return e1 <= e2;
}


int main() {

	SortedBag sb(first_relation);
	sb.add(5);
	sb.add(6);
	sb.add(0);
	sb.add(7);
	sb.add(11);
	sb.add(10);
	sb.add(8);

	assert(sb.size() == 7);
	assert(sb.elementsWithThisFrequency(1) == 7);

	try {
		sb.elementsWithThisFrequency(-1);
		assert(false);
	}
	catch (std::exception& e) {
		assert(true);
	}
	std::cout << "test elements with given frequency\n";

	testAll();
	testAllExtended();
	
	cout << "Test over" << endl;
	system("pause");
}
