#include "MultiMap.h"
#include "MultiMapIterator.h"
#include <exception>
#include <iostream>
#include <vector>

using namespace std;


MultiMap::MultiMap() {
	//TODO - Implementation
	nrElements = 0;
	head = nullptr;    //{<key, {info, head, tail}>, next, prev}
	tail = nullptr;
}


void MultiMap::add(TKey c, TValue v) {
	//TODO - Implementation
	Node* node=this->head;
	Node* newNode=new Node;
	newNode->info.first = c;
	newNode->info.second = v;
	newNode->next = nullptr;
	
	if (this->isEmpty())
	{
		newNode->prev = newNode->next = nullptr;
		this->tail = this->head = newNode;
		this->nrElements++;
		return;
	}


	while (node->next) {
		node = node->next;
	}

	node->next = newNode;
	this->tail = newNode;
	node->next->prev = node;
	this->nrElements++;
}
//Worst: Theta(n)
//Best: Theta(1)

bool MultiMap::remove(TKey c, TValue v) {
	//TODO - Implementation
	if (this->isEmpty())
		return false;
	Node* current = this->head;
	/*if(current->info.first==c && current->info.second ==v)
		if (this->size() == 1)
		{
			this->head = this->tail=nullptr;
			delete current;
			this->nrElements = 0;
			return true;
		}
		else {
			this->head = current->next;
			this->head->prev = nullptr;
			delete current;
			this->nrElements--;
		}*/
	
	while (current!=nullptr)
	{
		if (current->info.first == c && current->info.second == v) {
			if (current == this->head) {
				this->head = current->next;
			}
			else {
				current->prev->next = current->next;
			}
			if (current == this->tail) {
				this->tail = current->prev;
			}
			else {
				current->next->prev = current->prev;
			}
			delete current;
			this->nrElements--;
			return true;
		}
		current = current->next;
	}
	/*if (current->info.first == c && current->info.second == v)
	{
		this->tail = current->prev;
		this->tail->next = nullptr;

		delete current;
		this->nrElements--;
		return true;
	}*/
	return false;
}
//Worst: Theta(n)
//Best: Theta(1)


vector<TValue> MultiMap::search(TKey c) const {
	//TODO - Implementation
	vector<TValue> result;

	Node* node=this->head;
	while (node) {
		if (node->info.first == c)
		{
			result.push_back(node->info.second);
		}
		node = node->next;
	}

	return result;
}
//Theta(n)


int MultiMap::size() const {
	//TODO - Implementation
	return nrElements;
}
//Theta(1)


bool MultiMap::isEmpty() const {
	//TODO - Implementation
	return nrElements == 0;
}
//Theta(1)

MultiMapIterator MultiMap::iterator() const {
	return MultiMapIterator(*this);
}




MultiMap::~MultiMap() {
	//TODO - Implementation
	delete[] this->elems;
}

