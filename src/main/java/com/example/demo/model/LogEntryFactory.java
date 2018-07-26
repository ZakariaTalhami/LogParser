package com.example.demo.model;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.example.demo.singleton.LoggerSingleton;



/**
 * Factory entity used to generate appropriate class ({@link LogEntry} or {@link LogEntryError})
 * from the log information provided to the factory in @link getLogEntry Method},
 * the distinction is done by the log level.
 * 
 * 
 * @author Zakaria Talhami
 *
 */
@Component
public class LogEntryFactory {
	
	final static Logger logger = LoggerSingleton.getLoggerOBJ().getLoggerman();
	

	/**
	 * Returns an instance of either logEntry or LogEntryError depending on the level of the log entry provided.
	 * The provided buffer array must contain 6 elements, which include in this order
	 * { timeStamp , level , process , object , message , exception } 
	 * The ID of the service (file) that generated the log is provided link the log to the service.
	 * The level element in the buffer array is checked and the appropriate object instance is genreted and returend
	 * 
	 * @param buffer array that must have 6 elements { timeStamp , level , process , object , message , exception}
	 * @param serviceId The Id of the Service (File) that generated the log entry
 	 * @return a {@link LogEnry} or {@link LogEntryError} depending on logs level value
	 */
	public LogEntry getLogEntry(String[] buffer,int serviceId) {
		LogEntry logEntry = null;
		if(buffer.length != 6) {
			logger.error(getClass().getSimpleName()+" Unable to create LogEntry: insufficient passed attributes in the Array");
		}else {
			switch (buffer[1].toUpperCase()) {								//Differentiate between the different LogEntry classes
			case "ERROR":								//Create LogEntryError instance
				logger.debug(getClass().getSimpleName()+" Creating a LogEntryError");
				logEntry = new LogEntryError(buffer[0],buffer[1],buffer[2],buffer[3],buffer[4], buffer[5],serviceId);
				break;
			case "INFO":								//Create LogEntry instance
				logger.debug(getClass().getSimpleName()+" Creating a LogEntry");
				logEntry = new LogEntry(buffer[0],buffer[1],buffer[2],buffer[3],buffer[4],serviceId);
				break;
			case "WARN":								//Create LogEntry instance (there isnt WARN class)
				logger.debug(getClass().getSimpleName()+" Creating a LogEntry");
				logEntry = new LogEntry(buffer[0],buffer[1],buffer[2],buffer[3],buffer[4],serviceId);
				break;
			default:									//unacceptable Level value
				logEntry=null;							
				break;
			}
		}
		return logEntry;
	}
}
