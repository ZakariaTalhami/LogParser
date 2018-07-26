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
import com.example.demo.model.Tag;
import com.example.demo.singleton.LoggerSingleton;


/**
 * Provides services for handling log entries, which include saving and finding in their various forms.
 * Also has methods for parsing log files 
 *
 * @author Zakaria
 * @see LogEntryDAO
 */
@Service
public class LogService implements LogEntryDAO {

	@Autowired
	private EntityManager entityManagerFactor;
	@Autowired
	private LogParserServies parser;
	@Autowired
	private TagService tagService;
	
	final static Logger logger = LoggerSingleton.getLoggerOBJ().getLoggerman();
	
	
	/**
	 * {@inheritDoc}<br>
	 * Uses entityManagerFactorto save the LogEntries, should deal with the polymorphism,
	 * and save for both LogEntry and LogEntryError
	 * @see com.example.demo.dao.LogEntryDAO#save(com.example.demo.model.LogEntry)
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



	/**
	 * {@inheritDoc}<br>
	 *  Uses createQuery of entityManagerFactor to query the database with :<br>
	 *  		"select a from LogEntries a "
	 * @see com.example.demo.dao.LogEntryDAO#findAll()
	 */
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


	
	
	/**
	 * {@inheritDoc}<br>
	 * Uses createQuery of entityManagerFactor to query the database with :<br>
	 *  		"select a from LogEntries a where a.process like :wantedThread"
	 * @see com.example.demo.dao.LogEntryDAO#findAllByThread(java.lang.String)
	 */
	@Override
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


	/**
	 * {@inheritDoc}<br>
	 * Uses createQuery of entityManagerFactor to query the database with :<br>
	 *  		"select a from LogEntryError a"
	 * @see com.example.demo.dao.LogEntryDAO#findAllErrors()
	 */
	@Override
	public List<LogEntryError> findAllErrors() {
		List<LogEntryError> entryErrors = null;
		entryErrors = entityManagerFactor.createQuery("select a from LogEntryError a").getResultList();
		try {

		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + " findAllErrors() failed, " + e.getMessage());
		}

