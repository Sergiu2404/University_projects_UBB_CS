#pragma once
#include <iostream>
#include "UserRepository.h"

class CSVUserRepository: public UserRepository {
public:
	CSVUserRepository(std::string fileName) :UserRepository(fileName) {
		this->fileName = fileName;
	};
	CSVUserRepository() {}

	void write_to_file() override;

	void display() const override;

};