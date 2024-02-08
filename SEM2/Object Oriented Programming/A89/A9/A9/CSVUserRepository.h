#pragma once
#include "UserRepository.h"

class CSVUserRepository:public UserRepository
{
public:
	CSVUserRepository() :UserRepository() {}

	//CSVUserRepository() {};

	void write_to_file() override;

	void display() const override;
};

