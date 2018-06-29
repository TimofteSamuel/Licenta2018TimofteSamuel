/*
 * main.cpp
 *
 *  Created on: Apr 2, 2015
 *      Author: uidw7064
 */

#include <glog/logging.h>
#include <gflags/gflags.h>
#include <boost/filesystem.hpp>

#include "config/config.hpp"
#include "interfaces/interfaces.hpp"
#include "main_handler/main_handler.hpp"

DEFINE_string(config_file, "./wican.conf",
		"Specify the path to configuration file.");

int main(int argc, char** argv, char** envp) {
	// Default logging settings
	FLAGS_logtostderr = true;
	google::ParseCommandLineFlags(&argc, &argv, true);
	google::InitGoogleLogging(argv[0]);

	DLOG(INFO) << "Configuration file: " << FLAGS_config_file;
	Config configParser(FLAGS_config_file, envp);

	// Extract configurations groups
	std::map<std::string, Config*> configGroups = configParser.getGroups();
	Config *loggingConfig = configGroups["logging"];
	Config *webInterfaceConfig = configGroups["web_interface"];
	Config *canInterfaceConfig = configGroups["can_interface"];

	// Check logging configuration
	if(loggingConfig->pBool("log_to_file")) {
		FLAGS_alsologtostderr = loggingConfig->pBool("log_to_stderr");
		// Verify if exist log folder, else create
		boost::filesystem::path logFolder(loggingConfig->pString("log_file_path"));
		if(boost::filesystem::exists(logFolder)) {
			FLAGS_log_dir = logFolder.c_str();
		}
		else {
			if(boost::filesystem::create_directory(logFolder)) {
				FLAGS_log_dir = logFolder.c_str();
			}
			else {
				LOG(ERROR) << "Error at create folder for logging, logging to file disabled";
				FLAGS_logtostderr = true;
			}
		}
	}
	else {
		FLAGS_logtostderr = loggingConfig->pBool("log_to_stderr");
	}

	Interfaces *interfaces;
	MainHandler *mainHandler;
	try {
		interfaces = new Interfaces(webInterfaceConfig, canInterfaceConfig);
		mainHandler = new MainHandler(interfaces);
	}
	catch(std::exception& e) {
		LOG(ERROR) << "Error at create Interfaces. Details:\n" << e.what();
		//exit(EXIT_FAILURE);
	}

/*	while(1){

		CanInterface::Message canMessage;

		canMessage.id = 100;
		canMessage.dlc = 1;
		canMessage.data[0] = 42;

		interfaces->canInterface->SendMessageHandler(canMessage);

		sleep(1);
	}
*/
	DLOG(INFO) << "Wait WebInterface requests";
	interfaces->join();
}
