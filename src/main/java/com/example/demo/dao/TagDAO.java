package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Tag;

public interface TagDAO {
	public void saveTag(Tag tag);
	public void updateTag(Tag tag);
	public List<Tag> findAllTags();
	public Tag findTagById(int id);
	public void deleteTag(int id);
}
