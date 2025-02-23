//#include <mpi.h>
//#include <vector>
//#include <iostream>
//#include <algorithm>
//
//void mergeSortLocal(int* v, int dataSize) {
//    std::sort(v, v + dataSize);
//}
//
//void mergeParts(int* v, int dataSize, int halfSize) {
//    std::vector<int> temp(dataSize);
//    int i = 0, j = halfSize, k = 0;
//    while (i < halfSize && j < dataSize) {
//        if (v[i] < v[j]) temp[k++] = v[i++];
//        else temp[k++] = v[j++];
//    }
//    while (i < halfSize) temp[k++] = v[i++];
//    while (j < dataSize) temp[k++] = v[j++];
//    std::copy(temp.begin(), temp.end(), v);
//}
//
//void mergeSort(int* v, int dataSize, int myId, int nrProc) {
//    if (nrProc == 1 || dataSize <= 1) {
//        mergeSortLocal(v, dataSize);
//    }
//    else {
//        int halfSize = dataSize / 2;
//        int halfProc = nrProc / 2;
//        int child = myId + halfProc;
//        MPI_Ssend(&halfSize, 1, MPI_INT, child, 1, MPI_COMM_WORLD);
//        MPI_Ssend(&halfProc, 1, MPI_INT, child, 2, MPI_COMM_WORLD);
//        MPI_Ssend(v, halfSize, MPI_INT, child, 3, MPI_COMM_WORLD);
//        mergeSort(v + halfSize, dataSize - halfSize, myId, nrProc - halfProc);
//        MPI_Recv(v, halfSize, MPI_INT, child, 4, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
//        mergeParts(v, dataSize, halfSize);
//    }
//}
//
//void worker(int myId) {
//    MPI_Status status;
//    int dataSize, nrProc;
//    MPI_Recv(&dataSize, 1, MPI_INT, MPI_ANY_SOURCE, 1, MPI_COMM_WORLD, &status);
//    auto parent = status.MPI_SOURCE;
//    MPI_Recv(&nrProc, 1, MPI_INT, parent, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
//    std::vector<int> v(dataSize);
//    MPI_Recv(v.data(), dataSize, MPI_INT, parent, 3, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
//    mergeSort(v.data(), dataSize, myId, nrProc);
//    MPI_Ssend(v.data(), dataSize, MPI_INT, parent, 4, MPI_COMM_WORLD);
//}
//
//void master(int* v, int dataSize, int nrProc) {
//    mergeSort(v, dataSize, 0, nrProc);
//}
//
//int main(int argc, char** argv) {
//    MPI_Init(&argc, &argv);
//    int myId, nrProc;
//    MPI_Comm_rank(MPI_COMM_WORLD, &myId);
//    MPI_Comm_size(MPI_COMM_WORLD, &nrProc);
//
//    if (myId == 0) {
//        int dataSize = 10;
//        std::vector<int> v = { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
//        master(v.data(), dataSize, nrProc);
//        std::cout << "Sorted array: ";
//        for (int num : v) std::cout << num << " ";
//        std::cout << std::endl;
//    }
//    else {
//        worker(myId);
//    }
//
//    MPI_Finalize();
//    return 0;
//}



