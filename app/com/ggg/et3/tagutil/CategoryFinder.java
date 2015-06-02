package com.ggg.et3.tagutil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
	
	public void display() {
		System.out.println("** Tags ****");
		for(Tag tag : tagList) {
			System.out.println(tag);
		}
		System.out.println("************");
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
	
	/* this class is not thread safe - need to re-design */
	
	public void updateTransactionCategory(YearMonth yearMonth) {

		DCTransService ds = DCTransService.getInstance();
		//List<DCTrans> list = ds.findDebits(yearMonth);
		List<DCTrans> list = ds.findAllUncategorizedDebits();
		//System.out.println("Size of list: " + list.size());
		for(DCTrans t : list) {
			int catId = findCategory(t.getDescription());
			//System.out.println("Tran desc: " + t.getDescription() + " CatId search: " + catId);
			if (catId > 0 ) {
				ds.updateCategory(t.getTranId(), catId);
			}
		}
	}
	
	public static void main(String[] args) {
		//CategoryFinder service = new CategoryFinder();
		
		//Tag tag1 = new Tag(3, "Safeway");
		//Tag tag2 = new Tag(3, "Ralphs");
		//Tag tag3 = new Tag(3, "Sprouts");
		//Tag tag4 = new Tag(3, "Whole Foods"); 
		
		//service.display();
		
		//service.add(tag1);
		//service.add(tag2);
		//service.add(tag3);
		//service.add(tag4);
		//service.display();
		
		//System.out.println("Find category " + service.findCategory("kldjadsafewaylml;fk));"));
		//System.out.println("Find category " + service.findCategory("kldjadsfewaylml;fk));"));
		//System.out.println("Find category " + service.findCategory("kldjadRalphs dsa));"));
		
		
		//close() will save the data to a file
		//service.close();
		
	}
}
