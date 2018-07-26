package com.example.demo.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.TagDAO;
import com.example.demo.model.Tag;
import com.example.demo.singleton.LoggerSingleton;

@Service
public class TagService implements TagDAO {

	@Autowired
	private EntityManager entityManagerFactor;
	final static Logger logger = LoggerSingleton.getLoggerOBJ().getLoggerman();

	
	/**
	 * {@inheritDoc}<br>
	 * uses persist of entityManagerFactor to save to the database.
	 * @see com.example.demo.dao.TagDAO#saveTag(com.example.demo.model.Tag)
	 */
	@Override
	@Transactional
	public void saveTag(Tag tag) {
		try {
			entityManagerFactor.persist(tag);
			logger.info(getClass().getSimpleName() + " save(" + tag.getTag() + ") executed successfully ");
		} catch (Exception e) {
			logger.info(getClass().getSimpleName() + " save(" + tag.getTag() + ") Failed,  " + e.getMessage());
		}

	}

	/** 
	 * {@inheritDoc}<br>
	 * 
	 * @see com.example.demo.dao.TagDAO#updateTag(com.example.demo.model.Tag)
	 */
	@Override
	public void updateTag(Tag tag) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.example.demo.dao.TagDAO#findAllTags()
	 */
	@Override
	public List<Tag> findAllTags() {
		List<Tag> list = null;
		try {
			list = entityManagerFactor.createQuery("select a from Tag a").getResultList();
			logger.info(getClass().getSimpleName() + " findAllTags() executed successfully, returned " + list.size()
					+ " Tags");
		} catch (Exception e) {
			logger.info(getClass().getSimpleName() + " findAllTags() Faield , " + e.getMessage());
		}
		return list;
	}

	/**{@inheritDoc}
	 * @see com.example.demo.dao.TagDAO#findTagById(int)
	 */
	@Override
	public Tag findTagById(int id) {
		Tag tag = null;
		try {
			tag = entityManagerFactor.find(Tag.class, id);
			logger.info(getClass().getSimpleName() + " findTagById(" + id + ") executed successfully.");
		} catch (Exception e) {
			logger.info(getClass().getSimpleName() + " findTagById(" + id + ") Faield , " + e.getMessage());
		}
		return tag;
	}

	/**{@inheritDoc}
	 * @see com.example.demo.dao.TagDAO#deleteTag(int)
	 */
	@Override
	@Transactional
	public void deleteTag(int id) {
		Tag tag;
		try {
			tag = entityManagerFactor.find(Tag.class,id);
			entityManagerFactor.remove(tag);
			logger.info(getClass().getSimpleName() + " deleteTag(" + id + ") executed successfully.");
		} catch (Exception e) {
			logger.info(getClass().getSimpleName() + " deleteTag(" + id + ") Faield , " + e.getMessage());
		}
	}
}
