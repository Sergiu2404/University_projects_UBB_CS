#pragma once

#include <mutex>
#include <condition_variable>
#include <queue>
#include <iostream>

using namespace std;

class Buffer {
private:
	condition_variable readyToSendProduct;
	condition_variable readyToReceiveProduct;
	mutex mtx;
	queue<int> queue;

public:
	Buffer() {}

	int get();
	void put(int newValue);
};
