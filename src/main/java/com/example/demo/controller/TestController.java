package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

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
		List<LogEntry> entry = null;
		if (factory == null) {
			return null;
		} else {
//			LogEntry log = new LogEntry("2018-07-09 11:45:46,529","LEVEL" ,"PROCESS","CLASS","MESSAGE");
//			if (log.getTimestamp()!=null) {
//				logger.info(getClass().getSimpleName()+" Log has a timestamp of "+log.getTimestamp().toString());
//			}
			entry = new ArrayList<>();
			entry.add(logService.findById(122667));
			entry.add(logService.findById(122758));
			entry = logService.findByRange(entry.get(0).getTimestamp(), entry.get(1).getTimestamp());
//			logService.save(log);
			return entry;
		}
	}

	@RequestMapping("/")
	public List<LogEntry> findAll() {
		logger.info(getClass().getSimpleName() + " Entered findAll()");
		return logService.findAll();
	}

	@RequestMapping("/thread/{thread}")
	public List<LogEntry> findAllByThread(@PathVariable String thread) {
		logger.info(getClass().getSimpleName() + " Entered findAllByThread(" + thread + ")");
		return logService.findAllByThread(thread);
	}

	@RequestMapping("/parser/{LogName}")
	public void ReadLog(@PathVariable String LogName) {
		logger.info(getClass().getSimpleName() + " Entered ReadLog(" + LogName + ")");
		logService.parseLog(LogName);
	}

	@RequestMapping("/{id}")
	public LogEntry findById(@PathVariable int id) {
		LogEntry entry = null;
		logger.info(getClass().getSimpleName() + " Entered findById(" + id + ").");
		entry = logService.findById(id);

		logger.info(getClass().getSimpleName() + " Entered findById(" + id + "). "+entry.getTimestamp());
		return entry;
// 		return logService.findById(id);
	}

	@RequestMapping("/error")
	public List<LogEntryError> findAllErrors() {
		logger.info(getClass().getSimpleName() + " Entered findAllErrors().");
		return logService.findAllErrors();
	}

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

	@RequestMapping("/page/{page}")
	public List<LogEntry> getPage(@PathVariable int page, @PathParam("level") String level, @PathParam("file") int file,
			@PathParam("maxTime") String maxTime, @PathParam("minTime") String minTime,
			@PathParam("thread") String thread, @PathParam("className") String className) {
		logger.info(getClass().getSimpleName() + " Entered getpage(" + page + "," + level + "," + file + ")");
		return logService.getPage(page, level, file, maxTime, minTime, thread, className);
	}

	@RequestMapping("/thread")
	public List<String> getAllThreadNames() {
		return logService.getThreadList();
	}

	@RequestMapping("/class")
	public List<String> getAllClassNames() {
		return logService.getClassList();
	}
}
