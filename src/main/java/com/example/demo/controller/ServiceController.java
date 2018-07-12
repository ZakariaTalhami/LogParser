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
	
	@RequestMapping(value="")
	public List<ServiceModel> findAllServices(){
		return service.findAll();
	}
	
	@RequestMapping(value="/{id}")
	public ServiceModel FindServiceById(@PathVariable int id) {
		return service.findById(id);
	}
	
	@RequestMapping(value="/name/{name}")
	public ServiceModel FindServiceByName(@PathVariable String name) {
		return service.findByName(name);
	}
	
	@RequestMapping(value="/name/{name}/existance")
	public boolean CheckExistance(@PathVariable String name) {
		return service.exist(name);
	}
	
	@RequestMapping(method = RequestMethod.POST,value="")
	public void addService(@RequestBody ServiceModel serviceModel) {
		service.saveService(serviceModel);
	}
	
	@RequestMapping(method=RequestMethod.PUT , value="")
	public void updateServiceList() {
		service.updateServiceList();
	}
	
}
