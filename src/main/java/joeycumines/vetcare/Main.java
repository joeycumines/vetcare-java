package joeycumines.vetcare;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A simple command line program to test out the message system for rapid
 * development.
 * 
 * @author Joey
 *
 */
public class Main {

	public static void main(String[] args) {
		// create a new logger
		Logger logger = Logger.getLogger(Main.class.getName());
		// disable console info
		logger.setUseParentHandlers(false);
		FileHandler fh;
		try {
			// This block configure the logger with handler and formatter
			fh = new FileHandler("log.txt");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//logger is setup, load up the message API
		MessageAPI api = new MessageAPI();
		// message api is setup, load up the thread that performs background tasks, such as sending messages
		MessageWorker worker = new MessageWorker("VetcareMessageWorker", logger, api);
		//start the worker thread
		worker.start();
		logger.info("Loaded the program successfully.");
		
		
		//exit
		logger.info("Exiting, stopping all threads, will quit now.");
		api.stopWorker();
	}

}
