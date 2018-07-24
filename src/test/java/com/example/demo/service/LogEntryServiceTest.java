package com.example.demo.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.LogEntry;
import com.example.demo.model.LogEntryError;
import com.example.demo.model.Tag;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LogEntryServiceTest {

	@Mock
	private EntityManager entityManager;
	
	@Mock 
	private Query query;
	@Mock 
	private TagService tagger;
	
	@InjectMocks
	private LogService servicer;
	
	@Test
	public void saveTest() {
		LogEntry defaultEntry = new LogEntry("2018-07-09 10:06:20,504","error","","","");
		LogEntry ErrorEntry = new LogEntryError("2018-07-09 10:06:20,504","error","","","","");
		
		servicer.save(defaultEntry);
		servicer.save(ErrorEntry);
		verify(entityManager).persist(defaultEntry);
		verify(entityManager).persist(ErrorEntry);
		verifyNoMoreInteractions(entityManager);
	}
	
	
	@Test 
	public void findAllTest() {
		List<LogEntry> entries = new ArrayList<>();
		entries.add(new LogEntry("2018-07-09 10:06:20,504","error","","",""));
		entries.add(new LogEntry("2018-07-09 10:06:20,504","error","","",""));
		entries.add(new LogEntry("2018-07-09 10:06:20,504","error","","",""));
		entries.add(new LogEntry("2018-07-09 10:06:20,504","error","","",""));
		entries.add(new LogEntryError("2018-07-09 10:06:20,504","error","","","",""));
		entries.add(new LogEntryError("2018-07-09 10:06:20,504","error","","","",""));
		entries.add(new LogEntryError("2018-07-09 10:06:20,504","error","","","",""));
		
		
		when(entityManager.createQuery("select a from LogEntries a ")).thenReturn(query);
		when(query.setMaxResults(10)).thenReturn(query);
		when(query.getResultList()).thenReturn(entries);
		
		assertEquals( entries.size() , servicer.findAll().size());
		assertNotNull(servicer.findAll());
		assertThat(servicer.findAll(),  equalTo(entries));
	}
	
	
	@Test
	public void findAllByThreadTest() {
		List<LogEntry> entries = new ArrayList<>();
		List<LogEntry> res = null;
		String MockThread = "";
		entries.add(new LogEntry("2018-07-09 10:06:20,504","error","","",""));
		entries.add(new LogEntry("2018-07-09 10:06:20,504","error","","",""));
		entries.add(new LogEntry("2018-07-09 10:06:20,504","error","","",""));
		entries.add(new LogEntry("2018-07-09 10:06:20,504","error","","",""));
		entries.add(new LogEntryError("2018-07-09 10:06:20,504","error","","","",""));
		entries.add(new LogEntryError("2018-07-09 10:06:20,504","error","","","",""));
		entries.add(new LogEntryError("2018-07-09 10:06:20,504","error","","","",""));
		
		when(entityManager.createQuery("select a from LogEntries a where a.process like :wantedThread")).thenReturn(query);
		when(query.setParameter("wantedThread", MockThread)).thenReturn(query);
		when(query.getResultList()).thenReturn(entries);
		res = servicer.findAllByThread(MockThread);
		
		//Make sure same Array
		assertThat(res, equalTo(entries));
		//Make Sure that the two arrays are of the same size()
		assertEquals(entries.size(), res.size());
		//Make Sure all the LogEntries returned have a thread of %MockThread%
		assertTrue(res.stream().anyMatch(item -> MockThread.equals(item.getProcess())));
		
		verify(entityManager).createQuery("select a from LogEntries a where a.process like :wantedThread");
	}
	
	@Test
	public void findAllErrorsTest() {
		List<LogEntryError> entries = new ArrayList<>();
		List<LogEntryError> res = null;
		entries.add(new LogEntryError("2018-07-09 10:06:20,504","error","","","",""));
		entries.add(new LogEntryError("2018-07-09 10:06:20,504","error","","","",""));
		entries.add(new LogEntryError("2018-07-09 10:06:20,504","error","","","",""));
		
		when(entityManager.createQuery("select a from LogEntryError a")).thenReturn(query);
		when(query.getResultList()).thenReturn(entries);
		
		res = servicer.findAllErrors();
		
		assertEquals(entries.size(), res.size());
		assertThat(res, equalTo(entries));
		assertThat(res, everyItem(instanceOf(LogEntryError.class)));
		verify(entityManager).createQuery("select a from LogEntryError a");
	}
	
	@Test
	public void findById() {
		int mockId_def = 25;
		int mockId_Err = 25;
		LogEntry defaultEntry = new LogEntry("2018-07-09 10:06:20,504","error","","","");
		LogEntry ErrorEntry = new LogEntryError("2018-07-09 10:06:20,504","error","","","","");
		
		defaultEntry.setId(mockId_def);
		ErrorEntry.setId(mockId_Err);
		
		LogEntry res_er,res_def;
		
		when(entityManager.find(LogEntry.class, mockId_def)).thenReturn(defaultEntry);
		when(entityManager.find(LogEntry.class, mockId_Err)).thenReturn(ErrorEntry);
		res_er = servicer.findById(mockId_Err);
		res_def = servicer.findById(mockId_def);
		
		// default testing
		
		assertNotNull(res_def);
		assertThat(res_def, instanceOf(LogEntry.class));
//		assertThat(res_def, not(instanceOf(LogEntryError.class)));
		assertThat(res_def.getId(), equalTo(mockId_def));
		
		
		//Error testing
		assertNotNull(res_er);
		assertThat(res_er, instanceOf(LogEntryError.class));
		assertThat(res_er.getId() , equalTo(mockId_Err));
		
		
		verify(entityManager , times(2)).find(LogEntry.class, mockId_def);
//		verify(entityManager).find(LogEntry.class, mockId_Err);
		
	}
	
	@Test
	public void getThreadListTest() {
		List<String> mockThreadList = new ArrayList<>();
		List<String> res = null;
		mockThreadList.add("Thread1");
		mockThreadList.add("Thread2");
		mockThreadList.add("Thread3");
		mockThreadList.add("Thread4");
		when(entityManager.createQuery("select a.process from LogEntries a group by a.process")).thenReturn(query);
		when(query.getResultList()).thenReturn(mockThreadList);
		
		res  = servicer.getThreadList();
		
		assertNotNull(res);
		assertThat(res.size(), equalTo(mockThreadList.size()));
		
		verify(entityManager).createQuery("select a.process from LogEntries a group by a.process");
		
	}
		
	@Test
	public void getClassListTest() {
		List<String> mockClassList = new ArrayList<>();
		List<String> res = null;
		mockClassList.add("Class1");
		mockClassList.add("Class2");
		mockClassList.add("Class3");
		mockClassList.add("Class4");
		when(entityManager.createQuery("select a.className from LogEntries a group by a.className")).thenReturn(query);
		when(query.getResultList()).thenReturn(mockClassList);
		
		res  = servicer.getClassList();
		
		assertNotNull(res);
		assertThat(res.size(), equalTo(mockClassList.size()));
		
		verify(entityManager).createQuery("select a.className from LogEntries a group by a.className");
		
	}
	
	@Test
	@Ignore
	public void addTagTest() {
		int mockTagId = 5;
		int mockEntryId = 100;
		Tag tag= new Tag(mockTagId, "tag1");
		LogEntry entry = new LogEntry("2018-07-09 10:06:20,504","error","","","");
		Tag res_tag = null;
		LogEntry res_log = null;
		entry.setId(mockEntryId);
		
		when(servicer.findById(mockEntryId)).thenReturn(entry);
		when(tagger.findTagById(mockTagId)).thenReturn(tag);
		
		res_tag = tagger.findTagById(mockTagId);
		res_log = servicer.findById(mockEntryId);
		
	}

	
}