		return entryErrors;
	}

	/**
	 * {@inheritDoc}<br>
	 * Uses find of entityManagerFactor to query the database.
	 */
	@Override
	public LogEntry findById(int id) {
		LogEntry logEntry = null;
		try {
			logEntry = entityManagerFactor.find(LogEntry.class, id);			//Specify return model type
			logger.info(getClass().getSimpleName() + "  find(" + id + ") LogEntry Successfully ");
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + "  find(" + id + ") Failed. " + e.getMessage());
		}
		return logEntry;
	}

	/**
	 * {@inheritDoc}<br>
	 * Uses LogParseServices to parse the log file, passing the filename.
	 */
	@Override
	public void parseLog(String LogName) {
		parser.Parse(LogName);
	}

	/**
	 * {@inheritDoc}<br>
	 * Iterates through the entry list, passing them to {@link #save(LogEntry)}, 
	 * saving them to the database.
	 */
	@Override
	@Transactional
	public void saveAllEntries(List<LogEntry> entries) {

		for (int i = 0; i < entries.size(); i++) {
			save(entries.get(i));
		}
	}

	/** 
	 * {@inheritDoc}<br>
	 * Create a Criteria Query, then adding restrictions less and greater for the max and min timestamp respectively.
	 */
	@Override
	public List<LogEntry> findByRange(Date min, Date max) {
		List<LogEntry> entries = null;
		try {
			Session sess = entityManagerFactor.unwrap(Session.class);				//Extracting Session object
			entries = sess.createCriteria(LogEntry.class).add(Restrictions.le("timestamp", max))	
					.add(Restrictions.ge("timestamp", min)).list();									//adding criteria restrictions
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(getClass().getSimpleName() + "  findByRange() Failed. " + e.getMessage());
		}

		return entries;
	}
	/**
	 * {@inheritDoc}<br>
	 * Used to generate HTML selects for Threads from the front end.
	 * Uses createQuery of entityManagerFactor to query the database with :<br>
	 *  		"select a.process from LogEntries a group by a.process"
	 */
	@Override
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
	
	/**
	 * {@inheritDoc}<br>
	 * Used to generate HTML selects for Class from the front end.
	 * Uses createQuery of entityManagerFactor to query the database with :<br>
	 *  		"select a.className from LogEntries a group by a.className"
	 */
	@Override
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


	
	
	/**
	 * {@inheritDoc}<br>
	 * 	uses getCriteriaBuilder to build the query
	 * @see com.example.demo.dao.LogEntryDAO#getPage(int, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
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
				
				if (level !=null && !level.isEmpty()) {											//if level filter specified
					p.add(builder.and(builder.like(myRoot.get("level"), level)));				//add restriction 
				}
				
				if (thread !=null && !thread.isEmpty()) {										//if thread filter specified
					p.add(builder.and(builder.like(myRoot.get("process"), thread)));			//add restriction 
				}
				
				if (className !=null && !className.isEmpty()) {									//if class filter specified
					p.add(builder.and(builder.like(myRoot.get("className"), className)));		//add restriction 
				}	
				
				if(file > 0) {																	//if service file filter specified
					p.add(builder.and(builder.equal(myRoot.get("service").get("id"), file)));	//add restriction 
				}
				
				if(minTime!= null && !minTime.isEmpty()) {										//if min timestamp filter specified
					logger.info(getClass().getSimpleName()+" minTime = "+minTime);
					LogEntry min = new LogEntry(minTime,"","","","");							//Use LogEntry to format the date
					SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");//Format timestamp with milliseconds
					logger.info(getClass().getSimpleName()+" minTime = "+formater.format(min.getTimestamp())+","+min.getmSec());
																								//add restriction 
					p.add(builder.and(builder.greaterThanOrEqualTo(myRoot.<Date>get("timestamp"), min.getTimestamp())));
				}
				
				if(maxTime!= null && !maxTime.isEmpty()) {										//if max timestamp filter specified
					logger.info(getClass().getSimpleName()+" maxTime = "+maxTime);
					LogEntry max = new LogEntry(maxTime,"","","","");							//Use LogEntry to format the date
					SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");//Format timestamp with milliseconds
					logger.info(getClass().getSimpleName()+" maxTime = "+formater.format(max.getTimestamp())+","+max.getmSec());
																								//add restriction 
					p.add(builder.and(builder.lessThanOrEqualTo(myRoot.<Date>get("timestamp"), max.getTimestamp())));
				}
				
				//Convert ArrayList to normal array
				Predicate[] pr = new Predicate[p.size()];									
				
				//finish the query and return the result
				criteria.select(myRoot).where(p.toArray(pr));
				entries = entityManagerFactor.createQuery(criteria).setFirstResult(offset).setMaxResults(limit).getResultList();
				logger.info(getClass().getSimpleName() + " getPage(" + page + ") executed successfully, returned "
						+ entries.size()+" entries");
			} catch (Exception e) {
				logger.error(getClass().getSimpleName() + " getPage(" + page + ") Failed, " + e.getMessage());
			}
		}
		return entries;
	}

	/**
	 * {@inheritDoc}<br>
	 * uses {@link TagService#findTagById(int)} to get the Tag, and 
	 * {@link #findById(int)} to get the log entry. The tag is then added to the 
	 * logEntry then updated back to the database.
	 */
	@Override
	@Transactional
	public void addTag(int logId, int tagId) {
		Tag tag= null;
		LogEntry entry = null;
		try {
			tag = tagService.findTagById(tagId);					//Get tag from ID
			entry = this.findById(logId);							//Get log entry by ID
			entry.setTag(tag);										//Add the tag
			this.save(entry);										//Update database
			logger.info(getClass().getSimpleName()+"addTag("+logId+" , "+tagId+") executed successfully");
		} catch (Exception e) {
			logger.info(getClass().getSimpleName()+"addTag("+logId+" , "+tagId+") Failed, "+e.getMessage());
		}
		
	}

	/**
	 * {@inheritDoc}<br>
	 * Uses {@link #findById(int)} to get the log entry. 
	 * sets the Tag value to null and update the database.
	 */
	@Override
	@Transactional
	public void removeTag(int logId) {
		LogEntry entry = null;
		try {
			entry = this.findById(logId);							//Get log entry
			entry.setTag(null);										//remove tag
			this.save(entry);										//update database
			logger.info(getClass().getSimpleName()+"removeTag("+logId+") executed successfully");
		} catch (Exception e) {
			logger.info(getClass().getSimpleName()+"removeTag("+logId+") failed,  "+e.getMessage());
		}
		
	}
	
	
	

}
