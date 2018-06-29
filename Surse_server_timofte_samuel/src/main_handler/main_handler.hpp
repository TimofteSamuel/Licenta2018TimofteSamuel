/*
 * user_handler.hpp
 *
 *  Created on: Apr 24, 2015
 *      Author: uidw7064
 */

#ifndef SRC_MAIN_HANDLER_MAIN_HANDLER_HPP_
#define SRC_MAIN_HANDLER_MAIN_HANDLER_HPP_

#include "../interfaces/interfaces.hpp"
//#include <main_handler/user/user.hpp>

class MainHandler {
public:
	/**
	 * Constructor
	 * Create a WebInterface, PgsqlInterface and FsInterface using specified configurations
	 * Handling User and PublishedPages
	 * @param webInterfaceConfig	Configuration parameters for WebInterface
	 * @param dbInterfaceConfig		Configuration parameters for PgsqlInterface
	 */
	MainHandler(Interfaces *interfaces);

	/**
	 * Destructor
	 * Delete User(if exist), WebInterface, PgsqlInterface and FsInterface
	 */
	~MainHandler();

private:
	static const std::string mainHandlerKey;

	void requestCallback(ParameterList list);

	Interfaces *interfaces;
};

#endif /* SRC_MAIN_HANDLER_MAIN_HANDLER_HPP_ */
