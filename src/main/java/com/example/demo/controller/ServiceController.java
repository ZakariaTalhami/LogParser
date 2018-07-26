package com.example.demo.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ServiceModel;
import com.example.demo.service.ServiceService;

@RestController
@RequestMapping("/service")
public class ServiceController {

	@Autowired
	private ServiceService service;
	
	/**
	 * GET Request that returns all the log files in the database 
	 * @return JSON of all the service models in the database
	 */
	@RequestMapping(value="")
	public List<ServiceModel> findAllServices(){
		return service.findAll();
	}
	
	/**
	 * GET request for a service with a specific ID
	 * 
	 * @param id Id of service in url
	 * @return JSON of the details of a service model
	 */
	@RequestMapping(value="/{id}")
	public ServiceModel FindServiceById(@PathVariable int id) {
		return service.findById(id);
	}
	
	/**
	 * Get request for a service specified by file name
	 * 
	 * @param name Service name in url
	 * @return JSON of Service model
	 */
	@RequestMapping(value="/name/{name}")
	public ServiceModel FindServiceByName(@PathVariable String name) {
		return service.findByName(name);
	}
	
	/**
	 * Get Request to Check if a service model is within the database
	 * 
	 * @param name Service name in url
	 * @return json of boolean
	 */
	@RequestMapping(value="/name/{name}/existance")
	public boolean CheckExistance(@PathVariable String name) {
		return service.exist(name);
	}
	
	/**
	 * POST request for adding services into the database 
	 * 
	 * @param serviceModel Service model to be saved, in the body of the request
	 */
	@RequestMapping(method = RequestMethod.POST,value="")
	public void addService(@RequestBody ServiceModel serviceModel) {
		service.saveService(serviceModel);
	}
	
	/**
	 * PUT request for updating the service list for new log files in the log directory.
	 */
	@RequestMapping(method=RequestMethod.PUT , value="")
	public void updateServiceList() {
		service.updateServiceList();
	}
	
}
