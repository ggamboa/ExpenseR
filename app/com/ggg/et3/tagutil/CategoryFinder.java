package com.ggg.et3.tagutil;

import java.util.ArrayList;
import java.util.List;

import com.ggg.et3.domain.YearMonth;
import com.ggg.et3.jpa.entity.Category;
import com.ggg.et3.jpa.entity.CategoryTag;
import com.ggg.et3.jpa.entity.DCTrans;
import com.ggg.et3.jpa.service.CategoryService;
import com.ggg.et3.jpa.service.CategoryTagService;
import com.ggg.et3.jpa.service.DCTransService;

public class CategoryFinder {
	private static CategoryFinder instance;
	
	private List<Tag> tagList;
	
	private CategoryFinder() {
		tagList = new ArrayList<Tag>();
		loadData();
	}

	public static CategoryFinder getInstance() {
		if(instance == null) {
			synchronized(CategoryFinderService.class) {
				if(instance == null) {
					instance = new CategoryFinder(); 
				}
			}
		}
		return instance;
	}
	
	private void loadData() {
		
		List<CategoryTag> list = CategoryTagService.getInstance().find();
		for(CategoryTag ct : list) {
			Tag tag = new Tag(ct.getCategory().getId(), ct.getTag());
			tagList.add(tag);
		}
	}
	
	
	public void add(Tag tag) {
		tagList.add(tag);
		Category cat = CategoryService.getInstance().find(tag.getCategoryId());
		CategoryTagService.getInstance().create(new CategoryTag(cat, tag.getTag()));
	}
	
	public void addTag(int categoryId, String tagName) {
		add(new Tag(categoryId, tagName));
	}
	

	public int findCategory(String description) {
		
		int id = -1;
		
		for (Tag tag : tagList) {
			if (description.toLowerCase().contains(tag.getTag().toLowerCase())) {
				id = tag.getCategoryId();
				break;
			}
		}
	
		return id;
	}
	
	
	public void updateTransactionCategory() {

		DCTransService ds = DCTransService.getInstance();
		List<DCTrans> list = ds.findAllUncategorizedDebits();
		for(DCTrans t : list) {
			int catId = findCategory(t.getDescription());
			if (catId > 0 ) {
				ds.updateCategory(t.getTranId(), catId);
			}
		}
	}
	
}
