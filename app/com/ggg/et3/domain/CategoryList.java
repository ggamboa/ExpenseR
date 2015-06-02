package com.ggg.et3.domain;

import java.util.List;

import com.ggg.et3.jpa.entity.Category;
import com.ggg.et3.jpa.service.CategoryService;

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
