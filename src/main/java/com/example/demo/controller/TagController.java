package com.example.demo.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Tag;
import com.example.demo.service.TagService;
import com.example.demo.singleton.LoggerSingleton;

@RestController
@RequestMapping("/tag")
public class TagController {

	@Autowired
	private TagService tagService;

	final static Logger logger = LoggerSingleton.getLoggerOBJ().getLoggerman();

	
	/**
	 * Used by front end to insert new custom Tag
	 * 
	 * @param tag Tag model to be saved in request body
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void saveTag(@RequestBody Tag tag) {
		logger.info(getClass().getSimpleName() + " Entered SaveTag(tag)");
		tagService.saveTag(tag);
	}

	/**
	 * Returns List of all Tags, used by front end to construct the Tag selects for filers and tagging logs.
	 * 
	 * @return List of all tags as JSON
	 */
	@RequestMapping(value = "")
	public List<Tag> findAllTags() {

		logger.info(getClass().getSimpleName() + " Entered findAllTags()");
		return tagService.findAllTags();
	}

	/**
	 * 
	 * @param id Tag ID
	 * @return	JSON of Tag details
	 */
	@RequestMapping(value = "/{id}")
	public Tag findTagById(@PathVariable int id) {
		logger.info(getClass().getSimpleName() + " Entered findTagById(" + id + ")");
		return tagService.findTagById(id);
	}

	/**
	 * @param id Tag ID to be deleted in url path
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteTag(@PathVariable int id) {
		logger.info(getClass().getSimpleName() + " Entered deleteTag(" + id + ")");
		tagService.deleteTag(id);
	}
}
