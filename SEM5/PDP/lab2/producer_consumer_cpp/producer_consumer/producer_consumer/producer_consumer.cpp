#include <iostream>
#include <vector>
#include <thread>
#include <mutex>
#include "Buffer.h"

using namespace std;

vector<int> vector1;
vector<int> vector2;

int scalar_product = 0;

void consumer(Buffer* buffer)
{
	for (int i = 0; i < vector1.size(); i++)
	{
		int value = buffer->get();
		scalar_product += value;
		cout << "Consumer: Added " << value << " to scalar product, current scalar product is " << scalar_product << "\n";
	}

	cout << "Final scalar product is: " << scalar_product << endl;
}

void producer(Buffer* buffer)   
{
	for (int i = 0; i < vector1.size(); i++)
	{
		int result = vector1[i] * vector2[i];
		buffer->put(result);
	}
}

int main()
{
	vector1 = { 1, 2, 3 };
	vector2 = { 5, 5, 5 };

	Buffer* buffer = new Buffer();

	thread Consumer(consumer, buffer);
	thread Producer(producer, buffer);

	Producer.join();
	Consumer.join();

	delete buffer;
	return 0;
}
