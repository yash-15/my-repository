#ifndef LSYSTEM_INCLUDED
#define LSYSTEM_INCLUDED

#include <string>
#include <map>
#include <vector>
#include <utility>	//for std::pair

/*
	Lindenmayer System
	 - Context free grammer expressed using production rules
	 - Stochastic system (based on non-deterministic CFL)
*/

class LSystem{
	
private:
	std::map<char,bool> isVariable;
	// maps every alphabet (symbol) of grammer with:
	//	1: if it is a variable (replacable symbol)
	//  0: otherwise (terminating/ invalid symbol)

	std::map<char,bool> isTerminal;
	// maps every alphabet (symbol) of grammer with:
	//  1: if it is a terminating symbol
	//  0: otherwise (non-terminating/ invalid symbol)
	
	std::string axiom;
	// initial string for the grammar

	std::map<char,std::vector< std::pair<std::string,float> > > production_rule;
	// maps every symbol with a vector of all possible transitions and their probabilites

	int chooseRule(char);
	// returns index of stochastically selected production rule for given symbol

public:

	LSystem();
	// defaul constructor

	bool isValid(const std::string&);
	// checks if the string can be considered as an axiom

	std::string generate(int);
	// generates a terminal string produced after given no. of iterations on axiom

	std::string generateNext(const std::string&);
	// returns a string after applying production rules on each character of given string

	std::string purgeVariables(const std::string&);
	// returns a string with non-terminal symbols removed from given string

};

#endif