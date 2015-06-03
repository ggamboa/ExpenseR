package com.ggg.et3.domain;

import java.util.List;

import com.ggg.et3.jpa.entity.Category;
import com.ggg.et3.jpa.service.CategoryService;

/**
 * This class is used to hold a list of Categories defined in the table category
 * This is used in the display categories view 
 * @author gg2712
 *
 */
public class CategoryList {
	
	private static CategoryList instance;
	
	private List<Category> list;
	
	private CategoryList() {
		CategoryService cs = CategoryService.getInstance();
		list = cs.find();
	}
	
	public static CategoryList getInstance() {
		/**
		if (instance == null) {
			synchronized(CategoryList.class) {
				if(instance == null) {
					instance = new CategoryList();
				}
			}
		}*/
		instance = new CategoryList();
		return instance;
	}

	public List<Category> getList() {
		return list;
	}
}
