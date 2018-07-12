package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Services")
@Table(name = "Services")
public class ServiceModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SId")
	private int id;

	@Column(name = "file_name", unique = true)
	private String name;

	public ServiceModel() {

	}
		
	public ServiceModel(String name) {
		super();
		this.name = name;
	}
	public ServiceModel(int id , String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ServiceModel [id=" + id + ", name=" + name + "]";
	}
	
	

}
