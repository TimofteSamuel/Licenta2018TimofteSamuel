/*
 * can_interface.cpp
 *
 *  Created on: Jul 8, 2015
 *      Author: uidj5690
 */

#include <boost/asio.hpp>
#include <glog/logging.h>
#include "can_interface.hpp"

CanInterface::CanInterface(std::string interfaceName)
:descriptor(ioService)
,canManagerId(0)
,canManagerCallback(0) {
	struct sockaddr_can addr;
	struct ifreq interfaceReq;
	int ret;

	//open socket
	if((s = socket(PF_CAN, SOCK_RAW, CAN_RAW)) < 0) {
		throw std::runtime_error("Cannot create CAN socket. Error: " + std::to_string(s));
	}

	addr.can_family = AF_CAN;

	//setting the name for the interface
	strcpy(interfaceReq.ifr_name, interfaceName.c_str());
	if((ret = ioctl(s, SIOCGIFINDEX, &interfaceReq)) < 0) {
		throw std::runtime_error("Error setting interface name. Error: " + std::to_string(ret));
	}

	addr.can_ifindex = interfaceReq.ifr_ifindex;

	// bind the socket
	if ((ret = bind(s, (struct sockaddr *)&addr, sizeof(addr))) < 0) {
		throw std::runtime_error("Error binding the socket. Error: " + std::to_string(ret));
	}

	// Assign the socket for the interface
	descriptor.assign(s);

	// Run thread
	std::size_t (boost::asio::io_service::*run) () = &boost::asio::io_service::run;
	boost::thread t(boost::bind(run, &ioService));
}

CanInterface::~CanInterface() {
	close(s);
}

void CanInterface::Start(unsigned int canManagerId, boost::function<void (Message)> canManagerCallback) {
	if(this->canManagerId == 0) {
		this->canManagerId = canManagerId;
		this->canManagerCallback = canManagerCallback;

		startReceiveWait();
	}
	else {
		throw std::runtime_error("CAN interface is busy");
	}
}

void CanInterface::Stop(unsigned int canManagerId) {
	if(canManagerId == this->canManagerId) {
		this->canManagerId = 0;
		//unbind the socket
		descriptor.cancel();
	}
}

void CanInterface::ReceiveMessageHandler(const boost::system::error_code& error, size_t receivedData) {
	time(&can_rx_msg_.timestamp);
	if(receivedData) {
		can_rx_msg_.msgType = rx_.can_id & 0x80000000;
		can_rx_msg_.rtrFlag = rx_.can_id & 0x40000000;
		can_rx_msg_.errFlag = rx_.can_id & 0x20000000;
		can_rx_msg_.id = rx_.can_id & 0x1FFFFFFF;
		can_rx_msg_.dlc = rx_.can_dlc;

		for(int i = 0; i < rx_.can_dlc; i++) {
			can_rx_msg_.data[i] = rx_.data[i];
		}

		if(canManagerCallback != 0) {
			canManagerCallback(can_rx_msg_);
		} else {
			LOG(ERROR) << "Error in " << __FUNCTION__;
			throw std::runtime_error("Callback for CanManager don't exist");
		}
	}
	startReceiveWait();
}

void CanInterface::SendMessageHandler(const Message &message) {

	tx.can_id = (message.msgType << 31) |
				message.id;

	tx.can_dlc = message.dlc;

	for(int i = 0; i < message.dlc; i++) {
		tx.data[i] = message.data[i];
	}

	try {
		write(s, &tx, sizeof(struct can_frame));
		//boost::asio::write(descriptor, boost::asio::buffer(&tx, sizeof(tx)));

	}
	catch(boost::system::system_error &e) {
		LOG(ERROR) << "Error at send CAN message." << e.what();
		throw;
	}
}

void CanInterface::startReceiveWait(void) {
	descriptor.async_read_some	(boost::asio::buffer(&rx_, sizeof(rx_)),
								boost::bind(&CanInterface::ReceiveMessageHandler,
											this,
											boost::asio::placeholders::error,
											boost::asio::placeholders::bytes_transferred));
}
