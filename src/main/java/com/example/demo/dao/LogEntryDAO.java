package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.LogEntry;
import com.example.demo.model.LogEntryError;

public interface LogEntryDAO {
	
	public void save(LogEntry logEntry);
	public List<LogEntry> findAll();
	public LogEntry findById(int id);
	public LogEntryError findErrorById(int id);
	
	public void addTag(int logId , int tagId);
	public void removeTag(int logId );
	
}
