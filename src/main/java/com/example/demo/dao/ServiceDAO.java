package com.example.demo.dao;

import java.util.List;
import com.example.demo.model.ServiceModel;

public interface ServiceDAO {

	public void saveService(ServiceModel service);
	public List<ServiceModel> findAll();
	public ServiceModel findById(int id);
	public ServiceModel findByName(String name);
}
