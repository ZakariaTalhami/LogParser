package com.example.demo.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.LogEntryDAO;
import com.example.demo.model.LogEntry;
import com.example.demo.model.LogEntryError;
import com.example.demo.singleton.LoggerSingleton;

@Service
public class LogService implements LogEntryDAO {

	@Autowired
	private EntityManager entityManagerFactor;
	@Autowired
	private LogParserServies parser;
	final static Logger logger = LoggerSingleton.getLoggerOBJ().getLoggerman();

	/*
	 * @see com.example.demo.dao.LogEntryDAO#save(com.example.demo.model.LogEntry)
	 * 			Saves the LogEntries, should in theory deal with the polymorphism
	 */
	@Override
	@Transactional
	public void save(LogEntry logEntry) {

		try {
			entityManagerFactor.persist(logEntry);
			logger.info(getClass().getSimpleName()+" save(logEntry) executed Successfully");
//			logger.debug("Persisted LogEntity: "+logEntry.toString());
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + " save(logEntry) Failed, " + e.getMessage());
		}
	}

	@Transactional
	@Deprecated
	public void saveError(LogEntryError entryError) {
		try {
			entityManagerFactor.persist(entryError);
//			logger.info(getClass().getSimpleName()+" save(logEntry) executed Successfully");
//			logger.debug("Persisted LogEntity: "+logEntry.toString());
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + " saveError(entryError) Failed, " + e.getMessage());
		}
	}

	@Override
	public List<LogEntry> findAll() {
		List<LogEntry> entries = null
				;
		try {
			entries = entityManagerFactor.createQuery("select a from LogEntries a ").setMaxResults(10).getResultList();
		} catch (Exception e) {
			logger.error(getClass().getSimpleName()+" findAll() failed, "+e.getMessage());
		}
		
		return entries;
	}
	
	public List<LogEntry> findAllByThread(String thread) {
		List<LogEntry> entries = null
				;
		try {
			entries = entityManagerFactor.createQuery("select a from LogEntries a where a.process like :wantedThread").setParameter("wantedThread", thread).getResultList();
		} catch (Exception e) {
			logger.error(getClass().getSimpleName()+" findAll() failed, "+e.getMessage());
		}
		
		return entries;
	}
	
	public List<LogEntryError> findAllErrors(){
		List<LogEntryError> entryErrors = null;
		entryErrors = entityManagerFactor.createQuery("select a from LogEntryError a").getResultList();
		try {
			
		} catch (Exception e) {
			logger.error(getClass().getSimpleName()+" findAllErrors() failed, "+e.getMessage());
		}
			
		return entryErrors;
	}	
	

	@Override
	public LogEntry findById(int id) {
		LogEntry logEntry = null;
		try {
			logEntry = entityManagerFactor.find(LogEntry.class, id);
			logger.info(getClass().getSimpleName() + "  find(" + id + ") LogEntry Successfully ");
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + "  find(" + id + ") Failed. " + e.getMessage());
		}

		return logEntry;
	}

	@Override
	@Deprecated
	public LogEntryError findErrorById(int id) {
		//
		LogEntryError entryError = null;
		try {
			entryError = entityManagerFactor.find(LogEntryError.class, id);
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + "  findErrorById(" + id + ") Failed. " + e.getMessage());
		}
		return entryError;
	}


	public void parseLog(String LogName) {
		parser.Parse(LogName);
	}

	@Transactional
	public void saveAllEntries(List<LogEntry> entries) {

		for (int i = 0; i < entries.size(); i++) {
			save(entries.get(i));
		}
	}
	
	public List<LogEntry> findByRange(Date min , Date max) {
		List<LogEntry> entries = null;
		try {
			Session sess = entityManagerFactor.unwrap(Session.class);
			entries = sess.createCriteria(LogEntry.class).add(Restrictions.le("timestamp", max)).add(Restrictions.ge("timestamp", min)).list();
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(getClass().getSimpleName() + "  findByRange() Failed. " + e.getMessage());
		}
		
		return entries;
	}

}
