#include "SortedBagIterator.h"
#include "SortedBag.h"
#include <exception>



SortedBagIterator::SortedBagIterator(const SortedBag& b) : bag(b) { //this->bag=b;
	//TODO - Implementation
	this->currentElement = 0;
}

TComp SortedBagIterator::getCurrent() {
	//TODO - Implementation
	if (this->currentElement==this->bag.length)
		throw std::exception();
	
	return this->bag.elements[currentElement];
}

bool SortedBagIterator::valid() {
	//TODO - Implementation
	if (this->currentElement < this->bag.length)
		return true;
	return false;
}

void SortedBagIterator::next() {
	//TODO - Implementation
	if (this->currentElement==this->bag.length)
		throw std::exception();
	this->currentElement++;
}

void SortedBagIterator::first() {
	//TODO - Implementation
	this->currentElement = 0;
}

