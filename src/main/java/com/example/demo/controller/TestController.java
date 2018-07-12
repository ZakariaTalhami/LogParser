package com.example.demo.controller;

import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.LogEntry;
import com.example.demo.model.LogEntryError;
import com.example.demo.model.LogEntryFactory;
import com.example.demo.service.LogService;
import com.example.demo.singleton.LoggerSingleton;

@RestController
@RequestMapping("/log")
public class TestController {
	
	final static Logger logger = LoggerSingleton.getLoggerOBJ().getLoggerman();
//	@Autowired

	
	@Autowired
	private LogService logService;
	
	@Autowired
	private LogEntryFactory factory;
	
	@RequestMapping("/test")
	public List<LogEntry> test() {
		List<LogEntry> entry=null;
		if (factory == null) {
			return null;
		} else {
//			entry = new ArrayList<>();
//			String [] buffer = new String[]{"test","Error","test","test","test","test"}; 
//			entry.add(factory.getLogEntry(buffer));
			entry = logService.findAllByThread("ew-1");
			return entry;
		}
	}
	
	@RequestMapping("/")
	public List<LogEntry> findAll(){
		return logService.findAll();
	}
	
	@RequestMapping("/thread/{thread}")
	public List<LogEntry> findAllByThread(@PathVariable String thread){
		return logService.findAllByThread(thread);
	}
	
	
	@RequestMapping("/parser/{LogName}")
	public void ReadLog(@PathVariable String LogName) {
		logger.info(getClass().getSimpleName()+" Entered ReadLog("+LogName+")");
		logService.parseLog(LogName);
	}
	
	@RequestMapping("/{id}")
	public LogEntry findById(@PathVariable int id) {
 		return logService.findById(id);
	}
	
	@RequestMapping("/error")
	public List<LogEntryError> findAllErrors(){
		return logService.findAllErrors();
	}
}






















