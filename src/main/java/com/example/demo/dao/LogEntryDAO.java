package com.example.demo.dao;

import java.util.Date;
import java.util.List;

import com.example.demo.model.LogEntry;
import com.example.demo.model.LogEntryError;

public interface LogEntryDAO {
	
	/**
	 * Saves a LogEntry (Even a LogEntryError) into the database.<br>
	 * logEntry shouldn't be null.
	 * 
	 * @param logEntry the Model to be saved to the database.
	 */
	void save(LogEntry logEntry);

	/**
	 * Queries the database and returns all the logEntries in the database.
	 * 
	 * @return  List of LogEntry from the database
	 */
	List<LogEntry> findAll();

	
	/**
	 * Queries the database and returns all the logEntries with a specific Thread value.
	 * 
	 * @param thread the string to filer logEntry by Thread clm
	 * @return List of all log entries of with thread name
	 */
	List<LogEntry> findAllByThread(String thread);

	
	/**
	 * Queries the database and returns all the Error Log entries.
	 * 
	 * @return List of all the Error logEntries in the database
	 */
	List<LogEntryError> findAllErrors();

	
	/**
	 * Queries the database and returns Log Entry with a specific ID.
	 * 
	 * @param id The Id of the LogEntry to find in the database
	 * @return Log entry with specified ID
	 */
	LogEntry findById(int id);

	
	/**
	 * Parses the log entries in the provided log file, 
	 * convert to logEntry models and save to database.
	 * 
	 * @param LogName Name of the log file to be parsed
	 */
	void parseLog(String LogName);

	
	/**
	 * Save the collection of LogEntries to the database.
	 * 
	 * @param entries List of LogEntry to be saved to the database.
	 */
	void saveAllEntries(List<LogEntry> entries);

	
	
	/**
	 * Query the data base with filtering out any logEntries not within the specified range.
	 * 
	 * @param min minimum timestamp
	 * @param max maximum timestamp
	 * @return	Log of Entries that are within the specified range.
	 */
	List<LogEntry> findByRange(Date min, Date max);

	
	/**
	 * Query the database and group by the Thread clm, returns the Thread value alone.
	 * 
	 * @return List of all the Thread names in the database
	 */
	List<String> getThreadList();

	
	/**
	 * Query the database and group by the className clm, returns the className value alone.
	 * 
	 * @return List of all the Class names in the database
	 */
	List<String> getClassList();

	
	
	/**
	 * Used to generate a custom query to the database, allowing filter by all the columns<br>
	 * method also provides paging, by sending the required page number.
	 * 
	 * @param page  Required page of logEntries
	 * @param level	Filter value for Level
	 * @param file Filter value for log file(service)
	 * @param maxTime Filter value for upper limit timestamp
	 * @param minTime Filter value for lower limit timestamp
	 * @param thread Filter value for Thread
	 * @param className Filter value for class name
	 * @return Filter result of LogEntries 
	 */
	List<LogEntry> getPage(int page, String level, int file, String maxTime, String minTime, String thread,
			String className);

	
	/**
	 * Adds a tag value to a log entry, LogEntry of logId is tagged with tag of tagId
	 * 
	 * @param logId  ID of the logEntry to be tagged
	 * @param tagId  ID of the tag 
	 */
	void addTag(int logId, int tagId);

	/**
	 * Remove a tag value from a log entry, LogEntry of logId has its tag removed
	 * 
	 * @param logId  ID of the logEntry 
	 */
	void removeTag(int logId);
	
	
	
}
