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
#include <iostream>
#include <sstream>
#include <mpi.h>
#include <vector>

void printVector(std::vector<int> const& values, int me)
{
    std::stringstream ss;
    ss << "Me : " << me << ", values:";
    for (int v : values) ss << " " << v;
    std::cout << ss.str() << "\n";
}

int main()
{
    MPI_Init(0, 0);
    int me;
    int size;
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    MPI_Comm_rank(MPI_COMM_WORLD, &me);
    std::cout << "Hello, I am " << me << " out of " << size << "\n";

    std::vector<int> values;
    size_t const n = 25;
    if (me == 0) {
        values.reserve(n);
        for (size_t i = 0; i < n; ++i) values.push_back(i * i);
    }
    else {
        values.resize(n);
    }

    MPI_Bcast(values.data(), values.size(), MPI_INT, 0, MPI_COMM_WORLD);

    printVector(values, me);


    MPI_Finalize();
}