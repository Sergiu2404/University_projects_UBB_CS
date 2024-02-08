#include <stdio.h>
#include <stdint.h>
/*
9)
a. Decompose a given natural number in its prime factors.
b. Given a vector of numbers, find the longest contiguous subsequence such 
that any consecutive elements contain the same digits*/


int* numberToDecompose, suboptionNumber, array[100], lengthOfArray;


void print_menu() {
	printf("MENU==============================================================\n");
	printf("options:\n");
	printf("1-read vector\n");
	printf("2-> 1-Give a natural number and decompose it in its prime factors\n");
	printf("2=> 2-Give a vector of numbers, find the longest contiguous subsequence such that any consecutive elements contain the same digits\n");
	printf("MENU==============================================================\n");
}


int checkSameDigitsNumbers(int number1, int number2) {
	if (number1 == number2) {
		return 1;
	}
	int countDigitsArray[10];
	for (int digit = 0; digit <= 9; digit++) {
		countDigitsArray[digit] = 0;
	}
	while (number1) {
		countDigitsArray[number1 % 10]++;
		number1 = number1 / 10;
	}
	while (number2) {
		countDigitsArray[number2 % 10]--;
		number2 = number2 / 10;
	}
	for (int digit = 0; digit <= 9; digit++) {
		if (countDigitsArray[digit] != 0) {
			return 0;
		}
	}
	return 1;
}


void decomposeNumberIntoPrimeFactors(int numberToDecompose) {
	printf("read number to decompose: ");
	scanf_s("%d", &numberToDecompose);
	if (numberToDecompose == 1 || numberToDecompose == 0) {
		printf("can not decompose in prime factors\n");
	}
	else if (numberToDecompose < 0) {
		numberToDecompose = -numberToDecompose;
	}
	else {
		int primeFactor = 2;
		while (numberToDecompose > 1) {
			if (numberToDecompose % primeFactor == 0) {
				numberToDecompose = numberToDecompose / primeFactor;
				printf("%d ",primeFactor);
			}
			else primeFactor++;
		}

	}
	printf("\n");
}


void longestSubsequenceOfConsecutiveNumbersThatHaveSameDigits(int array[], int lengthOfArray) {
	int i = 1, temporarMaxLengthStartIndex = 0, temporarMaxLengthEndIndex = 0, maxLengthSubsequence = 0, maxLengthSubsequenceStartIndex = 1, maxLengthSubsequenceEndIndex = 1;
	while (i < lengthOfArray) {
		temporarMaxLengthStartIndex = i;
		temporarMaxLengthEndIndex = i;
		while (checkSameDigitsNumbers(array[i], array[i + 1])==1 && i < lengthOfArray) {
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
	printf("Maximum length: %d, with index start: %d, index end: %d", maxLengthSubsequence+1, maxLengthSubsequenceStartIndex, maxLengthSubsequenceEndIndex);
}



void use_commands(optionNumber) {

	
	if (optionNumber == 1) {
		printf("number of elements of the array: ");
		scanf_s("%d", &lengthOfArray);
		
		for (int i = 1; i <= lengthOfArray; i++) {
			printf("read element number %d from the vector: ",i);
			scanf_s("%d", &array[i]);
		}
	}
	else if (optionNumber == 2) {
		printf("suboption 1 or suboption 2: ");
		scanf_s("%d", &suboptionNumber);
		
		if (suboptionNumber == 1) {
			printf("decompose natural number in its prime \n factors option chosen: ");
			decomposeNumberIntoPrimeFactors(&numberToDecompose);
		}
		
		else if (suboptionNumber == 2) {
			if (lengthOfArray == 0) {
				printf("First choose option 1 for reading the array and its length\n");
			}
			else {
				printf("longest subsequence of consecutive numbers that have the \n same digits option chosen: ");
				longestSubsequenceOfConsecutiveNumbersThatHaveSameDigits(array, lengthOfArray);
			}
		}
		
		else {
			printf("Invalid suboption, try again only with digit 1 or 2\n");
		}
	}
	
	else if (optionNumber == 3) {
		printf("exit program");
		exit();
	}
	
	else {
		printf("Invalid option, try again only with digit 1 or 2\n");
	}

}


int main() {
	int option_number;
	int exit_variable = 1;
	while (exit_variable==1) {
		print_menu();
		printf("give option number: ");
		scanf_s("%d", &option_number);
		use_commands(option_number);
	}
	return 0;
}