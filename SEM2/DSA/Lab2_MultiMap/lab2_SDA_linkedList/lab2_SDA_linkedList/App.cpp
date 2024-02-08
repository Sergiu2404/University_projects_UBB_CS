#include <iostream>
#include "MultiMap.h"
#include "ExtendedTest.h"
#include "ShortTest.h"
#include "MultiMapIterator.h"
#include <cassert>

using namespace std;


int main() {


	testAll();
	MultiMap m;
	m.add(1, 100);
	m.add(2, 200);
	m.add(3, 300);
	m.add(1, 500);
	m.add(2, 600);
	m.add(4, 800);
	MultiMapIterator im = m.iterator();
	im.first();
	while (im.valid()) {
		cout << im.getCurrent().second << endl;
		im.next();
	}
	im.first();
	im.iteratorKSteps(4);
	assert(im.getCurrent().second == 600);
	try {
		im.iteratorKSteps(3);
		assert(false);
	}
	catch(exception& e){
		assert(true);
	}

	testAllExtended();
	cout << "End" << endl;
	system("pause");

}
