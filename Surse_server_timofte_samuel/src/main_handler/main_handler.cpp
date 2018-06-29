/*
 * user_handler.cpp
 *
 *  Created on: Apr 24, 2015
 *      Author: uidw7064
 */

#include <glog/logging.h>

#include "main_handler.hpp"

const std::string MainHandler::mainHandlerKey = "main_handler";

CanInterface::Message faceCanMessage;

/****************************************************************************************************/
/* Constructor and Destructor */
/****************************************************************************************************/
MainHandler::MainHandler(Interfaces *_interfaces)
:interfaces(_interfaces) {
	interfaces->webInterface->createMessageCallback(mainHandlerKey, [this](ParameterList _list) {
		this->requestCallback(_list);
	});
}

MainHandler::~MainHandler() {
	interfaces->webInterface->removeMessageCallback(mainHandlerKey);
}

/****************************************************************************************************/
/* Private Functions */
/****************************************************************************************************/
void MainHandler::requestCallback(ParameterList _message) {
	std::string command;
	ParameterList data, result;

	DLOG(INFO) << "Receive request for: " << mainHandlerKey;

	try {
		// Extract data
		command = _message.get<std::string>("cmd");
		data = _message.get_child("data");

		DLOG(INFO) << "Command: " << command;

		if(command == "send") {
			CanInterface::Message canMessage;
						// uint32_t tempID = data.get<uint32_t>("ID");
						// DLOG(INFO) << "ID from msg: " << tempID;
						// uint32_t tempDLC = data.get<uint32_t>("DLC");
						// DLOG(INFO) << "DLC from msg: " << tempDLC;
						faceCanMessage.id = data.get<uint32_t>("ID");
						faceCanMessage.dlc = data.get<uint32_t>("DLC");
						faceCanMessage.data[0] = data.get<uint8_t>("Byte_0");
						faceCanMessage.data[1] = data.get<uint8_t>("Byte_1");
						faceCanMessage.data[2] = data.get<uint8_t>("Byte_2");
						faceCanMessage.data[3] = data.get<uint8_t>("Byte_3");
						faceCanMessage.data[4] = data.get<uint8_t>("Byte_4");
						faceCanMessage.data[5] = data.get<uint8_t>("Byte_5");
						faceCanMessage.data[6] = data.get<uint8_t>("Byte_6");
						faceCanMessage.data[7] = data.get<uint8_t>("Byte_7");
						// DLOG(INFO) << "DLC from msg: " << tempDLC;
						interfaces->canInterface->SendMessageHandler(faceCanMessage);

		}
		else if(command == "register") {
//			result = registerUserCmd(data);
		}
		else if(command == "login") {
//			result = loginUserCmd(data);
		}
		else if(command == "logout") {
//			result = logoutUserCmd(data);
		}
		else {
			result.put("status", "Command don't exist");
		}
	}
	catch(boost::property_tree::ptree_error &e) {
		LOG(ERROR) << "Error at parsing parameters list in " << __FUNCTION__ << ". Details: " << e.what();
		result.put("status", e.what());
	}
	catch(std::exception& e) {
		LOG(ERROR) << "Unexpected error in " << __FUNCTION__ << ". Details: " << e.what();
		result.put("status", e.what());
	}

	// Update response data
	_message.put_child("data", result);

	// Send response
	interfaces->webInterface->sendMessageResponse(mainHandlerKey, _message);
}
