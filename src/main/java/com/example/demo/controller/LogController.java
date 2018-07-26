package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.LogEntry;
import com.example.demo.model.LogEntryError;
import com.example.demo.model.LogEntryFactory;
import com.example.demo.service.LogService;
import com.example.demo.singleton.LoggerSingleton;

@RestController
@RequestMapping("/log")
public class LogController {

	final static Logger logger = LoggerSingleton.getLoggerOBJ().getLoggerman();
//	@Autowired

	@Autowired
	private LogService logService;

	@Autowired
	private LogEntryFactory factory;

	/**
	 * Returns all the log entries in the database to the front end as json, 
	 * not really practical as the database can be too large to be sent all at once.
	 * @return  Json of all the Log entries
	 */
	@RequestMapping("/")
	public List<LogEntry> findAll() {
		logger.info(getClass().getSimpleName() + " Entered findAll()");
		return logService.findAll();
	}

	/**
	 * Return All log entries with specific thread name.
	 * 
	 * @param thread Thread name in url path
	 * @return json of all log entries with specific thread name
	 */
	@RequestMapping("/thread/{thread}")
	public List<LogEntry> findAllByThread(@PathVariable String thread) {
		logger.info(getClass().getSimpleName() + " Entered findAllByThread(" + thread + ")");
		return logService.findAllByThread(thread);
	}

	/**
	 * GET Request from user to parse a log file in the log folder
	 * 
	 * @param LogName log file name in url
	 */
	@RequestMapping("/parser/{LogName}")
	public void ReadLog(@PathVariable String LogName) {
		logger.info(getClass().getSimpleName() + " Entered ReadLog(" + LogName + ")");
		logService.parseLog(LogName);
	}

	
	/**
	 * GET Request for a specific Log Entry by ID
	 * 
	 * @param id	ID of logEntry in url
	 * @return JSON of LogEntry
	 */
	@RequestMapping("/{id}")
	public LogEntry findById(@PathVariable int id) {
		LogEntry entry = null;
		logger.info(getClass().getSimpleName() + " Entered findById(" + id + ").");
		entry = logService.findById(id);

		logger.info(getClass().getSimpleName() + " Entered findById(" + id + "). "+entry.getTimestamp());
		return entry;
// 		return logService.findById(id);
	}

	/**
	 * GET Request for all error log entries.
	 * @return JSON of all the error log entries.
	 */
	@RequestMapping("/error")
	public List<LogEntryError> findAllErrors() {
		logger.info(getClass().getSimpleName() + " Entered findAllErrors().");
		return logService.findAllErrors();
	}

	/**
	 * Get Request of all the log files within a timestamp range
	 * @param min	min Timestamp
	 * @param max	max Timesstamp
	 * @return	JSON of logEntries the range
	 */
	@RequestMapping("/range/{min}/{max}")
	public List<LogEntry> findByRange(@PathVariable String min, @PathVariable String max) {
		logger.info(getClass().getSimpleName() + " Entered findByRange(" + min + " , " + max + ")");
		List<LogEntry> entries = null;
		LogEntry minEntry, maxEntry;
		minEntry = new LogEntry();
		minEntry.setTimestamp(min);

		maxEntry = new LogEntry();
		maxEntry.setTimestamp(max);
		entries = logService.findByRange(minEntry.getTimestamp(), maxEntry.getTimestamp());
		return entries;
	}

	/**
	 * Get request to retrieve a filtered page of logs
	 * 
	 * @param page page
	 * @param level level
	 * @param file file
	 * @param maxTime maxTime
	 * @param minTime minTime
	 * @param thread thread
	 * @param className className
	 * @return JSON of the filtered log entries
	 */
	@RequestMapping("/page/{page}")
	public List<LogEntry> getPage(@PathVariable int page, @PathParam("level") String level, @PathParam("file") int file,
			@PathParam("maxTime") String maxTime, @PathParam("minTime") String minTime,
			@PathParam("thread") String thread, @PathParam("className") String className) {
		logger.info(getClass().getSimpleName() + " Entered getpage(" + page + "," + level + "," + file + ")");
		return logService.getPage(page, level, file, maxTime, minTime, thread, className);
	}

	/**
	 * GET request that returns all the Thread names in the database
	 * @return JSON of all thread names
	 */
	@RequestMapping("/thread")
	public List<String> getAllThreadNames() {
		return logService.getThreadList();
	}

	
	/**
	 * GET request that returns all the classes in the database
	 * @return JSON of all class names
	 */
	@RequestMapping("/class")
	public List<String> getAllClassNames() {
		return logService.getClassList();
	}
	
	
	/*
	 * these two should be PUT not GET
	 */
	
	/**
	 * GET request to add a tag to specific log entry
	 * @param logId Log ID
	 * @param tagId Tag ID
	 */
	@RequestMapping(value="/{logId}/tag/{tagId}")
	public void addTag(@PathVariable int logId , @PathVariable int tagId) {
		logService.addTag(logId, tagId);
	}
	
	
	/**
	 * GET request to remove a tag from a specific log entry
	 * @param logId Log ID
	 */
	@RequestMapping(value="/{logId}/tag", method=RequestMethod.PUT)
	public void removeTag(@PathVariable int logId ) {
		logService.removeTag(logId);
	}
}
