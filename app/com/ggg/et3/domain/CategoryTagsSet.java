package com.ggg.et3.domain;

import java.util.ArrayList;
import java.util.List;

import com.ggg.et3.jpa.entity.Category;
import com.ggg.et3.jpa.entity.CategoryTag;
import com.ggg.et3.jpa.service.CategoryService;
import com.ggg.et3.jpa.service.CategoryTagService;

/**
 * This class holds a list of CategoryTags which holds a list of tags per Category
 * Used in display tags view
 * @author gg2712
 *
 */
public class CategoryTagsSet {
	
	private List<CategoryTags> ctList;

	public CategoryTagsSet() {
		super();
		ctList = new ArrayList<CategoryTags>();
		
		CategoryService cs = CategoryService.getInstance();
		List<Category> catList = cs.find();
		
		CategoryTagService cts = CategoryTagService.getInstance();
				
		for (Category cat : catList) {
		
			
			List<CategoryTag> ctagList = cts.findByCategory(cat);
			
			if (ctagList.size() > 0) {
				
				CategoryTags categoryTags = new CategoryTags(cat.getName());
				
				for (CategoryTag ctag : ctagList) {
					categoryTags.add(new Tag(ctag.getId(), ctag.getTag(), ctag.getCategory().getName()));
				}
				
				add(categoryTags);
				
			}
			
		}
	}
	
	private void add(CategoryTags tags) {
		ctList.add(tags);
	}
	
	public List<CategoryTags> getCategoryTagsList() {
		return ctList;
	}
	
	public static void main(String[] args) {
		
		CategoryTagsSet cts = new CategoryTagsSet();
		List<CategoryTags> ctagsList = cts.getCategoryTagsList();
		
		for(CategoryTags ctags : ctagsList) {
			
			List<Tag> tagList = ctags.getTags();
			
			System.out.println("***" + ctags.getCategory());
			for(Tag tag : tagList) {
				System.out.println(tag);
			}
		}
	}
}
