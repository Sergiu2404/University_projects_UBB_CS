#include "SortedBag.h"
#include "SortedBagIterator.h"
#include <exception>


SortedBag::SortedBag(Relation r) {
	//TODO - Implementation
	this->capacity = 2;
	this->length = 0;
	this->r = r;
	this->elements = new TElem(capacity);
}
	//O(1)

void SortedBag::add(TComp e) {
	//TODO - Implementation
	if (this->capacity == this->length)
		resize(2*this->capacity);
	//throw std::exception();
	int index = 0;
	int positionToAddElement = 0;
		
	while (index < this->length && this->r(this->elements[index],e))
		index++;
	positionToAddElement = index;
	this->length++; //increase length with 1

	int aux = this->length;
	while (aux>index) {
		this->elements[aux] = this->elements[aux-1];
		aux--;
	}

	this->elements[positionToAddElement] = e;
//theta(1) / O(length)


}


bool SortedBag::remove(TComp e) {
	//TODO - Implementation
	int index = 0;
	bool elementFound = false;
	int positionOfElement = 0;

	while (index < this->length && elementFound==false) {
		if (e == this->elements[index])
		{
			elementFound = true;
			positionOfElement = index;
		}
		index++;
	}
	
	if(elementFound==false)
		return false;
	else {
		for (int i = positionOfElement; i < this->length - 1; i++)
			this->elements[i] = this->elements[i + 1];
		this->length--;
		return true;
	}
}
	//O(1) / O(length)


bool SortedBag::search(TComp elem) const {
	//TODO - Implementation
	int index = 0;
	while (index < this->length) {
		if (this->elements[index] == elem)
			return true;
		index++;
	}
	return false;
}
	//O(1) / O(length)


int SortedBag::nrOccurrences(TComp elem) const {
	//TODO - Implementation
	int index = 0;
	int counter = 0;
	while (index < this->length) {
		if (this->elements[index] == elem)
			counter++;
		index++;
	}
	return counter;
	//O(length)
}
void SortedBag::addOccurences(int nr, TComp elem)
{
	if (search(elem) == false)
		add(elem);
	else {
		if (nr < 0)
			throw "Number is less than 0";
		else {
			int occurences = nrOccurrences(elem);
			nr = nr - occurences;
			while (nr) {
				add(elem); //add element nr-occurences times 
				nr--;
			}
		}
	}

}
	//O(nr*length)



int SortedBag::size() const {
	//TODO - Implementation
	return this->length;
}
	//O(1)


bool SortedBag::isEmpty() const {
	//TODO - Implementation
	if (this->length == 0)
		return true;
	return false;
}
	//O(1)


SortedBagIterator SortedBag::iterator() const {
	return SortedBagIterator(*this);
}

void SortedBag::resize(int newCapacity) {
	TElem* newElements = new TElem[newCapacity];

	for (int i = 0; i < this->capacity && i < this->length; i++)
		newElements[i] = this->elements[i];

	//delete[] this->elements;
	this->capacity = newCapacity;
	this->elements = newElements;

}
    //O(length)


SortedBag::~SortedBag() {
	//TODO - Implementation
	delete[] this->elements;
}
	//O(1)
