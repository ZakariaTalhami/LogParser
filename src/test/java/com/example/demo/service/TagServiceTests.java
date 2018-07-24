package com.example.demo.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Tag;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TagServiceTests {

	@Mock
	private EntityManager entityManager ;
	@Mock
	private Query query;
	
	@InjectMocks
	private TagService servicer;
	
	@Test
	public void findAllTagsTest() {
		List<Tag> list = new ArrayList<>();
		list.add(new Tag(1 , "Tag"));
		list.add(new Tag(1 , "Tag"));
		list.add(new Tag(1 , "Tag"));
		list.add(new Tag(1 , "Tag"));
		list.add(new Tag(1 , "Tag"));
		list.add(new Tag(1 , "Tag"));
		when(entityManager.createQuery("select a from Tag a")).thenReturn(query);
		when(query.getResultList()).thenReturn(list);
		assertNotNull(servicer.findAllTags());
		assertEquals(list.size(), servicer.findAllTags().size());
		assertThat(servicer.findAllTags(), equalTo(list));
		
	}
	
	@Test
	public void fidnTagByIdTest() {
		int exists = 25 ;
		int doestExists = 21;
		Tag tagObj = new Tag(25 , "Tag");
		when(entityManager.find(Tag.class, exists)).thenReturn(tagObj);
		when(entityManager.find(Tag.class, doestExists)).thenReturn(null);
		
		// existence 
		
		assertThat(servicer.findTagById(exists), equalTo(tagObj));
		assertEquals(exists, servicer.findTagById(exists).getId());
		
		// non existence 
		
		assertThat(servicer.findTagById(doestExists), not(instanceOf(Tag.class)));
		assertNull(servicer.findTagById(doestExists));
	}
	
	@Test
	public void saveTagTest() {
		Tag tag = new Tag(12,"MyNewTag");
		servicer.saveTag(tag);
		verify(entityManager).persist(tag);
		verifyNoMoreInteractions(entityManager);
	}
	
	@Test
	public void deleteTagTest() {
		Tag tag = new Tag(25 , "HelpMeIamTagging");
		when(entityManager.find(Tag.class, tag.getId())).thenReturn(tag);
		when(entityManager.find(Tag.class, -1)).thenReturn(null);
		
		servicer.deleteTag(tag.getId());
		servicer.deleteTag(-1);
		verify(entityManager).find(Tag.class, tag.getId());
		verify(entityManager).remove(tag);
	}


}
