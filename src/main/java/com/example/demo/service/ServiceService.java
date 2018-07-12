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

	@Override
	@Transactional
	public void saveService(ServiceModel service) {
		// TODO Auto-generated method stub
		try {
			entityManager.persist(service);
			logger.info(getClass().getSimpleName() + " saveService() executed successfully");
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + " saveService() has failed, " + e.getMessage());
		}

	}

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
		return model.get(0);
	}

	public boolean exist(String name) {
		boolean found = false;
		int count = 0;
		try {
			count = ((Long) entityManager.createQuery("select count(*) from Services a ").getResultList().get(0))
					.intValue();
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

	@Transactional
	public List<ServiceModel> updateServiceList() {
		List<ServiceModel> list = null;
		try {
			File folder = new File("log");
			File[] fileList = folder.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isFile()) {
					saveService(new ServiceModel(fileList[i].getName()));
					logger.info(getClass().getSimpleName() + " updateServiceList() executed successfully, returned "
							+ fileList[i].getName());
				} else if (fileList[i].isDirectory()) {
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
