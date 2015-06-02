package com.ggg.et3.domain;

import java.util.ArrayList;
import java.util.List;

public class CategoryTags {
	private String category;
	private List<Tag> tagList;
	
	public CategoryTags(String categoryName) {
		super();
		category = categoryName;
		tagList = new ArrayList<Tag>();	
	}
	
	public String getCategory() {
		return category;
	}
	
	public List<Tag> getTags() {
		return tagList;
	}
	
	public void add(Tag tag) {
		tagList.add(tag);
	}
	
	
}
