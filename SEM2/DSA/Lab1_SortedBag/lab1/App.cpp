#include "SortedBag.h"
#include "SortedBagIterator.h"
#include <iostream>
#include "ShortTest.h"
#include "ExtendedTest.h"
#include <assert.h>
#include <exception>

using namespace std;

bool relation4(TComp e1, TComp e2) {
	return e1 <= e2;
}

void testNewFunction() {
	cout << "Test addOccurences function... ";

	SortedBag sb(relation4);
	sb.add(5);
	sb.add(6);
	sb.add(0);
	sb.add(5);
	sb.add(10);
	sb.add(8);


	sb.addOccurences(10, 5);
	assert(sb.size() == 14);
	assert(sb.nrOccurrences(5) == 10);
	assert(sb.remove(5) == true);
	assert(sb.nrOccurrences(5) == 9);
	/*try {
		sb.addOccurences(-1, 5);
		assert(false);
	}
	catch(exception&){
		assert(true);
	}*/

}

int main() {
	testAll();
	testAllExtended();
	testNewFunction();
	
	cout << "Test over" << endl;
	system("pause");
}
