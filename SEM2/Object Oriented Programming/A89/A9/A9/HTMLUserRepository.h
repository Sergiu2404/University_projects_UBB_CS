#pragma once
#include "UserRepository.h"

class HTMLUserRepository:public UserRepository
{
public:
	HTMLUserRepository() :UserRepository() {
	};
	//HTMLUserRepository() {}

	std::vector<Movie> read_from_file() override;

	void write_to_file() override;

	void display() const override; //pure virtual function display
};

