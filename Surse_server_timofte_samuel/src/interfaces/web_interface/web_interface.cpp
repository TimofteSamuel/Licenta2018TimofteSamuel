/*
 * web_interface.cpp
 *
 *  Created on: Apr 2, 2015
 *      Author: uidw7064
 */
#include <fstream>
#include <string>
#include <iostream>
#include <glog/logging.h>

#include "web_interface.hpp"

const std::string WebInterface::targetKey = "target";
const std::string WebInterface::messageKey = "message";

// TODO Accept all connection for login for editing project but if someone run project and block CAN bus notify other user
// TODO In future create a chat for discussing ordering of use of CAN bus

WebInterface::WebInterface(unsigned short _httpPort, std::string _httpPath, unsigned short _websocketPort)
: httpPort(_httpPort)
, httpPath(_httpPath)
, websocketPort(_websocketPort)
, httpServer(httpPort, 4)
, websocketServer(websocketPort, 4) {
	// Create default resource for http server
	httpServer.default_resource["GET"]=[this](SimpleWeb::ServerBase<SimpleWeb::HTTP>::Response& response, std::shared_ptr<SimpleWeb::ServerBase<SimpleWeb::HTTP>::Request> request) {
		std::string filename=httpPath;

		std::string path=request->path;

		//Replace all ".." with "." (so we can't leave the web-directory)
		size_t pos;
		while((pos=path.find(".."))!=std::string::npos) {
			path.erase(pos, 1);
		}

		filename+=path;
		std::ifstream ifs;
		//A simple platform-independent file-or-directory check do not exist, but this works in most of the cases:
		if(filename.find('.')==std::string::npos) {
			if(filename[filename.length()-1]!='/')
				filename+='/';
			filename+="index.html";
		}
		ifs.open(filename, std::ifstream::in);
		if(ifs) {
			ifs.seekg(0, std::ios::end);
			size_t length=ifs.tellg();

			ifs.seekg(0, std::ios::beg);

			response << "HTTP/1.1 200 OK\r\nContent-Length: " << length << "\r\n\r\n";

			//read and send 128 KB at a time if file-size>buffer_size
			size_t buffer_size=131072;
			if(length>buffer_size) {
				std::vector<char> buffer(buffer_size);
				size_t read_length;
				while((read_length=ifs.read(&buffer[0], buffer_size).gcount())>0) {
					response.stream.write(&buffer[0], read_length);
					response << SimpleWeb::ServerBase<SimpleWeb::HTTP>::flush;
				}
			}
			else
				response << ifs.rdbuf();

			ifs.close();
		}
		else {
			std::string content="Could not open file "+filename;
			response << "HTTP/1.1 400 Bad Request\r\nContent-Length: " << content.length() << "\r\n\r\n" << content;
		}
	};

	// Start HTTP server in another thread
	httpServerThread = new std::thread([this](){
        httpServer.start();
    });

	// Setting websocket server
	auto& websocket = websocketServer.endpoint["^/?$"];

	websocket.onopen=[this](std::shared_ptr<SimpleWeb::SocketServerBase<SimpleWeb::WS>::Connection> connection) {
		DLOG(INFO) << "Opened connection: " << (size_t)connection.get();
		if(!connected) {
			this->connected = true;
			this->connection = (size_t)connection.get();
			DLOG(INFO) << "Connection accepted: " << (size_t)connection.get();
		} else {
			// TODO send reject message
			this->websocketServer.send_close(connection, 1000, "busy");
			DLOG(INFO) << "Connection rejected, another connection is opened";
		}
	};

	websocket.onmessage=[this](std::shared_ptr<SimpleWeb::SocketServerBase<SimpleWeb::WS>::Connection> connection, std::shared_ptr<SimpleWeb::SocketServerBase<SimpleWeb::WS>::Message> request) {
		// Verification if it is first connected user
		if(this->connection == (size_t)connection.get()) {
			// Extract data as string data
			std::stringstream requestData;
			request->data >> requestData.rdbuf();
			DLOG(INFO) << "Request data received: \n" << requestData.str();

			try {
				// Try parsing message from JSON, else exception
				boost::property_tree::ptree mainTree;
				boost::property_tree::read_json(requestData, mainTree);

				// Extract request type, else exception
				std::string targetCallback = mainTree.get<std::string>(targetKey);
				//DLOG(INFO) << "Target callback request: " << targetCallback;

				// Extract list of parameters, else exception
				ParameterList message = mainTree.get_child(messageKey);

				// Find callback: if exist call with list of parameters, else exception
				bool callbackExist = false;
				for(auto& it: messageCallbacks) {
					if(targetCallback == it.first) {
						it.second(message);
						callbackExist = true;
						break;
					}
				}

				if(!callbackExist) {
					throw std::runtime_error("Target callback not exist");
				}
			}
			catch(std::exception& e) {
				// Log the exception and send error message
				LOG(WARNING) << "Exception: " << e.what();
				ParameterList result;
				result.put("reason", e.what());
				this->sendMessageResponse("error", result);
			}
		}
	};

	//See RFC 6455 7.4.1. for status codes
	websocket.onclose=[this](std::shared_ptr<SimpleWeb::SocketServerBase<SimpleWeb::WS>::Connection> connection, int status, const std::string& reason) {
		DLOG(INFO) << "Closed connection " << (size_t)connection.get() << " with status code " << status;
		if(this->connection == (size_t)connection.get()) {
			connected = false;
			this->connection = 0;
		}
	};

	//See http://www.boost.org/doc/libs/1_55_0/doc/html/boost_asio/reference.html, Error Codes for error code meanings
	websocket.onerror=[this](std::shared_ptr<SimpleWeb::SocketServerBase<SimpleWeb::WS>::Connection> connection, const boost::system::error_code& ec) {
		LOG(ERROR) << "Error in connection " << (size_t)connection.get() << ". " <<
				"Error: " << ec << ", error message: " << ec.message();
		this->connected = false;
	};

	// Start websocket server in thread
	websocketServerThread = new std::thread([this](){
		websocketServer.start();
	});
}

