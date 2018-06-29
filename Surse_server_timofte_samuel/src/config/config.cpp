#include "config.hpp"

#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <glog/logging.h>


Config::Config(std::string name, std::string parentDebugInfo) {
	debugInfo = parentDebugInfo + ", " + name;
}

Config::Config(std::string configFile, char** envp) {
	while (*envp) {
		std::string envEntry = *envp;
		size_t pos = envEntry.find('=');
		if (pos != std::string::npos) {
			std::string name = envEntry.substr(0, pos);
			std::string value = envEntry.substr(pos+1, std::string::npos);
			envSymbols[name] = value;
			//DLOG(INFO) << "environment symbol: '" << name << "' = '" << value << "'";
		}
		++envp;
	}

	debugInfo = configFile;
	groupStack.push_front(this);

	FILE* in = fopen(configFile.c_str(), "r");
	if (!in) {
		LOG(ERROR) << "Cannot open input file '" << configFile << "'" << std::endl;
		exit(EXIT_FAILURE);
	}

	char buff[1024];
	while (fgets(buff, 1024, in)) {

		std::string line=buff;
		if ( (line.length() > 2) && (line[0] != '#') && (line.find(')') == std::string::npos) ) {
			std::string name;
			std::string value;
			split(line, name, value, '=');

			if (value == "(") {
				DLOG(INFO) << "config: new group '" << name << "'";
				Config* newGroup = new Config(name, debugInfo);
				groupStack.front()->groups[name] = newGroup;
				groupStack.push_front(newGroup);
			} else {
				for (std::list<Config*>::reverse_iterator i = groupStack.rbegin(); i != groupStack.rend(); ++i) {
					(*i)->symbolExpand(value);
				}
				envSymbolExpand(value);
				DLOG(INFO) << "config: name = '" << name << "', value = '" << value << "'";
				groupStack.front()->add(name, value);
			}
		}
		if ( (line.length() > 0) && (line[0] != '#') && (line.find(')') != std::string::npos) ) {
			DLOG(INFO) << "end of group";
			groupStack.pop_front();
		}
	}

	fclose(in);
}

Config::~Config() {
	for (std::map<std::string, Config*>::iterator i = groups.begin(); i != groups.end(); ++i) {
		delete i->second;
	}
}

void Config::add(std::string name, std::string value) {
	symbols[name] = value;
}

void Config::split(std::string in, std::string& left, std::string& right, char c) {
	size_t pos = in.find_first_of(c);
	if(pos == std::string::npos) {
		left = in;
		trim(left);
		right = "";
	} else if (pos <= 1) {
		left = "";
		right = in.substr(pos+1, std::string::npos);
		trim(right);
	} else {
		left = in.substr(0, pos-1);
		trim(left);
		right = in.substr(pos+1, std::string::npos);
		trim(right);
	}
}

void Config::trim(std::string& s) {
	while ( (s.length() > 1) && ( (s[0] == ' ') || (s[0] =='\t') ) ) {
		s = s.substr(1, std::string::npos);
	}
	while ( (s.length() > 1) &&
			( (s[s.length()-1] == ' ') ||
			  (s[s.length()-1] == '\t') || 
			  (s[s.length()-1] == '\n') || 
			  (s[s.length()-1] == '\r') ) ) {
		s = s.substr(0, s.length()-1);
	}
	if ( (s.length() > 1) && (s[0] == '"') ) {
		s = s.substr(1, std::string::npos);
	}
	if ( (s.length() > 1) && (s[s.length()-1] == '"') ) {
		s = s.substr(0, s.length()-1);
	}
}

void Config::symbolExpand(std::string& s) {
	symbolExpand(symbols, s);
}

void Config::envSymbolExpand(std::string& s) {
	symbolExpand(envSymbols, s);
}

void Config::symbolExpand(std::map<std::string, std::string>& symbols, std::string& s) {
	bool expanded;
	do {
		expanded = false;
		for (std::map<std::string, std::string>::iterator i = symbols.begin(); i != symbols.end(); ++i) {
			std::string search = "%" + i->first + "%";
			std::string replace = i->second;
			size_t pos = s.find(search);
			if (pos != std::string::npos) {
				expanded = true;
				s.replace(pos, search.length(), replace);
			}
		}
	} while (expanded);
}

std::string Config::pString(std::string name) {
	std::map<std::string, std::string>::iterator i = symbols.find(name);
	if (i == symbols.end()) {
		DLOG(INFO) << "access of missing property '" << name << "' (" << debugInfo << ")";
		exit(4);
	}
	return i->second;
}

bool Config::pBool(std::string name) {
	std::string val = pString(name);

	if ( (val == "yes") ||
	     (val == "Yes") ||
	     (val == "YES") ||
		 (val == "true") ||
	     (val == "True") ||
	     (val == "TRUE"))
	{
		return true;
	}

	return false;
}

double Config::pDouble(std::string name) {
	std::string val = pString(name);

	return atof(val.c_str());
}

int Config::pInt(std::string name) {
	std::string val = pString(name);

	return atoi(val.c_str());
}
