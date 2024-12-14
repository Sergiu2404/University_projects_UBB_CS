#include <iostream>
#include <vector>
#include <cmath>
#include <thread>
#include <future>
#include <chrono>
#include <mpi.h>

using namespace std;


vector<double> addPolynomials(const vector<double>& a, const vector<double>& b) {
    size_t n = max(a.size(), b.size());
    vector<double> result(n, 0.0);
    for (size_t i = 0; i < a.size(); i++) result[i] += a[i];
    for (size_t i = 0; i < b.size(); i++) result[i] += b[i];
    return result;
}

vector<double> subtractPolynomials(const vector<double>& a, const vector<double>& b) {
    size_t n = max(a.size(), b.size());
    vector<double> result(n, 0.0);
    for (size_t i = 0; i < a.size(); i++) result[i] += a[i];
    for (size_t i = 0; i < b.size(); i++) result[i] -= b[i];
    return result;
}

vector<double> shiftPolynomial(const vector<double>& poly, int k) {
    vector<double> result(k, 0.0);
    result.insert(result.end(), poly.begin(), poly.end());
    return result;
}

// Karatsuba algorithm for polynomial multiplication (sequential)
vector<double> multiplyPolynomialsKaratsuba(const vector<double>& a, const vector<double>& b) {
    size_t n = max(a.size(), b.size());
    if (n == 1) return { a[0] * b[0] };

    int newSize = pow(2, ceil(log2(n)));
    vector<double> aPadded = a, bPadded = b;
    aPadded.resize(newSize, 0.0);
    bPadded.resize(newSize, 0.0);

    int half = newSize / 2;
    vector<double> aLow(aPadded.begin(), aPadded.begin() + half);
    vector<double> aHigh(aPadded.begin() + half, aPadded.end());
    vector<double> bLow(bPadded.begin(), bPadded.begin() + half);
    vector<double> bHigh(bPadded.begin() + half, bPadded.end());

    vector<double> ac = multiplyPolynomialsKaratsuba(aHigh, bHigh);
    vector<double> bd = multiplyPolynomialsKaratsuba(aLow, bLow);
    vector<double> ab_cd = multiplyPolynomialsKaratsuba(addPolynomials(aLow, aHigh), addPolynomials(bLow, bHigh));
    ab_cd = subtractPolynomials(ab_cd, ac);
    ab_cd = subtractPolynomials(ab_cd, bd);

    vector<double> result = addPolynomials(shiftPolynomial(ac, 2 * half), shiftPolynomial(ab_cd, half));
    result = addPolynomials(result, bd);
    result.resize(a.size() + b.size() - 1);
    return result;
}

// Parallel Karatsuba algorithm using threads
vector<double> multiplyPolynomialsKaratsubaParallel(const vector<double>& a, const vector<double>& b) {
    size_t n = max(a.size(), b.size());
    if (n == 1) return { a[0] * b[0] };

    int newSize = pow(2, ceil(log2(n)));
    vector<double> aPadded = a, bPadded = b;
    aPadded.resize(newSize, 0.0);
    bPadded.resize(newSize, 0.0);

    int half = newSize / 2;
    vector<double> aLow(aPadded.begin(), aPadded.begin() + half);
    vector<double> aHigh(aPadded.begin() + half, aPadded.end());
    vector<double> bLow(bPadded.begin(), bPadded.begin() + half);
    vector<double> bHigh(bPadded.begin() + half, bPadded.end());

    auto futureAC = async(launch::async, multiplyPolynomialsKaratsubaParallel, aHigh, bHigh);
    auto futureBD = async(launch::async, multiplyPolynomialsKaratsubaParallel, aLow, bLow);
    auto futureABCD = async(launch::async, [](const vector<double>& al, const vector<double>& ah,
        const vector<double>& bl, const vector<double>& bh) {
            return multiplyPolynomialsKaratsubaParallel(addPolynomials(al, ah), addPolynomials(bl, bh));
        }, aLow, aHigh, bLow, bHigh);

    vector<double> ac = futureAC.get();
    vector<double> bd = futureBD.get();
    vector<double> ab_cd = futureABCD.get();
    ab_cd = subtractPolynomials(ab_cd, ac);
    ab_cd = subtractPolynomials(ab_cd, bd);

    vector<double> result = addPolynomials(shiftPolynomial(ac, 2 * half), shiftPolynomial(ab_cd, half));
    result = addPolynomials(result, bd);
    result.resize(a.size() + b.size() - 1);
    return result;
}

// MPI Polynomial Multiplication
void mpiPolynomialMultiplication(const vector<double>& a, const vector<double>& b, bool useKaratsuba, int rank, int size) {
    vector<double> localResult;
    if (rank == 0) {
        int n = a.size(), m = b.size();
        MPI_Bcast(&n, 1, MPI_INT, 0, MPI_COMM_WORLD);
        MPI_Bcast(&m, 1, MPI_INT, 0, MPI_COMM_WORLD);
        MPI_Bcast(const_cast<double*>(a.data()), n, MPI_DOUBLE, 0, MPI_COMM_WORLD);
        MPI_Bcast(const_cast<double*>(b.data()), m, MPI_DOUBLE, 0, MPI_COMM_WORLD);
        localResult = useKaratsuba ? multiplyPolynomialsKaratsuba(a, b) : multiplyPolynomialsKaratsuba(a, b);
    }
    else {
        int n, m;
        MPI_Bcast(&n, 1, MPI_INT, 0, MPI_COMM_WORLD);
        MPI_Bcast(&m, 1, MPI_INT, 0, MPI_COMM_WORLD);
        vector<double> aReceived(n), bReceived(m);
        MPI_Bcast(aReceived.data(), n, MPI_DOUBLE, 0, MPI_COMM_WORLD);
        MPI_Bcast(bReceived.data(), m, MPI_DOUBLE, 0, MPI_COMM_WORLD);
        localResult = useKaratsuba ? multiplyPolynomialsKaratsuba(aReceived, bReceived) : multiplyPolynomialsKaratsuba(aReceived, bReceived);
    }
    vector<double> result;
    if (rank == 0) result.resize(a.size() + b.size() - 1);
    MPI_Reduce(localResult.data(), result.data(), result.size(), MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);
    if (rank == 0) {
        cout << "MPI Result: ";
        for (double coeff : result) cout << coeff << " ";
        cout << endl;
    }
}

// Main function with timing
int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);
    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank); //ranking each processes by assigning some indexes
    MPI_Comm_size(MPI_COMM_WORLD, &size); //get nr of processes in the communicator

    vector<double> a = { 1, 2, 3 };
    vector<double> b = { 4, 5, 6 };

    if (rank == 0) {
        auto start = chrono::high_resolution_clock::now();
        vector<double> result = multiplyPolynomialsKaratsubaParallel(a, b);
        auto end = chrono::high_resolution_clock::now();
        chrono::duration<double> elapsed = end - start;
        cout << "Parallel Karatsuba Result: ";
        for (double coeff : result) cout << coeff << " ";
        cout << endl;
        cout << "Parallel Karatsuba Time: " << elapsed.count() << " seconds" << endl;
    }

    MPI_Barrier(MPI_COMM_WORLD);
    double mpiStartTime = MPI_Wtime();
    mpiPolynomialMultiplication(a, b, true, rank, size);
    double mpiEndTime = MPI_Wtime();
    if (rank == 0) cout << "MPI Karatsuba Time: " << (mpiEndTime - mpiStartTime) << " seconds" << endl;

    MPI_Finalize(); //for closing any comm channels
    return 0;
}
