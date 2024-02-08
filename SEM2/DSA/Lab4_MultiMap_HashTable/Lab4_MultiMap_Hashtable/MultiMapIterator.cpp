#include "MultiMapIterator.h"
#include "MultiMap.h"
#include <stdexcept>


MultiMapIterator::MultiMapIterator(const MultiMap& c): col(c) {
	//TODO - Implementation
    this->first();
}
//Theta(capacity)

TElem MultiMapIterator::getCurrent() const{
	//TODO - Implementation
    if (!valid()) {
        return NULL_TELEM;
    }
    TValue* values = this->currentElement->values;
    int currentValue = this->currentElement->counter - 1;
    return TElem(this->currentElement->key, values[currentValue]);
	
}
//Theta(1)

bool MultiMapIterator::valid() const {
	//TODO - Implementation
    //return this->currentElement != nullptr;
    return this->currentElement != nullptr && this->col.size() > 0;
}
//Theta(1)

void MultiMapIterator::next() {
	//TODO - Implementation
    if (!valid()) {
        return;
    }

    if (this->currentElement->counter > 1) {
        this->currentElement->counter--;
    }
    else {
        int index = col.hashFunction1(this->currentElement->key);
        int step = col.hashFunction2(this->currentElement->key);

        this->currentElement = nullptr;
        int counter = 0;

        for (int i = (index + step) % col.capacity; i != index; i = (i + step) % col.capacity) {
            if (!col.hashTable[i].deleted && col.hashTable[i].key != NULL_TVALUE) {
                this->currentElement = &col.hashTable[i];
                break;
            }
            counter++;
            if (counter >= col.size()) {
                break;
            }
        }
    } 
}  
//BC: Theta(1)
//WC: Theta(capacity)


void MultiMapIterator::first() {
	//TODO - Implementation
	this->currentElement = nullptr;
	for (int i = 0; i < col.capacity; i++) {
		if (!col.hashTable[i].deleted && col.hashTable[i].key != NULL_TVALUE && col.hashTable[i].counter > 0) {
			this->currentElement = &col.hashTable[i];
			break;
		}
	}
}
//Theta(multimap_capacity)

