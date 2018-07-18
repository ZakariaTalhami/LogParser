package com.example.demo.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
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
	 * Saves the LogEntries, should in theory deal with the polymorphism
	 */
	@Override
	@Transactional
	public void save(LogEntry logEntry) {

		try {
			entityManagerFactor.persist(logEntry);
			logger.info(getClass().getSimpleName() + " save(logEntry) executed Successfully");
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
		List<LogEntry> entries = null;
		try {
			entries = entityManagerFactor.createQuery("select a from LogEntries a ").setMaxResults(10).getResultList();
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + " findAll() failed, " + e.getMessage());
		}

		return entries;
	}

	public List<LogEntry> findAllByThread(String thread) {
		List<LogEntry> entries = null;
		try {
			entries = entityManagerFactor.createQuery("select a from LogEntries a where a.process like :wantedThread")
					.setParameter("wantedThread", thread).getResultList();
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + " findAll() failed, " + e.getMessage());
		}

		return entries;
	}

	public List<LogEntryError> findAllErrors() {
		List<LogEntryError> entryErrors = null;
		entryErrors = entityManagerFactor.createQuery("select a from LogEntryError a").getResultList();
		try {

		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + " findAllErrors() failed, " + e.getMessage());
		}

		return entryErrors;
	}

	@Override
	public LogEntry findById(int id) {
		LogEntry logEntry = null;
		try {
			logEntry = entityManagerFactor.find(LogEntry.class, id);
			logger.info(getClass().getSimpleName() + "  find(" + id + ") LogEntry Successfully ");
			logger.info(getClass().getSimpleName() + "  find(" + id + ") LogEntry Successfully "+logEntry.getTimestamp());
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

	public List<LogEntry> findByRange(Date min, Date max) {
		List<LogEntry> entries = null;
		try {
			Session sess = entityManagerFactor.unwrap(Session.class);
			entries = sess.createCriteria(LogEntry.class).add(Restrictions.le("timestamp", max))
					.add(Restrictions.ge("timestamp", min)).list();

		} catch (Exception e) {
			// TODO: handle exception
			logger.error(getClass().getSimpleName() + "  findByRange() Failed. " + e.getMessage());
		}

		return entries;
	}
	public List<String> getThreadList(){
		List<String> list=null;
		try {
//			select a.log_process from log_entries a group by a.log_process;
			list = entityManagerFactor.createQuery("select a.process from LogEntries a group by a.process").getResultList();
			logger.info(getClass().getSimpleName()+" getThreadList() executed successfully, returned "+list.size()+" Threads");
		} catch (Exception e) {
			// TODO: handle exception
			logger.info(getClass().getSimpleName()+" getThreadList() Failed, "+e.getMessage());
		}
		return list;
	}
	
	public List<String> getClassList(){
		List<String> list=null;
		try {
//			select a.log_process from log_entries a group by a.log_process;
			list = entityManagerFactor.createQuery("select a.className from LogEntries a group by a.className").getResultList();
			logger.info(getClass().getSimpleName()+" getThreadList() executed successfully, returned "+list.size()+" Classes");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}


//	public List<LogEntry> getPage(int page, String level) {
//		List<LogEntry> entries = null;
//		int limit = 50;
//		int offset = (page-1)*limit;
//		String levelFilter="";
//		String whereCondition="";
//		if (page > 0) {
//			try {
//				// Filter by LEVEL
//				if (level != null && !level.isEmpty()) {
//					if (whereCondition.isEmpty()) {
//						whereCondition = " where ";
//					}else {
//						levelFilter += " and ";
//					}
//					levelFilter += "a.level like '"+level+"' " ;
//				}
//				
//				// Creating the Query
//				entries = entityManagerFactor.createQuery("select a from LogEntries a "+whereCondition+levelFilter).setFirstResult(offset)
//						.setMaxResults(limit).getResultList();
//				logger.info(getClass().getSimpleName() + " getPage(" + page + ") executed successfully, returned "
//						+ entries.size()+" entries");
//			} catch (Exception e) {
//				logger.error(getClass().getSimpleName() + " getPage(" + page + ") Failed, " + e.getMessage());
//			}
//		}
//		return entries;
//	}
//	
	
	
	public List<LogEntry> getPage(int page, String level,int file,String maxTime ,String minTime ,String thread, String className ) {
		List<LogEntry> entries = null;
		int limit = 50;
		int offset = (page-1)*limit;
		String levelFilter="";
		String whereCondition="";
		if (page > 0) {
			try {
				// Getting criteriaBuilder
				Session sess = entityManagerFactor.unwrap(Session.class);
				CriteriaBuilder builder = sess.getCriteriaBuilder();
				
				//Creating a criteriaQuery
				CriteriaQuery<LogEntry> criteria = builder.createQuery(LogEntry.class);
				Root<LogEntry> myRoot = criteria.from(LogEntry.class);
				
				//Creating all the constraints and filters
				List <Predicate> p = new ArrayList<Predicate> ();
				if (level !=null && !level.isEmpty()) {
					p.add(builder.and(builder.like(myRoot.get("level"), level)));
				}
				if (thread !=null && !thread.isEmpty()) {
					p.add(builder.and(builder.like(myRoot.get("process"), thread)));
				}
				if (className !=null && !className.isEmpty()) {
					p.add(builder.and(builder.like(myRoot.get("className"), className)));
				}
				if(file > 0) {
					p.add(builder.and(builder.equal(myRoot.get("service").get("id"), file)));
				}
//				if(minTime!= null && !minTime.isEmpty()) {
//					logger.info(getClass().getSimpleName()+" minTime = "+minTime);
//					LogEntry min = new LogEntry(minTime,"","","","");
//					SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
//					logger.info(getClass().getSimpleName()+" minTime = "+formater.format(min.getTimestamp())+","+min.getmSec());
//					
//					p.add(builder.and(builder.equal(myRoot.<Date>get("timestamp"), min.getTimestamp())));
//				}
				Predicate[] pr = new Predicate[p.size()];
				
				//finish the query and return the result
				criteria.select(myRoot).where(p.toArray(pr));
				entries = entityManagerFactor.createQuery(criteria).setFirstResult(offset).setMaxResults(limit).getResultList();
//				logger.info(getClass().getSimpleName()+" QUery"+entityManagerFactor.createQuery(criteria).);
				logger.info(getClass().getSimpleName() + " getPage(" + page + ") executed successfully, returned "
						+ entries.size()+" entries");
			} catch (Exception e) {
				logger.error(getClass().getSimpleName() + " getPage(" + page + ") Failed, " + e.getMessage());
			}
		}
		return entries;
	}
	
	
//	public List<MyObject> listAllForIds(List<Long> ids) {
//
//	    CriteriaBuilder builder = getSessionFactory().getCurrentSession().getCriteriaBuilder();
//	    CriteriaQuery<MyObject> criteria = builder.createQuery(MyObject.class);
//	    Root<MyObject> myObjectRoot = criteria.from(MyObject.class);
//	    Join<MyObject, JoinObject> joinObject = myObjectRoot.join("joinObject");
//
//	    Predicate likeRestriction = builder.and(
//	            builder.notLike( myObjectRoot.get("name"), "%string1"),
//	            builder.notLike( myObjectRoot.get("name"), "%string2")
//	    );
//
//	    criteria.select(myObjectRoot).where(joinObject.get("id").in(ids), likeRestriction);
//
//	    TypedQuery<MyObject> query = getSessionFactory().getCurrentSession().createQuery(criteria);
//
//	    return query.getResultList();
//	}

}
