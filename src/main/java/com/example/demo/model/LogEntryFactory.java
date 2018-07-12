package com.example.demo.model;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.example.demo.singleton.LoggerSingleton;

@Component
public class LogEntryFactory {
 
	final static Logger logger = LoggerSingleton.getLoggerOBJ().getLoggerman();
	
	public LogEntry getLogEntry(String[] buffer) {
		LogEntry logEntry = null;
		if(buffer.length != 6) {
			logger.error(getClass().getSimpleName()+" Unable to create LogEntry: insufficient passed attributes in the Array");
		}else {
			switch (buffer[1].toUpperCase()) {
			case "ERROR":
				logger.debug(getClass().getSimpleName()+" Creating a LogEntryError");
				logEntry = new LogEntryError(buffer[0],buffer[1],buffer[2],buffer[3],buffer[4], buffer[5]);
				break;

			default:
				logger.debug(getClass().getSimpleName()+" Creating a LogEntry");
				logEntry = new LogEntry(buffer[0],buffer[1],buffer[2],buffer[3],buffer[4]);
				break;
			}
		}
		
		
		return logEntry;
	}
}
