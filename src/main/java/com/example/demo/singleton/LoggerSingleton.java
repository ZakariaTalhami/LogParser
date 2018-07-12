package com.example.demo.singleton;

import org.apache.log4j.Logger;

public class LoggerSingleton {

	private static LoggerSingleton loggerOBJ = new LoggerSingleton();
	final static Logger loggerman = Logger.getLogger(LoggerSingleton.class);
	
	private LoggerSingleton() {
		
	}

	public static LoggerSingleton getLoggerOBJ() {
		return loggerOBJ;
	}

	public static void setLoggerOBJ(LoggerSingleton logger) {
		LoggerSingleton.loggerOBJ = logger;
	}

	public static Logger getLoggerman() {
		return loggerman;
	}

	
	public void logInfo() {
		
	}
	
	
	
	
	
}
