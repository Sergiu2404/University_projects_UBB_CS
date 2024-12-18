#include <iostream>      // for input/output operations
#include <vector>        // for vector container to store polynomials
#include <cmath>         // for mathematical operations like log2 and pow
#include <thread>        // for threading in parallel Karatsuba
#include <future>        // for handling async tasks in parallel Karatsuba
#include <chrono>        // for measuring execution time
#include <mpi.h>         // for MPI operations (Message Passing Interface)

using namespace std;

vector<double> addPolynomials(const vector<double>& a, const vector<double>& b) {
    size_t n = max(a.size(), b.size());   // determine the larger size of the two polynomials
    vector<double> result(n, 0.0);         // create a result vector filled with 0s
    for (size_t i = 0; i < a.size(); i++) result[i] += a[i]; // add the coefficients of a to result
    for (size_t i = 0; i < b.size(); i++) result[i] += b[i]; // add the coefficients of b to result
    return result;
}

vector<double> subtractPolynomials(const vector<double>& a, const vector<double>& b) {
    size_t n = max(a.size(), b.size());   // determine the larger size of the two polynomials
    vector<double> result(n, 0.0);         // create a result vector filled with 0s
    for (size_t i = 0; i < a.size(); i++) result[i] += a[i]; // copy coefficients of a to result
    for (size_t i = 0; i < b.size(); i++) result[i] -= b[i]; // subtract coefficients of b from result
    return result;
}

// shift a polyn by k positions (put zeros in front)
vector<double> shiftPolynomial(const vector<double>& poly, int k) {
    vector<double> result(k, 0.0);   // create a vector with k zeros
    result.insert(result.end(), poly.begin(), poly.end()); // append the original polynomial to the result
    return result;
}

vector<double> multiplyPolynomialsKaratsuba(const vector<double>& a, const vector<double>& b) {
    size_t n = max(a.size(), b.size()); // determine the largest polynomial size
    if (n == 1) return { a[0] * b[0] }; 

    // pad the polynomials to the next power of 2
    int newSize = pow(2, ceil(log2(n)));
    vector<double> aPadded = a, bPadded = b;
    aPadded.resize(newSize, 0.0);  // resize a to new size with 0 padding
    bPadded.resize(newSize, 0.0);  // resize b to new size with 0 padding

    int half = newSize / 2;  // divide the polynomials into two halves
    vector<double> aLow(aPadded.begin(), aPadded.begin() + half);    // low part of a
    vector<double> aHigh(aPadded.begin() + half, aPadded.end());      // high part of a
    vector<double> bLow(bPadded.begin(), bPadded.begin() + half);    // low part of b
    vector<double> bHigh(bPadded.begin() + half, bPadded.end());      // high part of b

    vector<double> ac = multiplyPolynomialsKaratsuba(aHigh, bHigh);
    vector<double> bd = multiplyPolynomialsKaratsuba(aLow, bLow);
    vector<double> ab_cd = multiplyPolynomialsKaratsuba(addPolynomials(aLow, aHigh), addPolynomials(bLow, bHigh)); // (aLow + aHigh) * (bLow + bHigh)

    ab_cd = subtractPolynomials(ab_cd, ac);
    ab_cd = subtractPolynomials(ab_cd, bd);

    // combine the results into the final polynomial
    vector<double> result = addPolynomials(shiftPolynomial(ac, 2 * half), shiftPolynomial(ab_cd, half)); // shift ac and ab_cd
    result = addPolynomials(result, bd);  // add bd to get the final result
    result.resize(a.size() + b.size() - 1);  // resize to remove extra zeroes
    return result;
}

