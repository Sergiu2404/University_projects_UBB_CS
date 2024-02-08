#include "SortedBagIterator.h"
#include "SortedBag.h"
#include <exception>
#include <stdexcept>
#include <stack>

using namespace std;

SortedBagIterator::SortedBagIterator(const SortedBag& b) : bag(b) {
	//TODO - Implementation
	this->currentNode = nullptr;
	this->first();
	//this->init();
}
//Theta(1)

TComp SortedBagIterator::getCurrent() {
	//TODO - Implementation
	if (!valid())
		throw std::exception("Exception");
	//return NULL_TCOMP;
	return currentNode->element;
}
//Theta(1)

bool SortedBagIterator::valid() {
	//TODO - Implementation
	return currentNode != nullptr;
}
//theta(1)

void SortedBagIterator::next() {
	//TODO - Implementation
	// 
	if (!valid())
		throw std::runtime_error("Invalid iterator!");

	// If there is a right child, go to its leftmost descendant
	if (currentNode->right != nullptr) {
		currentNode = currentNode->right;
		while (currentNode->left != nullptr)
			currentNode = currentNode->left;
	}
	else {
		// Otherwise, move upwards until finding a node
		// where currentNode is on the left side
		while (currentNode->parent != nullptr && currentNode->parent->right == currentNode)
			currentNode = currentNode->parent;
		currentNode = currentNode->parent;
	}
	//if (!valid())
	//	throw std::exception("Exception");

	//stack<Node<TElem>*> stiva;
	//this->first();

	//while (currentNode != nullptr)
	//{
	//	stiva.push(currentNode);
	//	currentNode = currentNode->left;
	//}
	//while (!stiva.empty())
	//{
	//	currentNode = stiva.top();
	//	currentNode = currentNode->right;
	//	while (currentNode != nullptr)
	//	{
	//		stiva.push(currentNode);
	//		currentNode = currentNode->left;
	//	}
	//}
	//if (!valid())
	//	throw std::exception("Exception");
	//if (currentNode->right != nullptr)
	//{
	//	//search the smallest node greater than current node
	//	currentNode = currentNode->right;
	//	while (currentNode->left != nullptr)
	//		currentNode = currentNode->left;
	//	
	//}
	//else
	//{
	//	//search while the current node is the right child of its parent
	//	while (currentNode->parent != nullptr && currentNode == currentNode->parent->right)
	//		currentNode = currentNode->parent;

	//	currentNode = currentNode->parent;
	//}
}
//Theta(nr of levels)


void SortedBagIterator::first() {
	//TODO - Implementation
	this->currentNode = this->bag.root;
}
//Theta(1)

