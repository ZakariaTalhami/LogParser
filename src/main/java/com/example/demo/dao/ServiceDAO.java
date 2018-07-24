package com.example.demo.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.ServiceModel;

public interface ServiceDAO {

	public void saveService(ServiceModel service);
	public List<ServiceModel> findAll();
	public ServiceModel findById(int id);
	public ServiceModel findByName(String name);
	
	public boolean exist(String name);
	public List<ServiceModel> updateServiceList();
}
