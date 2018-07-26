package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Tag;

public interface TagDAO {
	
	
	/**
	 * Saves a Tag model to the database
	 * 
	 * @param tag Tag model to be saved
	 */
	public void saveTag(Tag tag);
	/**
	 * Updates a Tag model in the database, if the model isn't in the database,
	 * it saves the model to the database.
	 * 
	 * @param tag Tag model to update or save in the database.
	 */
	public void updateTag(Tag tag);
	/**
	 * Retrieves all the Tag models in the database.
	 * 
	 * @return List of all the tag models in the database.
	 */
	public List<Tag> findAllTags();
	/**
	 * Retrieve a Tag Model with a specific ID from the database.
	 * 
	 * @param id ID of the Tag to be retrieved.
	 * @return	Returns Tag model with the provided id.
	 */
	public Tag findTagById(int id);
	/**
	 * Delete a tag with a specific id from the database.
	 * 
	 * @param id Id of the tag to be deleted.
	 */
	public void deleteTag(int id);
}
