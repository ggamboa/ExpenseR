package com.ggg.et3.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ggg.et3.jpa.entity.DCTrans;

/**
 * This class is used to hold the data for the report
 * It will load a list of transactions for the current reporting period
 * The load routine adds each transcation to a CategorySet - a set that contains transactions with similar category
 * The CategorySet's are stored in a Hashmap for retrieval by key which is the category name
 * @author gg2712
 *
 */
public class Report {
	
	private YearMonth yearMonth;
	private Map<String, CategorySet> map;
	private List<CategorySet> listCat; 
	private double totalExpenses=0;
	
	public Report(YearMonth yearMonth) {
		this.yearMonth = yearMonth;
		map = new HashMap<String, CategorySet>(20);
	}
	
	public Report(int year, int month) {
		this.yearMonth = new YearMonth(year, month);
		map = new HashMap<String, CategorySet>(20);
	}
	
	/**
	 * First retrieve CategorySet based on category name from hashmap
	 * If not found, create new category set and add to hashmap
	 * Add transaction to the the CategorySet (new or retrieved from hashmap)
	 * @param t
	 */
	private void add(DCTrans t) {
		CategorySet set = map.get(t.getCategory().getName());
		
		if(set == null) {
			set = new CategorySet(t.getCategory());
			map.put(t.getCategory().getName(), set);
		}
		set.add(t);
		
	}
	
	/**
	 * Load each transaction to a CategorySet
	 * @param list
	 */
	public void load(List<DCTrans> list) {
		
		for(DCTrans t : list) {
			add(t);
		}
		
		
		// Save key set of hash map into a sorted list (report will be sorted by category)
		Iterator<String> iter = map.keySet().iterator();
		
		List<String> categoryList = new ArrayList<String>();
		while (iter.hasNext()) {
			categoryList.add(iter.next());
		}
		
		Collections.sort(categoryList);
		
		
		// Save the CategorySets in a list and compute total amount for the report (Sum of all category sub total)		
		
		listCat = new ArrayList<CategorySet>();
		
		for(String cat : categoryList) {
			CategorySet set = map.get(cat);
			listCat.add(set);
			
			if(!set.getCategory().getName().startsWith("*")) {
				totalExpenses += set.getCategoryTotal();
			}
		}
	}
	
	public List<CategorySet> getAsList() {
		return listCat;
	}
	
	public double getTotalExpenses() {
		return totalExpenses;
	}
}