// parallel Karatsuba algorithm using threads (async tasks)
vector<double> multiplyPolynomialsKaratsubaParallel(const vector<double>& a, const vector<double>& b) {
    size_t n = max(a.size(), b.size()); // determine the largest polynomial size
    if (n == 1) return { a[0] * b[0] }; // base case: single coefficient multiplication

    // pad the polynomials to the next power of 2
    int newSize = pow(2, ceil(log2(n)));
    vector<double> aPadded = a, bPadded = b;
    aPadded.resize(newSize, 0.0);  // resize a to new size with 0 padding
    bPadded.resize(newSize, 0.0);  // resize b to new size with 0 padding

    int half = newSize / 2;  // divide the polynomials into two halves
    vector<double> aLow(aPadded.begin(), aPadded.begin() + half);    // low part of a
    vector<double> aHigh(aPadded.begin() + half, aPadded.end());      // high part of a
    vector<double> bLow(bPadded.begin(), bPadded.begin() + half);    // low part of b
    vector<double> bHigh(bPadded.begin() + half, bPadded.end());      // high part of b

    // create async tasks for parallel computation
    auto futureAC = async(launch::async, multiplyPolynomialsKaratsubaParallel, aHigh, bHigh);   // async task for aHigh * bHigh
    auto futureBD = async(launch::async, multiplyPolynomialsKaratsubaParallel, aLow, bLow);     // async task for aLow * bLow
    auto futureABCD = async(launch::async, [](const vector<double>& al, const vector<double>& ah,
        const vector<double>& bl, const vector<double>& bh) {
            return multiplyPolynomialsKaratsubaParallel(addPolynomials(al, ah), addPolynomials(bl, bh));
        }, aLow, aHigh, bLow, bHigh);   // async task for (aLow + aHigh) * (bLow + bHigh)

    // wait for results from all async tasks
    vector<double> ac = futureAC.get();
    vector<double> bd = futureBD.get();
    vector<double> ab_cd = futureABCD.get();

    ab_cd = subtractPolynomials(ab_cd, ac);  // ab_cd - ac
    ab_cd = subtractPolynomials(ab_cd, bd);  // ab_cd - bd

    // combine the results into the final polynomial
    vector<double> result = addPolynomials(shiftPolynomial(ac, 2 * half), shiftPolynomial(ab_cd, half)); // shift ac and ab_cd
    result = addPolynomials(result, bd);  // add bd to get the final result
    result.resize(a.size() + b.size() - 1);  // resize to remove extra zeroes
    return result;
}

void mpiPolynomialMultiplication(const vector<double>& a, const vector<double>& b, bool useKaratsuba, int rank, int size) {
    vector<double> localResult;  // store the result on each process
    if (rank == 0) {
        int n = a.size(), m = b.size();  // get the sizes of the polynomials
        // Brodcast the polynomial sizes and coefficients to all proc
        MPI_Bcast(&n, 1, MPI_INT, 0, MPI_COMM_WORLD); //send size of first
        MPI_Bcast(&m, 1, MPI_INT, 0, MPI_COMM_WORLD); //send size of second
        MPI_Bcast(const_cast<double*>(a.data()), n, MPI_DOUBLE, 0, MPI_COMM_WORLD);
        MPI_Bcast(const_cast<double*>(b.data()), m, MPI_DOUBLE, 0, MPI_COMM_WORLD);

        // multiply the polynomials using Karatsuba algorithm
        localResult = useKaratsuba ? multiplyPolynomialsKaratsuba(a, b) : multiplyPolynomialsKaratsuba(a, b);
    }
    else {
        int n, m;

        MPI_Bcast(&n, 1, MPI_INT, 0, MPI_COMM_WORLD);
        MPI_Bcast(&m, 1, MPI_INT, 0, MPI_COMM_WORLD);
        vector<double> aReceived(n), bReceived(m);
        MPI_Bcast(aReceived.data(), n, MPI_DOUBLE, 0, MPI_COMM_WORLD);
        MPI_Bcast(bReceived.data(), m, MPI_DOUBLE, 0, MPI_COMM_WORLD);

        // multiply the polynomials using Karatsuba algorithm
        localResult = useKaratsuba ? multiplyPolynomialsKaratsuba(aReceived, bReceived) : multiplyPolynomialsKaratsuba(aReceived, bReceived);
    }

    vector<double> result;
    if (rank == 0) result.resize(a.size() + b.size() - 1);  // prepare the result vector on rank 0
    // reduce results from all processes to the main one (0)
    MPI_Reduce(localResult.data(), result.data(), result.size(), MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);

    // print the result from rank 0
    if (rank == 0) {
        cout << "MPI Result: ";
        for (double coeff : result) cout << coeff << " ";
        cout << endl;
    }
}

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);  // Initialize MPI
    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank); // get the rank of the current process
    MPI_Comm_size(MPI_COMM_WORLD, &size); // get the total number of processes

    vector<double> a = { 1, 2, 3 };  // sample polynomial a
    vector<double> b = { 4, 5, 6 };  // sample polynomial b

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

    // synchronize all processes before starting MPI mul
    MPI_Barrier(MPI_COMM_WORLD);

    double mpiStartTime = MPI_Wtime();
    mpiPolynomialMultiplication(a, b, true, rank, size);
    double mpiEndTime = MPI_Wtime();

    if (rank == 0) cout << "MPI Karatsuba Time: " << (mpiEndTime - mpiStartTime) << " seconds" << endl;

    MPI_Finalize(); // close comm channels
    return 0;
}