WebInterface::~WebInterface() {
	// Stoping servers
	httpServer.stop();
	websocketServer.stop();
	// Wait thread to terminate
	httpServerThread->join();
	websocketServerThread->join();
	// Delete threads
	delete httpServerThread;
	delete websocketServerThread;
}

void WebInterface::createMessageCallback(std::string _targetName, CallbackFunc _callback) {
	messageCallbacks.insert({_targetName, _callback});
}


void WebInterface::removeMessageCallback(std::string _targetName) {
	messageCallbacks.erase(_targetName);
}

void WebInterface::sendMessageResponse(std::string _targetName, ParameterList _parameterList) {
	// Create JSON message
	boost::property_tree::ptree mainTree;
	mainTree.put(targetKey, _targetName);

	mainTree.put_child(messageKey, _parameterList);

	std::stringstream data_ss;
	boost::property_tree::write_json(data_ss, mainTree);

	for(auto it: this->websocketServer.get_connections()) {
		if(this->connection == (size_t)it.get()) {
			this->websocketServer.send(it, data_ss, [](const boost::system::error_code& ec){
		        if(ec) {
		            LOG(ERROR) << "Error sending message. " <<
		            //See http://www.boost.org/doc/libs/1_55_0/doc/html/boost_asio/reference.html, Error Codes for error code meanings
		                    "Error: " << ec << ", error message: " << ec.message();
		       }
		    });
		}
	}

}

void WebInterface::join(void) {
	// join to threads
	httpServerThread->join();
	websocketServerThread->join();
}

void WebInterface::addHttpResource(void) {
    this->httpServer.resource["^/info$"]["GET"]=[this](SimpleWeb::ServerBase<SimpleWeb::HTTP>::Response& response, std::shared_ptr<SimpleWeb::ServerBase<SimpleWeb::HTTP>::Request> request)  {
        std::stringstream content_stream;
        content_stream << "<h1>Request:</h1>";
        content_stream << request->method << " " << request->path << " HTTP/" << request->http_version << "<br>";
        for(auto& header: request->header) {
            content_stream << header.first << ": " << header.second << "<br>";
        }

        //find length of content_stream (length received using content_stream.tellp())
        content_stream.seekp(0, std::ios::end);

        response <<  "HTTP/1.1 200 OK\r\nContent-Length: " << content_stream.tellp() << "\r\n\r\n" << content_stream.rdbuf();
    };

    this->httpServer.refreshResources();

    // Setting websocket server
	auto& websocket = websocketServer.endpoint["^/test/?$"];

	websocket.onopen=[this](std::shared_ptr<SimpleWeb::SocketServerBase<SimpleWeb::WS>::Connection> connection) {
		DLOG(INFO) << "Opened connection: " << (size_t)connection.get();
	};

	websocket.onmessage=[this](std::shared_ptr<SimpleWeb::SocketServerBase<SimpleWeb::WS>::Connection> connection, std::shared_ptr<SimpleWeb::SocketServerBase<SimpleWeb::WS>::Message> message) {
		DLOG(INFO) << "Message " << message->data.get();
	};

	//See RFC 6455 7.4.1. for status codes
	websocket.onclose=[this](std::shared_ptr<SimpleWeb::SocketServerBase<SimpleWeb::WS>::Connection> connection, int status, const std::string& reason) {
		DLOG(INFO) << "Closed connection " << (size_t)connection.get() << " with status code " << status;
	};

	//See http://www.boost.org/doc/libs/1_55_0/doc/html/boost_asio/reference.html, Error Codes for error code meanings
	websocket.onerror=[](std::shared_ptr<SimpleWeb::SocketServerBase<SimpleWeb::WS>::Connection> connection, const boost::system::error_code& ec) {
		LOG(ERROR) << "Error in connection " << (size_t)connection.get() << ". " <<
				"Error: " << ec << ", error message: " << ec.message();
	};

}
