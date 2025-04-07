// test_apriorit.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <iostream>
#include <string>
#include <vector>

int getShift(char cipher, char plain) {
    return (cipher - plain + 26) % 26;
}

std::string decipher(const std::string& ciphertext) {
    std::string knownSymbol = "CSR";
    std::vector<int> keys;

    for (int i = 0; i < 3; i++) {
        keys.push_back(getShift(ciphertext[i], knownSymbol[i]));
    }

    int k1 = keys[0];
    int k2 = keys[1];
    int k3 = keys[2];
    int k4;

    if ((k2 - k1 == k3 - k2)) {
        int diff = k2 - k1;
        k4 = k3 + diff;
    }
    else if ((0 != k1 && 0 == k2 % k1) && (0 != k2 && 0 == k3 % k2)) {
        int ratio = k2 / k1;
        k4 = k3 * ratio;
    }
    else {
        std::cout << "Invalid progression";
        return "";
    }

    keys.push_back(k4);

    std::string result = "";
    for (int i = 0; i < 4; i++) {
        char decrypted = (ciphertext[i] - keys[i] - 'A' + 26) % 26 + 'A';
        result = result + decrypted;
    }
    return result;
}

int main()
{
    std::string cipher = "DUUE";
    std::string plain = decipher(cipher);
    std::cout << plain;
}

// Run program: Ctrl + F5 or Debug > Start Without Debugging menu
// Debug program: F5 or Debug > Start Debugging menu

// Tips for Getting Started: 
//   1. Use the Solution Explorer window to add/manage files
//   2. Use the Team Explorer window to connect to source control
//   3. Use the Output window to see build output and other messages
//   4. Use the Error List window to view errors
//   5. Go to Project > Add New Item to create new code files, or Project > Add Existing Item to add existing code files to the project
//   6. In the future, to open this project again, go to File > Open > Project and select the .sln file
