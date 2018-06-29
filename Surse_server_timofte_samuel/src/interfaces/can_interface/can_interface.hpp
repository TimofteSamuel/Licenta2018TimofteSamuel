/*
 * can_interface.h
 *
 *  Created on: Jul 8, 2015
 *      Author: uidj5690
 */

#ifndef SRC_CAN_INTERFACE_CAN_INTERFACE_HPP_
#define SRC_CAN_INTERFACE_CAN_INTERFACE_HPP_

#include <iostream>
#include <string>
#include <stdint.h>
#include <ctime>

#include <boost/thread.hpp>
#include <boost/asio.hpp>

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include <net/if.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/socket.h>

#include <linux/can.h>
#include <linux/can/raw.h>

class CanInterface{
public:
	typedef struct {
		time_t timestamp;
		bool msgType;
		bool errFlag;
		bool rtrFlag;
		uint32_t id;
		int8_t dlc;
		int8_t data[8];
	} Message;
	//Constructor
	CanInterface(std::string interfaceName);
	//Desctructor
	~CanInterface();
	void Start(unsigned int canManagerId, boost::function<void (Message)> canManagerCallback);
	void Stop(unsigned int id_interface);
	void ReceiveMessageHandler(const boost::system::error_code& error, size_t receivedData);
	void SendMessageHandler(const Message &message);

private:
	void startReceiveWait(void);
	can_frame tx;
	boost::asio::io_service ioService;
	boost::asio::posix::stream_descriptor descriptor;
	unsigned int s;
	unsigned int canManagerId;
	boost::function<void (Message)> canManagerCallback;
	Message can_rx_msg_;
	can_frame rx_;
};

#endif /* SRC_CAN_INTERFACE_CAN_INTERFACE_HPP_ */
