// ex9_a1.cpp : This file contains the 'main' function. Program execution begins and ends there.
// !!NO ABREVIATIONS
// !!SUGESTIVE VARIABLE AND FUNCTION NAMES

#include <iostream>

//decompose into prime numbers
void decomposeNumberIntoPrimeFactors(int numberToDecompose) {

	if (numberToDecompose == 1 || numberToDecompose == 0) {
		std::cout << "can not decompose in prime factors";
	}
	else if (numberToDecompose < 0) {
		numberToDecompose = -numberToDecompose;
	}
	else {
		int primeFactor = 2;
		while (numberToDecompose > 1) {
			if(numberToDecompose % primeFactor == 0) {
				numberToDecompose = numberToDecompose / primeFactor;
				std::cout << primeFactor << " ";
			}
			else primeFactor++;
		}

	}

}

int checkSameDigitsNumbers(int number1, int number2) {
	if (number1 == number2)
		return true;
	int countDigitsArray[10];
	for (int digit = 0; digit <= 9; digit++)
		countDigitsArray[digit] = 0;
	while (number1) {
		countDigitsArray[number1 % 10]++;
		number1 = number1 / 10;
	}
	while (number2) {
		countDigitsArray[number2 % 10]--;
		number2 = number2 / 10;
	}
	for (int digit = 0; digit <= 9; digit++)
		if (countDigitsArray[digit] != 0)
			return false;
	return true;
}
void longestSubsequenceOfConsecutiveNumbersThatHaveSameDigits(int array[],int length) {
	int i=1, temporarMaxLengthStartIndex=0, temporarMaxLengthEndIndex=0, j,maxLengthSubsequence=0, maxLengthSubsequenceStartIndex=1, maxLengthSubsequenceEndIndex=1;
	while (i < length) {
		temporarMaxLengthStartIndex = i;
		temporarMaxLengthEndIndex=i;
		while (checkSameDigitsNumbers(array[i], array[i + 1]) && i<length) {
			temporarMaxLengthEndIndex++;
			i++;
		}
		if (temporarMaxLengthEndIndex - temporarMaxLengthStartIndex > maxLengthSubsequence) {
			maxLengthSubsequence = temporarMaxLengthEndIndex - temporarMaxLengthStartIndex;
			maxLengthSubsequenceStartIndex = temporarMaxLengthStartIndex;
			maxLengthSubsequenceEndIndex = temporarMaxLengthEndIndex;
		}
		i++;
	}
	std::cout<<"maximum length: "<<maxLengthSubsequence << " with index start: " << maxLengthSubsequenceStartIndex << ", index end: " << maxLengthSubsequenceEndIndex;
}
int main() {
	int keyboardNumber,array[100],numbersOfArray,length;
	std::cout << "number to decompose: ";
	std::cin >> keyboardNumber;
	decomposeNumberIntoPrimeFactors(keyboardNumber);

	std::cout << checkSameDigitsNumbers(112, 121);
	std::cout << "\n";
	std::cout << "length of array: ";
	std::cin >> length;
	for (int i = 1; i <= length; i++) {
		std::cout << "enter number: ";
		std::cin >> array[i];
	}
	
	longestSubsequenceOfConsecutiveNumbersThatHaveSameDigits(array, length);

	return 0;
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
