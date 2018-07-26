package com.example.demo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.LogParser;
import com.example.demo.model.LogEntry;
import com.example.demo.model.ServiceModel;
import com.example.demo.singleton.LoggerSingleton;

/**
 *  Provides log Parsing services, with on publicly accessible method (Parse), which 
 *  Takes a filename, opens it reads the logs and converts them to LogEntries and saves them
 *  to the database.
 * 
 * 
 * @author Zakaria Talhami
 *
 */
@Service
public class LogParserServies {

	final static Logger logger = LoggerSingleton.getLoggerOBJ().getLoggerman();
	
	@Autowired
	private LogService logServicer;
	@Autowired
	private ServiceService serviceHandler;

	
	/**
	 * Open and read the log a log file, extracts the logs as string, converts the string
	 * into a List of {@link LogEntry}. using the {@link LogService} saves the resulting
	 * LogEntry list into the database.
	 *  
	 * The LogName must be the name of the log file, that is recorded in the database. The
	 * File name is checked to see if it exists in the database. If exists in the database 
	 * the ID is retrieved, which is used to link the entries to the service( log file)
	 * 
	 * @param LogName Name of the log file to be parsed
	 */
	public void Parse(String LogName) {
		String log;
		ServiceModel service;
		if(serviceHandler.exist(LogName)) {								//check if the log file exists
			service = serviceHandler.findByName(LogName);				//Id of the log file
			log = openLog(LogName);										//Get the log entries as string
			logger.info(getClass().getSimpleName()+" Parse Read "+log.length()+" lines from the log file");
			//Create a LogParser, set the log string and generate the LogEntry List
			LogParser parser = new LogParser();
			parser.setLog(log);
			parser.setServiceId(service.getId());
			parser.ParseAll();
			logServicer.saveAllEntries(parser.getEntryList());			//Persist logEntries
		}else {
			/*
			 * Can be used to check file system and update the database for a new log file
			 * 
			 */
		}
		
	}
	
	
	/**
	 * Opens the provided log file and read the entire file, returning the result as string
	 * 
	 * @param LogName  Name of the log file to be Read
	 * @return	The entire content of the log file as a string 
	 */
	public String openLog(String LogName) {
		String log="";
		File file = new File("log\\"+LogName+".log");

		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			logger.info(getClass().getSimpleName()+" log:"+LogName+" was opened successfully");
			char[] data = new char[(int)file.length()];					//Read the entire log file all at once
			reader.read(data);
			log = new String(data);										//Convert Array of char to string 
			reader.close();
			
		} catch (Exception e) {
			logger.info(getClass().getSimpleName()+" log:"+LogName+" Failed to open. "+e.getMessage());
		}
		
		return log;
	}
	

}
