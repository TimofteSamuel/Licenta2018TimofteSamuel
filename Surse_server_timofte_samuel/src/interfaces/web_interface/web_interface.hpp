/*
 * web_interface.hpp
 *
 *  Created on: Apr 2, 2015
 *      Author: uidw7064
 */

#ifndef SRC_WEB_INTERFACE_WEB_INTERFACE_HPP_
#define SRC_WEB_INTERFACE_WEB_INTERFACE_HPP_

#include <boost/function.hpp>

//Added for the json
#define BOOST_SPIRIT_THREADSAFE
#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/json_parser.hpp>

#include "http_server/server_http.hpp"
#include "websocket_server/server_ws.hpp"

class WebInterface;

typedef boost::property_tree::ptree ParameterList;
typedef boost::function<void (ParameterList)> CallbackFunc;

class WebInterface
{
public:
	// Constructor
	WebInterface(unsigned short _httpPort, std::string _httpPath, unsigned short _websocketPort);

	// Destructor
	~WebInterface();

	void createMessageCallback(std::string _resourceName, CallbackFunc _callback);

	void removeMessageCallback(std::string _resourceName);

	void sendMessageResponse(std::string _resourceName, ParameterList _parameterList);

	void join(void);

	void addHttpResource(void);

private:
	static const std::string targetKey;
	static const std::string messageKey;

	std::unordered_map<std::string, CallbackFunc> messageCallbacks;
	unsigned short httpPort;
	std::string httpPath;
	unsigned short websocketPort;

	// HTTP server
	SimpleWeb::Server<SimpleWeb::HTTP> httpServer;
	std::thread *httpServerThread;

	// WebSocket server
	SimpleWeb::SocketServer<SimpleWeb::WS> websocketServer;
	std::thread *websocketServerThread;
	bool connected;
	unsigned long connection;
};

#endif /* SRC_WEB_INTERFACE_WEB_INTERFACE_HPP_ */
