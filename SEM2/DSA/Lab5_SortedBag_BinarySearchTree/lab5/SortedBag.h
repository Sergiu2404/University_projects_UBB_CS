#pragma once
//DO NOT INCLUDE SORTEDBAGITERATOR

//DO NOT CHANGE THIS PART
typedef int TComp;
typedef TComp TElem;
typedef bool(*Relation)(TComp, TComp);
#define NULL_TCOMP -11111;

class SortedBagIterator;

template <typename TElem>
struct Node {
	TElem element;
	Node* left, * right,*parent;
	int frequency;
};


class SortedBag {
	friend class SortedBagIterator;

private:
	//TODO - Representation
	Node<TElem>* root;
	Relation relation;
	int counter;


	Node<TElem>* createNode(TElem& element);
	/*void destroyNode(Node<TElem>* node);
	bool insertNode(Node<TElem>* node,TElem& element);
	bool removeNode(Node<TElem>* node, TElem& element);
	Node<TElem>* findNode(Node<TElem>* node, TElem& element);
	int countFrequency(Node<TElem>* node, TElem& element);*/


public:
	//constructor
	SortedBag(Relation r);

	//adds an element to the sorted bag
	void add(TComp e);

	//removes one occurence of an element from a sorted bag
	//returns true if an eleent was removed, false otherwise (if e was not part of the sorted bag)
	bool remove(TComp e);

	//checks if an element appearch is the sorted bag
	bool search(TComp e) const;

	//returns the number of occurrences for an element in the sorted bag
	int nrOccurrences(TComp e) const;

	//returns the number of elements from the sorted bag
	int size() const;

	//returns an iterator for this sorted bag
	SortedBagIterator iterator() const;

	//checks if the sorted bag is empty
	bool isEmpty() const;

	int elementsWithThisFrequency(int frequency) const;
	int countElementsWithFrequency(Node<TElem>* node,int frequency) const;

	//destructor
	~SortedBag();
};