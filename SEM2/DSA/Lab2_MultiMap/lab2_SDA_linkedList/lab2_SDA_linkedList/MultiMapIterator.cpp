#include "MultiMapIterator.h"
#include "MultiMap.h"


MultiMapIterator::MultiMapIterator(const MultiMap& c) : col(c) {
	//TODO - Implementation
	this->currentElement = this->col.head;
}

TElem MultiMapIterator::getCurrent() const {
	//TODO - Implementation
	if (this->valid())
		return this->currentElement->info;
	throw exception("Iterator is not valid");
}

bool MultiMapIterator::valid() const {
	//TODO - Implementation
	if (this->currentElement)
		return true;
	return false;
}

void MultiMapIterator::next() {
	//TODO - Implementation
	if (this->valid())
		this->currentElement = this->currentElement->next;
}

void MultiMapIterator::first() {
	//TODO - Implementation
	this->currentElement = this->col.head;
}

void MultiMapIterator::iteratorKSteps(int k)
{
	if (k == 0) throw exception();
	while (k--) {

		if (valid())
			next();
		else throw exception();
	}
}

