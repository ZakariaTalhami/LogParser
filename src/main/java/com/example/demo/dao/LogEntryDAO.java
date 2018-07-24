package com.example.demo.dao;

import java.util.Date;
import java.util.List;

import com.example.demo.model.LogEntry;
import com.example.demo.model.LogEntryError;

public interface LogEntryDAO {
	
	void save(LogEntry logEntry);

	void saveError(LogEntryError entryError);

	List<LogEntry> findAll();

	List<LogEntry> findAllByThread(String thread);

	List<LogEntryError> findAllErrors();

	LogEntry findById(int id);

	LogEntryError findErrorById(int id);

	void parseLog(String LogName);

	void saveAllEntries(List<LogEntry> entries);

	List<LogEntry> findByRange(Date min, Date max);

	List<String> getThreadList();

	List<String> getClassList();

	List<LogEntry> getPage(int page, String level, int file, String maxTime, String minTime, String thread,
			String className);

	void addTag(int logId, int tagId);

	void removeTag(int logId);
	
	
	
}
