#include "MultiMap.h"
#include "MultiMapIterator.h"
#include <exception>
#include <iostream>

using namespace std;


//int MultiMap::hashFunction1(TKey key) const {
//	//return abs(key % this->capacity);
//	int m = this->capacity;
//	return abs(key % m);
//}
//
//int MultiMap::hashFunction2(TKey key) const {
//	//return abs(1 + (key % (this->capacity - 1)));
//	int m = this->capacity - 1;
//	return abs(1 + (key % m));
//}

int MultiMap::hashFunction1(TKey key) const {
	const int p = 2; 
	const int m = this->capacity;  
	int hashVal = 0;
	int pPow = 1;
	while (key > 0) {
		int digit = key % 10;
		key /= 10;
		hashVal = (hashVal + (digit * pPow) % m) % m;
		pPow = (pPow * p) % m;
	}
	return hashVal;
}

int MultiMap::hashFunction2(TKey key) const {
	const int p = 3; 
	const int m = capacity - 1;
	int hashVal = 0;
	int pPow = 1;
	while (key > 0) {
		int digit = key % 10;
		key /= 10;
		hashVal = (hashVal + (digit * pPow) % m) % m;
		pPow = (pPow * p) % m;
	}
	return hashVal + 1;
}







void MultiMap::resize()
{

	int newCapacity = 2 * capacity;
	Node* newHashTable = new Node[newCapacity];

	for (int i = 0; i < newCapacity; i++) {
		newHashTable[i].key = NULL_TVALUE;
		newHashTable[i].values = nullptr;
		newHashTable[i].capacity = 0;
		newHashTable[i].counter = 0;
		newHashTable[i].deleted = false;
	}

	for (int i = 0; i < capacity; i++) {
		if (hashTable[i].key != NULL_TVALUE && !hashTable[i].deleted) {
			TKey key = hashTable[i].key;
			int index = hashFunction1(key);
			int step = hashFunction2(key);

			while (newHashTable[index].key != NULL_TVALUE) {
				index = (index + step) % newCapacity;
			}

			newHashTable[index].key = key;
			newHashTable[index].counter = hashTable[i].counter;
			newHashTable[index].capacity = hashTable[i].capacity;
			newHashTable[index].values = new TValue[newHashTable[index].capacity];

			for (int j = 0; j < newHashTable[index].counter; j++) {
				newHashTable[index].values[j] = hashTable[i].values[j];
			}

			delete[] hashTable[i].values;
		}
	}
	delete[] hashTable;
	hashTable = newHashTable;
	capacity = newCapacity;
}
//BC: Theta(newCapacity_hashtable)
//WC: Theta(newCapacity_hashtable * size_array)


MultiMap::MultiMap() {
	//TODO - Implementation
	this->capacity = 16;
	this->counter = 0;
	this->hashTable = new Node[this->capacity];
	for (int i = 0; i < this->capacity; i++)
	{
		this->hashTable[i].key = NULL_TVALUE;
		this->hashTable[i].values = nullptr;
		this->hashTable[i].capacity = 0;
		this->hashTable[i].counter = 0;
		this->hashTable[i].deleted=false;
	}
}
//Theta(capacity_hashtable)


void MultiMap::add(TKey c, TValue v) {
	//TODO - Implementation
	if (this->counter >= 0.5 * this->capacity)
		this->resize();
	
	int hash1 = hashFunction1(c);
	int hash2 = hashFunction2(c);
	int index = hash1 % this->capacity;

	while (this->hashTable[index].key != NULL_TVALUE && this->hashTable[index].key != c) {
		index = (index + hash2) % this->capacity;
	}

	
	if (this->hashTable[index].key == c) {
		// add value to existing key
		if (this->hashTable[index].counter >= this->hashTable[index].capacity) {
			TValue* newValues = new TValue[2 * this->hashTable[index].capacity];

			for (int i = 0; i < this->hashTable[index].counter; i++) {
				newValues[i] = this->hashTable[index].values[i];
			}

			this->hashTable[index].capacity *= 2;
			delete[] this->hashTable[index].values;
			this->hashTable[index].values = newValues;
		}
		this->hashTable[index].values[this->hashTable[index].counter] = v;
		this->hashTable[index].counter++;
	}
	else {
		//add value to new key
		this->hashTable[index].key = c;
		this->hashTable[index].counter = 1;
		this->hashTable[index].values = new TValue[1];
		this->hashTable[index].values[0] = v;
		this->hashTable[index].capacity = 1;
		counter++;
	}
	
}
//BC: Theta(1)
//WC: Theta(size_dynamic_array)


bool MultiMap::remove(TKey c, TValue v) {
	//TODO - Implementation
	int hash1 = hashFunction1(c);
	int hash2 = hashFunction2(c);

	for (int i = 0; i < this->capacity; i++) {
		int index = (hash1 + i * hash2) % capacity;
		if (this->hashTable[index].key == c && !this->hashTable[index].deleted) {
			// key found
			TValue* values = this->hashTable[index] .values;
			int size = this->hashTable[index].counter;

			// remove value from array
			for (int j = 0; j < size; j++) {
				if (values[j] == v) {
					for (int k = j; k < size - 1; k++) {
						values[k] = values[k + 1];
					}
					this->hashTable[index].counter--;
					return true;
				}
			}
			return false; //value not found
		}
	}
	return  false; //key not found
}
//BC: Theta(capacity_hashtable * 1)
//WC: Theta(capacity_hashtable * size_array)


TValue MultiMap::mostFrequent() const {
	TValue mostFrequentValue = NULL_TVALUE;
	int maxFrequency = 0;

	for (int i = 0; i < capacity; i++) {
		if (hashTable[i].key != NULL_TVALUE) {
			int frequency = hashTable[i].counter;

			if (frequency > maxFrequency) {
				maxFrequency = frequency;
				mostFrequentValue = hashTable[i].values[0];
			}
		}
	}

	return mostFrequentValue;
}
//Theta(capacity of hashtable)


vector<TValue> MultiMap::search(TKey c) const {
	//TODO - Implementation
	vector<TValue> result;

	int index = hashFunction1(c) % this->capacity;

	while (this->hashTable[index].key != NULL_TVALUE && this->hashTable[index].key != c) {
		index = (index + hashFunction2(c)) % this->capacity;
	}
	
	if (this->hashTable[index].key == NULL_TVALUE)
		return result; //empty vector
	else {
		for (int j = 0; j < this->hashTable[index].counter; j++)
			result.push_back(this->hashTable[index].values[j]);
	}

	return result;
}
//Theta(size of dynamic array of values from coresponding slot)


int MultiMap::size() const {
	//TODO - Implementation
	int count = 0;
	for (int i = 0; i < this->capacity; i++)
	{
		count += this->hashTable[i].counter; //sum up all the elements
	}
	return count;
}
//Theta(capacity of hashtable)


bool MultiMap::isEmpty() const {
	//TODO - Implementation
	return this->counter == 0;
}
//Theta(1)

MultiMapIterator MultiMap::iterator() const {
	return MultiMapIterator(*this);
}


MultiMap::~MultiMap() {
	//TODO - Implementation

	for (int i = 0; i < this->capacity; i++)
		if(this->hashTable[i].values!=nullptr)
		delete[] this->hashTable[i].values;

	delete[] this->hashTable;
}
//Theta(capacity of hashtable)

