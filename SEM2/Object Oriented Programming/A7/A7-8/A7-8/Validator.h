#include <vector>
#include <iostream>
#include "Exceptions.h"


class Validator {
public:
	void validate(std::string option, std::vector<std::string> options) {
		int found = 0;

		for (int i = 0; i < options.size(); i++)
			if (option == options[i])
				found = 1;

		if (found == 0)
			throw InputException("Invalid input!");
	}
};
