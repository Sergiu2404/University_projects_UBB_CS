#include "Buffer.h"

int Buffer::get()
{
    unique_lock<mutex> lock(mtx);

    // eait the notify from the producer
    while (queue.empty()) {
        cout << "[Thr " << this_thread::get_id() << "]: Buffer is empty\n";
        readyToReceiveProduct.wait(lock);
    }

    int value = queue.front();
    queue.pop();
    cout << "[Thr " << this_thread::get_id() << "] took " << value << " from buffer\n";

    readyToSendProduct.notify_one();

    return value;
}

void Buffer::put(int newValue)
{
    unique_lock<mutex> lock(mtx);

    // wait the notifie from the consumer
    while (queue.size() == 1) {
        cout << "[Thr " << this_thread::get_id() << "]: Buffer is full\n";
        readyToSendProduct.wait(lock);
    }

    queue.push(newValue);
    cout << "[Thr " << this_thread::get_id() << "] added " << newValue << " to buffer\n";

    readyToReceiveProduct.notify_one();
}
