package com.example.demo.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.ServiceModel;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceServiceTest {
	
	
	@Mock
	private EntityManager entityManager;
	@Mock
	private Query query;
	
	@InjectMocks
	private ServiceService servicer;

	
	@Test 
	public void findByIdTest_EXISTS(){
		int ExistingID = 25;
		ServiceModel serviceOBJ = new ServiceModel(ExistingID, "Me File,ARR");
		when(entityManager.find(ServiceModel.class, ExistingID)).thenReturn(serviceOBJ);
		//Testing when Finding Entry That exists
		assertThat(servicer.findById(ExistingID), instanceOf(ServiceModel.class));
		assertThat(servicer.findById(ExistingID), notNullValue());
		assertThat(servicer.findById(ExistingID), equalTo(serviceOBJ));
		assertEquals(ExistingID, servicer.findById(ExistingID).getId());
	}
	
	@Test 
	public void findByIdTest_NOT_EXISTS(){
		int nonExistingID = -1;
		when(entityManager.find(ServiceModel.class, nonExistingID)).thenReturn(null);
		//Testing when Finding Entry That Doesnt exists
		assertThat(servicer.findById(nonExistingID), not(instanceOf(ServiceModel.class)));
		assertThat(servicer.findById(nonExistingID), nullValue());
	}
	
	@Test
	public void saveService(){
		//How am i supposed to do this as it has a void return
	}
	
	@Test
	public void findAllServices() {
		List<ServiceModel> list = new ArrayList<>();
		list.add(new ServiceModel(1, ""));
		list.add(new ServiceModel(1, ""));
		list.add(new ServiceModel(1, ""));
		list.add(new ServiceModel(1, ""));
		list.add(new ServiceModel(1, ""));
		
		
		when(entityManager.createQuery("select a from Services a ")).thenReturn(query);
		when(query.getResultList()).thenReturn(list);
		
		assertThat(servicer.findAll(), equalTo(list));
		assertEquals(list.size(), servicer.findAll().size());

	}
	
	@Test
	public void findByName_EXISTS() {
		String Mockname = "WillowAndfoldy";
		List<ServiceModel> res = new ArrayList<>();
		ServiceModel serObj = new ServiceModel(25 , Mockname);
		res.add(serObj);
		when(entityManager.createQuery("select a from Services a where a.name like :name")
				).thenReturn(query);
		when(query.setParameter("name", Mockname)).thenReturn(query);
		when(query.getResultList()).thenReturn(res);
		assertThat(servicer.findByName(Mockname), equalTo(serObj));
	}
	
	@Test
	public void findByName_NON_EXISTS() {
		String Mockname = "WillowAndfoldy";
		List<ServiceModel> res = new ArrayList<>();
		when(entityManager.createQuery("select a from Services a where a.name like :name")
				).thenReturn(query);
		when(query.setParameter("name", Mockname)).thenReturn(query);
		when(query.getResultList()).thenReturn(res);
		assertThat(servicer.findByName(Mockname), equalTo(null));
	}
	
	@Test
	public void existTest() {
		String Mockname = "HouseOfShoes";
		List<Long> list = new ArrayList<>();
		list.add(new Long(20));
		when(entityManager.createQuery("select count(*) from Services a where a.name like :name")).thenReturn(query);
		when(query.setParameter("name", Mockname)).thenReturn(query);
		when(query.getResultList()).thenReturn(list);
//		long count = ((Long) entityManager.createQuery("select count(*) from Services a where a.name like :name")
//				.setParameter("name", Mockname).getResultList().get(0)).intValue();
//		System.out.println(count);
		assertTrue(servicer.exist(Mockname));
	}
	
	@Test
	public void existTest_false() {
		String Mockname = "HouseOfShoes";
		when(entityManager.createQuery("select count(*) from Services a where a.name like :name")).thenReturn(query);
		when(query.setParameter("name", Mockname)).thenReturn(query);
		when(query.getResultList()).thenReturn(null);
		assertFalse(servicer.exist(Mockname));
	}

}






















