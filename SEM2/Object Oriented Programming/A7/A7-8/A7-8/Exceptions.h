#pragma once
#include <iostream>
#include <vector>



class FileException :public std::exception {
private:
	std::string message;
public:
	FileException(std::string message) :message{ message } {
		this->print_message();
	};
	FileException() {};

	std::string print_message() {
		return this->message;//message;
	}
};


class InputException {
private:
	std::string message;
public:
	InputException(std::string message) : message{ message } {
		this->print_message();
	};
	InputException() {};

	std::string print_message() {
		return this->message;
	}
};






//class NotANumber:public std::exception {
//private:
//	std::string message;//std::string print_message();
//public:
//	NotANumber(std::string message) :message{ message } {
//		this->print_message();
//	};
//	std::string print_message() {
//		return this->message;//message;
//	}
//};