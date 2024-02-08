#include "SortedBag.h"
#include "SortedBagIterator.h"
#include <exception>

Node<TElem>* SortedBag::createNode(TElem& element)
{
	Node<TElem>* node = new Node<TElem>;

	node->element = element;
	node->left = nullptr;
	node->right = nullptr;
	node->parent = nullptr;
	node->frequency = 1;

	return node;
}
//Theta(1)

SortedBag::SortedBag(Relation r) {
	//TODO - Implementation
	this->counter = 0;
	this->root = nullptr;
	this->relation = r;
}
//Theta(1)

void SortedBag::add(TComp e) {
	//TODO - Implementation
	if (this->root == nullptr) {
		this->root = createNode(e);
		this->counter++;
		return;
	}

	Node<TElem>* current = root;
	while (true) {
		if (relation(e, current->element)) {
			if (current->left == nullptr) {
				current->left = createNode(e);
				this->counter++;
				return;
			}
			current = current->left;
		}
		else if (relation(current->element, e)) {
			if (current->right == nullptr) {
				current->right = createNode(e);
				this->counter++;
				return;
			}
			current = current->right;
		}
		else {
			current->frequency++;
			this->counter++;
			return;
		}
	}
}
//BC: Theta(1)-root
//WC: Theta()

bool SortedBag::remove(TComp e) {
	//TODO - Implementation

	Node<TElem>* currentNode = root;
	Node<TElem>* parentNode = nullptr;

	while (currentNode != nullptr && currentNode->element != e) {
		parentNode = currentNode;
		if (relation(e, currentNode->element))
			currentNode = currentNode->left;
		else
			currentNode = currentNode->right;
	}

	if (currentNode == nullptr) //node not found
		return false;

	currentNode->frequency--;

	if (currentNode->frequency == 0) {
		//leaf
		if (currentNode->left == nullptr && currentNode->right == nullptr) {
			if (parentNode == nullptr)
				root = nullptr;
			else if (parentNode->left == currentNode)
				parentNode->left = nullptr;
			else
				parentNode->right = nullptr;

			delete currentNode;
		}
		//has both left and right children
		else if (currentNode->left != nullptr && currentNode->right != nullptr) {
			Node<TElem>* successorParent = currentNode;
			Node<TElem>* successorNode = currentNode->right;


			//searching for node with smallest value greater than current node
			while (successorNode->left != nullptr) {
				successorParent = successorNode;
				successorNode = successorNode->left;
			}

			currentNode->element = successorNode->element;
			currentNode->frequency = successorNode->frequency;

			if (successorParent->left == successorNode)
				successorParent->left = successorNode->right;
			else
				successorParent->right = successorNode->right;

			delete successorNode;
		}
		//has left child
		else if (currentNode->left != nullptr) {
			if (parentNode == nullptr)
				root = currentNode->left;
			else if (parentNode->left == currentNode)
				parentNode->left = currentNode->left;
			else
				parentNode->right = currentNode->left;

			delete currentNode;
		}
		//has right child
		else if (currentNode->right != nullptr) {
			if (parentNode == nullptr)
				root = currentNode->right;
			else if (parentNode->left == currentNode)
				parentNode->left = currentNode->right;
			else
				parentNode->right = currentNode->right;

			delete currentNode;
		}

		this->counter--;
		return true;
	}

	return false;
}
//BC: Theta(1)
//WC: Theta(number of elements from the left of the right element of the root)


bool SortedBag::search(TComp elem) const {
	//TODO - Implementation
	
	Node<TElem>* currentNode = root;

	while (currentNode != nullptr) {
		if (currentNode->element == elem) {
			return true;
		}
		else if (relation(elem, currentNode->element)) {
			currentNode = currentNode->left;
		}
		else {
			currentNode = currentNode->right;
		}
	}

	return false; // not found
}
//BC: Theta(1)-root
//WC: Theta(number of levels until the searched node)


void countOccurrences(Node<TElem>* node, TElem element, int& count){
	if (node != nullptr) {
		countOccurrences(node->left, element, count); //search in left
		if (node->element == element) {
			count += node->frequency;
		}
		countOccurrences(node->right, element, count);//search in right
	}
}
//Theta(number of nodes)

int SortedBag::nrOccurrences(TComp elem) const {
	//TODO - Implementation
	int count = 0;
	countOccurrences(root, elem, count);
	return count;
}
//Theta(number of nodes)



int SortedBag::size() const {
	//TODO - Implementation
	return this->counter;
}
//Theta(1)


bool SortedBag::isEmpty() const {
	//TODO - Implementation
	return this->counter==0;
}
//Theta(1)


int SortedBag::countElementsWithFrequency(Node<TElem>* node,int frequency) const
{
	if (node == nullptr)
		return 0;

	int count = 0;
	if (node->frequency == frequency)
		count++;

	count += countElementsWithFrequency(node->left, frequency);
	count += countElementsWithFrequency(node->right, frequency);

	return count;
}


int SortedBag::elementsWithThisFrequency(int frequency) const
{
	if (frequency <= 0)
		throw std::exception("exception");
	return countElementsWithFrequency(this->root, frequency);
}

//Theta()


SortedBagIterator SortedBag::iterator() const {
	return SortedBagIterator(*this);
}


SortedBag::~SortedBag() {
	//TODO - Implementation
}
