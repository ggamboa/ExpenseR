package com.ggg.et3.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ggg.et3.jpa.entity.DCTrans;

public class Report {
	
	private YearMonth yearMonth;
	private Map<String, CategorySet> map;
	List<CategorySet> listCat; 
	private double totalExpenses=0;
	
	public Report(YearMonth yearMonth) {
		this.yearMonth = yearMonth;
		map = new HashMap<String, CategorySet>(20);
	}
	
	public Report(int year, int month) {
		this.yearMonth = new YearMonth(year, month);
		map = new HashMap<String, CategorySet>(20);
	}
	
	private void add(DCTrans t) {
		CategorySet set = map.get(t.getCategory().getName());
		
		if(set == null) {
			set = new CategorySet(t.getCategory());
			map.put(t.getCategory().getName(), set);
		}
		set.add(t);
		
	}
	
	public void load(List<DCTrans> list) {
		
		for(DCTrans t : list) {
			add(t);
		}
		
		listCat = new ArrayList<CategorySet>();
		Iterator<String> iter = map.keySet().iterator();
		
		while (iter.hasNext()) {
			CategorySet set = map.get(iter.next());
			listCat.add(set);
			
			if(!set.getCategory().getName().startsWith("*")) {
				totalExpenses += set.getCategoryTotal();
			}
			
		}
	}
	
	public void display() {
		
		System.out.println("Report for: " + yearMonth);
		
		Iterator<String> iter = map.keySet().iterator();
		
		while (iter.hasNext()) {
			
			CategorySet set = map.get(iter.next());
			
			List<DCTrans> tList = set.getTranList();
			
			System.out.println("List of trans for: " + set.getCategory());
			for(DCTrans t : tList) {
				System.out.println(t);
			}
			System.out.println("Total for category " + set.getCategory().getName() + " : " + set.getCategoryTotal());
			
		}
	}
	
	public List<CategorySet> getAsList() {
		
		/**List<CategorySet> listCat = new ArrayList<CategorySet>();
		Iterator<String> iter = map.keySet().iterator();
		
		while (iter.hasNext()) {
			CategorySet set = map.get(iter.next());
			listCat.add(set);
		}*/
		return listCat;
	}
	
	public double getTotalExpenses() {
		return totalExpenses;
	}
	

}
