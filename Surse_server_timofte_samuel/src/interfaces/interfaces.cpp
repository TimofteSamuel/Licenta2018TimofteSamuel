/*
 * interfaces.cpp
 *
 *  Created on: Jul 17, 2015
 *      Author: uidw7064
 */

#include <glog/logging.h>

#include "interfaces.hpp"

Interfaces::Interfaces(Config *_webInterfaceConfig, Config *_canInterfaceConfig)
:webInterfaceConfig(_webInterfaceConfig)
,canInterfaceConfig(_canInterfaceConfig) {
	try {
			webInterface = new WebInterface		(webInterfaceConfig->pInt("http_port"),
												webInterfaceConfig->pString("http_path"),
												webInterfaceConfig->pInt("websocket_port"));
		}
		catch(std::exception& e) {
			LOG(ERROR) << "Error at create a WEB interface.\n";
			throw;
		}

		try {
			canInterface = new CanInterface		(canInterfaceConfig->pString("name"));
		}
		catch(std::exception& e) {
			LOG(ERROR) << "Error at create a CAN interface.\n";
			throw;
		}
}

Interfaces::~Interfaces() {
	delete this->webInterface;
	delete this->canInterface;
}

void Interfaces::join(void) {
	this->webInterface->join();
}

