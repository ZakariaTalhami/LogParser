package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.example.demo.model.LogEntry;
import com.example.demo.model.LogEntryFactory;
import com.example.demo.singleton.LoggerSingleton;


/**
 * 	Parser Class, used to hold the Log string and split it, to generate the list of {@link LogEntry},
 * which are also stored in the class.<br>
 * The {@link LogEntry} are created using {@link LogEntryFactory}
 * 
 * @author Zakaria Talhami
 *
 */
public class LogParser {

	private String Log;
	private int serviceId;
	private List<LogEntry> EntryList = new ArrayList<>();
	private int nextIndex=0;
	private Logger logger = LoggerSingleton.getLoggerOBJ().getLoggerman();
	
	private LogEntryFactory entryFactory = new LogEntryFactory();
	
	public LogParser() {
	}

	public String getLog() {
		return Log;
	}

	public void setLog(String log) {
		Log = log.trim();
	}

	public List<LogEntry> getEntryList() {
		return EntryList;
	}

	public void setEntryList(List<LogEntry> entryList) {
		EntryList = entryList;
	}
	
	
	public int getServiceId() {
		return serviceId;
	}
	
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;

	}


	
	/**
	 * Parses a log string of one line and returns the LogEntry<br>
	 * <b>Note:</b>Mehtod doesnt work with Error Entries only entries with one line
	 * 
	 * @return Parsed LogEntry
	 */
	@Deprecated
	public LogEntry parseOneEntry() {
		LogEntry entry = null;
		String [] buffer;
		String timeStamp,lvl,process,object,message;
		buffer = Log.split("\\|");
		timeStamp = buffer[0];
		lvl = buffer[1];
		process = buffer[2];
		object = buffer[4];
		message = buffer[5];
		
		System.out.println("Time => "+timeStamp);
		System.out.println("lvl => "+lvl);
		System.out.println("process => "+process);
		System.out.println("object => "+object);
		System.out.println("message => "+message);
		return entry;
	}
	
	
	
	/**
	 * 	Splits and parses the log string provided in the class log attribute and returns a list of parsed
	 *  log Entries as a list object of class {@link LogEntry}, moreover the result is also store in the 
	 *  class
	 * 	{@link LogEntryFactory} is use to create the correct LogEntry instance
	 * 
	 * 
	 * @return List of the log entries resulted from the parsing of the classes log attribute 
	 */
	public List<LogEntry> ParseAll(){
		List<LogEntry> entries = new ArrayList<>();
		String [] buffer;
		String timeStamp,lvl,process,object,message;
		String exception = "";

		//Loop until all the log String has been parsed
		while (!Log.isEmpty()) {
			buffer = Log.split("\\|" , 7);						//split by "|"
			timeStamp = buffer[0].trim();
			lvl = buffer[1].trim();
			process = buffer[2].trim();							//skip "| |"
			object = buffer[4].trim();
			message = buffer[5].trim();
			setLog(buffer[6].trim());							//Remove the entry from log string
			exception = "";
			
//			Special Handling of ERROR level for the log
			if(lvl.equalsIgnoreCase("ERROR")) {
				Matcher matcher = Pattern.compile("((\\d{4})-(\\d{2})-(\\d{2}))").matcher(Log);
				buffer = Log.split("((\\d{4})-(\\d{2})-(\\d{2}))", 2);				//Split and match by the date format
				exception = buffer[0].trim();					//Get the exception from the log
				logger.debug(getClass().getSimpleName()+"   first => "+buffer[0]);
				logger.debug(getClass().getSimpleName()+"   second => "+buffer[1]);
				if(matcher.find()) {							//Retrieve the delimiter
					buffer[1] = matcher.group(1)+buffer[1];		//Re-insert the delimiter back into the  log
				}
				setLog(buffer[1]);								//Remove the exception from the log String
			}
			//log the LogEntryFactory to create the correct instance of LogEntry 
			EntryList.add(entryFactory.getLogEntry(new String[] {timeStamp,lvl,process,object,message,exception},serviceId));
			
		}
		
		return entries;
	}
	
	
}
