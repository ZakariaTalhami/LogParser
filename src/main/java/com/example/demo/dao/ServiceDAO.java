package com.example.demo.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.ServiceModel;

public interface ServiceDAO {

	
	/**	
	 * Save Service model to the database.
	 * @param service Service model to be saved to the database
	 */
	public void saveService(ServiceModel service);
	/**
	 * Retrieve all the services in the database.
	 * @return	List of all the Services in the database 
	 */
	public List<ServiceModel> findAll();
	/**
	 * Retrieve from the database a Service model withe the specified ID
	 * 
	 * @param id ID of the wanted Service model
 	 * @return Service model of specified ID
	 */
	public ServiceModel findById(int id);
	/**
	 * Retrieve service model from the database with the specified filename. 
	 * @param name Name of the service File 
	 * @return	Service model of the specified name 
	 */
	public ServiceModel findByName(String name);
	
	/**
	 * Checks if the specified file Name exist in the database.
	 * 
	 * @param name	Filename of the service file
	 * @return	Boolean of Services existence
	 */
	public boolean exist(String name);
	/**
	 * Reads through the log folder and updates the log services models in the database.
	 * skipping over the files that already in the database
	 * 
	 * @return	List of all the added Service models
	 */
	public List<ServiceModel> updateServiceList();
}