//#include <iostream>
//#include <mpi.h>
//#include <vector>
//#include <time.h>
//#include <stdint.h>
//#include <chrono>
//#include <ctime>   
//
//using namespace std;
//
//// Local merging of sorted sequences. Merges the sequences from
//// begin1 to end1 with that from begin2 to end2; the result is stored
//// starting at merged (which must have enough space)
//void merge(int* begin1, int* end1, int* begin2, int* end2, int* merged)
//{
//    int* curr1 = begin1;
//    int* curr2 = begin2;
//    while (curr1 < end1 || curr2 < end2) {
//        if (curr2 >= end2 || (curr1 < end1 && *curr1 < *curr2)) {
//            *merged = *curr1;
//            ++curr1;
//        }
//        else {
//            *merged = *curr2;
//            ++curr2;
//        }
//        ++merged;
//    }
//}
//
//// Recursively executes merge-sort for the sequence of sequence from in and of size n.
//// If nrProcs==1, all happens locally; otherwise, half of the input sequence is sent
//// to a child process for sorting and the other half is sorted by recursively calling
//// mergeSortRec()
//void mergeSortRec(size_t n, int* in, size_t me, size_t nrProcs)
//{
//    if (n <= 1) {
//        return;
//    }
//    size_t k = n / 2;
//    if (nrProcs >= 2) {
//        size_t child = me + nrProcs / 2;
//        int sizes[2];
//        sizes[0] = n - k;
//        sizes[1] = nrProcs - nrProcs / 2;
//        cout << "Worker " << me << ", sending to child " << child << ", part size = " << n - k << ", nrProcs = " << sizes[1] << "\n";
//        // first send the number of numbers to send and the number of processes that can be used by the child
//        MPI_Send(sizes, 2, MPI_INT, child, 1, MPI_COMM_WORLD);
//        MPI_Send(in + k, n - k, MPI_INT, child, 2, MPI_COMM_WORLD);
//        mergeSortRec(k, in, me, nrProcs / 2);
//        MPI_Status status;
//        MPI_Recv(in + k, n - k, MPI_INT, child, 3, MPI_COMM_WORLD, &status);
//        cout << "Worker " << me << ", received from child " << child << ", part size = " << n - k << "\n";
//    }
//    else {
//        mergeSortRec(k, in, me, 1);
//        mergeSortRec(n - k, in + k, me, 1);
//    }
//    int* buf = new int[n];
//    merge(in, in + k, in + k, in + n, buf);
//    for (size_t i = 0; i < n; ++i) in[i] = buf[i];
//    delete[] buf;
//}
//
//// Top function that calls mergeSortRec(). It must be called only on process 0
//void mergeSort(vector<int>& v, size_t nrProcs)
//{
//    mergeSortRec(v.size(), v.data(), 0, nrProcs);
//}
//
//// The main function to be executed on the worker. It receives a vector from the parent, sorts it,
//// possibly by using subordinate processes, and sends the result back to the parent.
//void mergeWorker(size_t me)
//{
//    // First, receive the number of numbers to sort and the number of sub-processes to use;
//    // the sender is considered the parent
//    int sizes[2];
//    MPI_Status status;
//    MPI_Recv(sizes, 2, MPI_INT, MPI_ANY_SOURCE, 1, MPI_COMM_WORLD, &status);
//    int parent = status.MPI_SOURCE;
//    size_t n = sizes[0];
//    size_t nrProcs = sizes[1];
//    cout << "Worker " << me << ", child of " << parent << ", part size = " << n << ", nrProcs = " << nrProcs << "\n";
//
//    // receive the data from the parent
//    vector<int> v;
//    v.resize(n);
//    MPI_Recv(v.data(), n, MPI_INT, parent, 2, MPI_COMM_WORLD, &status);
//    cout << "Worker " << me << ", received from parent " << parent << ", part size = " << n << "\n";
//
//    // do the local sorting
//    mergeSortRec(v.size(), v.data(), me, nrProcs);
//
//    // send back the result to the parent
//    cout << "Worker " << me << ", sending to parent " << parent << ", part size = " << n << "\n";
//    MPI_Ssend(v.data(), n, MPI_INT, parent, 3, MPI_COMM_WORLD);
//}
//
//void generate(vector<int>& v, size_t n)
//{
//    v.reserve(n);
//    for (size_t i = 0; i < n; ++i) {
//        // v.push_back(rand());
//        v.push_back((i * 101011) % 123456);
//    }
//}
//
//bool isSorted(vector<int> const& v)
//{
//    size_t const n = v.size();
//    for (size_t i = 1; i < n; ++i) {
//        if (v[i - 1] > v[i]) return false;
//    }
//    return true;
//}
//
//int main(int argc, char** argv)
//{
//    MPI_Init(0, 0);
//    int me;
//    int nrProcs;
//    MPI_Comm_size(MPI_COMM_WORLD, &nrProcs);
//    MPI_Comm_rank(MPI_COMM_WORLD, &me);
//
//    unsigned n = 4;
//    vector<int> v;
//    
//
//
//    if (me == 0) {
//        generate(v, n);
//        fprintf(stderr, "generated\n");
//    }
//
//
//    if (me == 0) {
//        mergeSort(v, nrProcs);
//    }
//    else {
//        mergeWorker(me);
//    }
//    if (me == 0) {
//        cout << ((n == v.size() && isSorted(v)) ? "ok" : "WRONG") << "\n";
//    }
//
//    MPI_Finalize();
//}




#include <mpi.h>
#include <vector>
#include <algorithm>
#include <iostream>

void mergeSortLocal(int* v, int dataSize) {
    std::sort(v, v + dataSize);
}

void mergeParts(int* v, int dataSize, int halfSize) {
    std::vector<int> temp(v, v + dataSize);
    std::merge(temp.begin(), temp.begin() + halfSize, temp.begin() + halfSize, temp.end(), v);
}

void mergeSort(int* v, int dataSize, int myId, int nrProc) {
    if (nrProc == 1) {
        mergeSortLocal(v, dataSize);
    }
    else {
        int halfLen = dataSize / 2;
        int halfProc = nrProc / 2;
        int child = myId + halfProc;

        MPI_Ssend(&halfLen, 1, MPI_INT, child, 1, MPI_COMM_WORLD);
        MPI_Ssend(&halfProc, 1, MPI_INT, child, 2, MPI_COMM_WORLD);
        MPI_Ssend(v, halfLen, MPI_INT, child, 3, MPI_COMM_WORLD);

        mergeSort(v + halfLen, dataSize - halfLen, myId, nrProc - halfProc);

        MPI_Recv(v, halfLen, MPI_INT, child, 4, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        mergeParts(v, dataSize, halfLen);
    }
}

void worker(int myId) {
    MPI_Status status;
    int dataSize, nrProc;
    MPI_Recv(&dataSize, 1, MPI_INT, MPI_ANY_SOURCE, 1, MPI_COMM_WORLD, &status);
    int parent = status.MPI_SOURCE;
    MPI_Recv(&nrProc, 1, MPI_INT, parent, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

    std::vector<int> v(dataSize);
    MPI_Recv(v.data(), dataSize, MPI_INT, parent, 3, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

    mergeSort(v.data(), dataSize, myId, nrProc);

    MPI_Ssend(v.data(), dataSize, MPI_INT, parent, 4, MPI_COMM_WORLD);
}

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int myId, nrProc;
    MPI_Comm_rank(MPI_COMM_WORLD, &myId);
    MPI_Comm_size(MPI_COMM_WORLD, &nrProc);

    if (myId == 0) {
        int dataSize = 17; // Example size
        std::vector<int> data = { 16, 14, 5, 6, 8, 7, 10, 9, 2, 4, 12, 11, 3, 1, 15, 17, 13 };

        mergeSort(data.data(), dataSize, myId, nrProc);

        std::cout << "Sorted array: ";
        for (int num : data) std::cout << num << " ";
        std::cout << std::endl;
    }
    else {
        worker(myId);
    }

    MPI_Finalize();
    return 0;
}
