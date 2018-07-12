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
import com.example.demo.model.ServiceModel;
import com.example.demo.singleton.LoggerSingleton;

@Service
public class LogParserServies {

	final static Logger logger = LoggerSingleton.getLoggerOBJ().getLoggerman();
	
	@Autowired
	private LogService logServicer;
	@Autowired
	private ServiceService serviceHandler;

	/*
	 * Parse(String)
	 * 		Reads the log file provided from the passed parameter, 
	 * 		parses the data and then creates and stores the objects in the database
	 */
	public void Parse(String LogName) {
		//Make the log global?
		String log;
		ServiceModel service;
		if(serviceHandler.exist(LogName)) {
			service = serviceHandler.findByName(LogName);
			log = openLog(LogName);							//Get the log entries as string
			logger.info(getClass().getSimpleName()+" Parse Read "+log.length()+" lines from the log file");
			LogParser parser = new LogParser();
			parser.setLog(log);
			parser.setServiceId(service.getId());
			parser.ParseAll();
//			parser.printEntryList();	
			logServicer.saveAllEntries(parser.getEntryList());
		}else {
			
		}
		
	}
	
	
	
	
	/*
	 * openLog(String)
	 * 		Reads the log file provided from the passed parameter, 
	 * 		Returns the log file as a string
	 * 								(should change this if this is still considered good practice)
	 */
	private String openLog(String LogName) {
		String log="";
		File file = new File("log\\"+LogName+".log");

		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			logger.info(getClass().getSimpleName()+" log:"+LogName+" was opened successfully");
			char[] data = new char[(int)file.length()];					//Read the entire log file all at once
			reader.read(data);
			log = new String(data);
			reader.close();
			
		} catch (Exception e) {
			logger.info(getClass().getSimpleName()+" log:"+LogName+" Failed to open. "+e.getMessage());
		}
		
		return log;
	}
	

}
