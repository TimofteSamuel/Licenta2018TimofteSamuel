/*
 * interfaces.hpp
 *
 *  Created on: Jul 17, 2015
 *      Author: uidw7064
 */

#ifndef SRC_INTERFACES_INTERFACES_HPP_
#define SRC_INTERFACES_INTERFACES_HPP_

#include "../config/config.hpp"
#include "web_interface/web_interface.hpp"
#include "can_interface/can_interface.hpp"

class Interfaces {
public:
	Interfaces(Config *webInterfaceConfig, Config *canInterfaceConfig);
	~Interfaces();

	void join(void);

	WebInterface *webInterface;
	CanInterface *canInterface;

private:

	Config *webInterfaceConfig;
	Config *canInterfaceConfig;
};

#endif /* SRC_INTERFACES_INTERFACES_HPP_ */
