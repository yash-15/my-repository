#include "LSystem.h"

LSystem::LSystem(){
	//  default constructor
}

bool LSystem::isValid(const std::string & str){
	int n = str.length();
	for(int i=0;i<n;i++)
		if(!isVariable[str[i]] && !isTerminal[str[i]])
			return false;
	return true;
}

std::string LSystem::generate(int no_of_levels){
	std::string result[2];
	result[0] = axiom;
	bool current = 0, next = 1;
	while (no_of_levels --){
		result[next] = generateNext(result[current]);
		current ^= 1;
		next ^= 1;
	}
	result[next] = purgeVariables(result[current]);
	current ^= 1;
	next ^= 1;
	
	return result[current];
}

std::string LSystem::generateNext(const std::string & str){
	std::string result;
	int n = str.length();
	for(int i=0;i<n;i++){
		if(isTerminal[str[i]])
			result += str[i];
		else
			result += production_rule[str[i]][chooseRule(str[i])].first;
	}
	return result;
}

std::string LSystem::purgeVariables(const std::string & str){
	std::string result;
	int n = str.length();
	for(int i=0;i<n;i++){
		if(isTerminal[str[i]])
			result += str[i];
	}
	return result;
}