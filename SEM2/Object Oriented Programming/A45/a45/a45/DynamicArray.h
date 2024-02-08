#pragma once

template<typename T>
class DynamicArray {
private:
	int capacity, size;
	T* elements;
	void resize();

public:

	DynamicArray(int capacity=10);
	~DynamicArray();
	DynamicArray(const DynamicArray& dynamicArray);

	void add(T movie);
	void remove(int position);
	void update(int position, T element);
	
	//DynamicArray get_movies();
	T get_element(int position);
	int get_size();
};

template<typename T>
inline void DynamicArray<T>::resize()
{
	this->capacity *= 2;
	auto* newDynamicArray = new T[this->capacity];

	for (int i = 0; i < this->size; i++)
		newDynamicArray[i] = this->elements[i];

	delete[] this->elements;
	this->elements = newDynamicArray;
}


template<typename T>
DynamicArray<T>::DynamicArray(const DynamicArray<T>& dynamicArray) {
	this->size = dynamicArray.size;
	this->capacity = dynamicArray.capacity;
	this->elements = new T[this->capacity];
	for (int i = 0; i < this->size; i++)
		this->elements[i] = dynamicArray.elements[i];
}


template<typename T>
inline DynamicArray<T>::DynamicArray(int capacity)
{
	this->capacity = capacity;
	this->elements = new T[this->capacity];
	this->size = 0;
}

template<typename T>
inline DynamicArray<T>::~DynamicArray()
{
	delete[] this->elements;
}


//template<typename T>
//DynamicArray<T>& DynamicArray<T>::operator=(const DynamicArray<T>& v) {
//	if (this == &v)
//		return *this;
//
//	this->size = v.size;
//	this->capacity = v.capacity;
//
//	delete[] this->elems;
//	this->elems = new T[this->capacity];
//	for (int i = 0; i < this->size; i++)
//		this->elems[i] = v.elems[i];
//
//	return *this;
//}

template<typename T>
inline void DynamicArray<T>::add(T movie)
{
	if (this->size == this->capacity)
		this->resize();

	this->elements[this->size] = movie;
	this->size++;
}

template<typename T>
inline void DynamicArray<T>::remove(int position)
{
	for (int i = position; i < this->size-1; i++)
		this->elements[i] = this->elements[i + 1];
	this->size--;
}

template<typename T>
inline void DynamicArray<T>::update(int position, T element)
{
	this->elements[position] = element;
}

//template<typename T>
//T* DynamicArray<T>::get_movies()
//{
//	return this->elems;
//}

template<typename T>
inline T DynamicArray<T>::get_element(int position)
{
	return this->elements[position];
}


template<typename T>
inline int DynamicArray<T>::get_size()
{
	return this->size;
}
