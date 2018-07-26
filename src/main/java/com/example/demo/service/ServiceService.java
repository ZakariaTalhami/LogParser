package com.example.demo.service;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.ServiceDAO;
import com.example.demo.model.ServiceModel;
import com.example.demo.singleton.LoggerSingleton;

import ch.qos.logback.core.joran.conditional.IfAction;

@Service
public class ServiceService implements ServiceDAO {

	final static Logger logger = LoggerSingleton.getLoggerOBJ().getLoggerman();

	@Autowired
	private EntityManager entityManager;

	/**
	 * {@inheritDoc}
	 * @see com.example.demo.dao.ServiceDAO#saveService(com.example.demo.model.ServiceModel)
	 */
	@Override
	@Transactional
	public void saveService(ServiceModel service) {
		try {
			entityManager.persist(service);
			logger.info(getClass().getSimpleName() + " saveService() executed successfully");
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + " saveService() has failed, " + e.getMessage());
		}

	}

	/**
	 * {@inheritDoc}
	 * @see com.example.demo.dao.ServiceDAO#findAll()
	 */
	@Override
	public List<ServiceModel> findAll() {
		List<ServiceModel> model = null;
		try {
			model = entityManager.createQuery("select a from Services a ").getResultList();
			logger.info(getClass().getSimpleName() + " findAll() executed successfully");
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + " findAll() has failed, " + e.getMessage());
		}
		return model;
	}

	/**
	 * {@inheritDoc}
	 * @see com.example.demo.dao.ServiceDAO#findById(int)
	 */
	@Override
	public ServiceModel findById(int id) {
		ServiceModel model = null;
		try {
			model = entityManager.find(ServiceModel.class, id);
			logger.info(getClass().getSimpleName() + " findById(" + id + ") executed successfully");
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + " findById() failed, " + e.getMessage());
		}
		return model;
	}

	
	/** 
	 * {@inheritDoc}
	 * @see com.example.demo.dao.ServiceDAO#findByName(java.lang.String)
	 */
	@Override
	public ServiceModel findByName(String name) {
		List<ServiceModel> model = null;
		try {
			model = entityManager.createQuery("select a from Services a where a.name like :name")
					.setParameter("name", name).getResultList();
			logger.error(getClass().getSimpleName() + " findByName(" + name + ") executed successfully.");
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + " findByName(" + name + ") has failed, " + e.getMessage());
		}
		if (model.size() == 0) {
			return null;
		}
		return model.get(0);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.example.demo.dao.ServiceDAO#exist(java.lang.String)
	 */
	@Override
	public boolean exist(String name) {
		boolean found = false;
		int count = 0;
		try {
			count = ((Long) entityManager.createQuery("select count(*) from Services a where a.name like :name")
					.setParameter("name", name).getResultList().get(0)).intValue();
			if (count != 0) {
				found = true;
			}
			logger.info(getClass().getSimpleName() + " exist(" + name + ") executed successfully, returned " + found
					+ " count " + count);
		} catch (Exception e) {
			logger.info(getClass().getSimpleName() + " exist(" + name + ") Failed, " + e.getMessage());
		}
		return found;
	}

	/**
	 * {@inheritDoc}<br>
	 * Uses the {@link File} class to get the list of files in the log folder, the files are check to be folder or file.
	 * files are checked if they already exist in the database if so they are skip, else they are added to the database.
	 * Folders are also skipped at the moment
	 * @see com.example.demo.dao.ServiceDAO#updateServiceList()
	 */
	@Override
	@Transactional
	public List<ServiceModel> updateServiceList() {
		List<ServiceModel> list = null;
		String fileName = "";
		try {
			File folder = new File("log");
			File[] fileList = folder.listFiles();								//get file list
			for (int i = 0; i < fileList.length; i++) {
				fileName = fileList[i].getName().split("\\.")[0];				//remove extension
				if (fileList[i].isFile()) {											//check if not folder
					if (!exist(fileName)) {												//check database for the file
						saveService(new ServiceModel(fileName));							//add to database
						logger.info(getClass().getSimpleName() + " updateServiceList() executed successfully, returned "
								+ fileName);
					} else {															//Exists, skip
						logger.warn(getClass().getSimpleName()
								+ " updateServiceList() executed successfully, file already exits " + fileName);
					}
				} else if (fileList[i].isDirectory()) {								//Folder, skip
					/*
					 * Can make it recursive and let it cascade through the directories
					 */
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

}
