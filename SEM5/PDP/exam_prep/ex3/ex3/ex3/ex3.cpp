#include <iostream>
#include <vector>
#include <thread>
#include <functional>
#include "binary_tree_sum.cpp";
using namespace std;


// 2023 nr 2
// SUM OF ELEMS OF A MATRIX USING BINARY TREE
int sumSubrange(const std::vector<std::vector<int>>& matrix, int startRow, int endRow) {
    int sum = 0;
    for (int i = startRow; i < endRow; ++i) {
        for (int j = 0; j < matrix[i].size(); ++j) {
            sum += matrix[i][j];
        }
    }
    return sum;
}

int binaryTreeSum(const std::vector<std::vector<int>>& matrix, int startRow, int endRow) {
    if (startRow == endRow - 1) {
        return sumSubrange(matrix, startRow, endRow);
    }

    int midRow = (startRow + endRow) / 2;
    int leftSum = 0, rightSum = 0;

    std::thread leftThread([&]() { leftSum = binaryTreeSum(matrix, startRow, midRow); });
    std::thread rightThread([&]() { rightSum = binaryTreeSum(matrix, midRow, endRow); });

    leftThread.join();
    rightThread.join();

    return leftSum + rightSum;
}




// 2023 nr 3
struct Node {
    int value;
    Node* left;
    Node* right;

    Node(int val) : value(val), left(nullptr), right(nullptr) {}
};

Node* createTree(const vector<int>& vec, int start, int end) {
    if (start > end) return nullptr;

    int mid = (start + end) / 2;
    Node* root = new Node(vec[mid]);

    root->left = createTree(vec, start, mid - 1);
    root->right = createTree(vec, mid + 1, end);

    return root;
}

// compute the scalar prod (dot prod) using the binary tree
int computeDotProduct(Node* tree1, Node* tree2) {
    if (tree1 == nullptr || tree2 == nullptr) return 0;

    // recursively calculate the dot product by traversing the tree
    int leftProduct = computeDotProduct(tree1->left, tree2->left);
    int rightProduct = computeDotProduct(tree1->right, tree2->right);

    int currentProduct = tree1->value * tree2->value;

    return leftProduct + rightProduct + currentProduct;
}





//2023 nr 7
#include <cstdlib>
#include <chrono>
#include <sstream>
#include <mutex>


//void partialDotProduct(const vector<int>& a, const vector<int>& b, int start, int end, long long& partialSum) {
//    partialSum = 0;
//    for (int i = start; i < end; ++i) {
//        partialSum += a[i] * b[i];
//    }
//}
vector<int> partialSums;
mutex mtx;

void partialDotProduct(vector<int> a, vector<int> b, int start, int end)
{
    int current_sum = 0;
    for (int i = start; i < end; i++)
    {
        current_sum += a[i] * b[i];
    }
    cout << current_sum << " from thread " << this_thread::get_id() << "| ";

    mtx.lock();
    partialSums.push_back(current_sum);
    mtx.unlock();
}





int main(int argc, char* argv[]) {
    // 2023 nr 2
    /*std::vector<std::vector<int>> matrix = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9},
        {10, 11, 12}
    };
    int totalSum = binaryTreeSum(matrix, 0, matrix.size());
    std::cout << "Total sum: " << totalSum << std::endl;*/



    //2023 nr 3
    /*vector<int> vector1 = { 1, 2, 3, 4 };
    vector<int> vector2 = { 5, 6, 7, 8 };

    Node* tree1 = createTree(vector1, 0, vector1.size() - 1);
    Node* tree2 = createTree(vector2, 0, vector2.size() - 1);

    int result = computeDotProduct(tree1, tree2);
    cout << "The scalar prod of the vectors is: " << result << endl;*/




    // 2023 nr 7
    //int numThreads = 3;
    //int vectorSize = 8;

    //vector<int> a{ 1, 2, 3, 4, 5, 6, 7, 8 };
    //vector<int> b{ 0, 1, 2, 3, 4, 5, 6, 7 };

    //vector<thread> threads;

    //int chunkSize = vectorSize / numThreads;
    //int remainder = vectorSize % numThreads;

    //int startIdx = 0;
    //for (int i = 0; i < numThreads; ++i) {
    //    int endIdx = startIdx + chunkSize + (i < remainder ? 1 : 0); // Distribute remainder fairly
    //    threads.emplace_back(partialDotProduct, a, b, startIdx, endIdx);
    //    startIdx = endIdx;
    //}

    //for (auto& t : threads) {
    //    t.join();
    //}

    //int dotProduct = 0;

    //for (int i = 0; i < partialSums.size(); i++)
    //{
    //    dotProduct += partialSums[i];
    //}

    //cout << "Partial Sums: ";
    //for (int sum : partialSums) {
    //    cout << sum << " ";
    //}
    //cout << "\nFinal Dot Product: " << dotProduct << endl;



    return 0;
}
