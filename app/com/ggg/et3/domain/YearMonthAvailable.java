package com.ggg.et3.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.ggg.et3.jpa.entity.DCTrans;
import com.ggg.et3.jpa.service.DCTransService;
import com.ggg.et3.utils.DateUtils;

public class YearMonthAvailable {
	
	private static YearMonthAvailable instance;
	private Set<YearMonth> theSet;
	private YearMonth latestYearMonth;
	
	public static YearMonthAvailable getInstance() {
		if(instance == null) {
			synchronized(YearMonthAvailable.class) {
				if(instance == null) {
					instance = new YearMonthAvailable();
				}
			}
		}
		return instance;
	}
	
	private YearMonthAvailable() {
		theSet = new TreeSet<YearMonth>(); 
		load();
	}
	
	private void add(YearMonth yrmo) {
		theSet.add(yrmo);
	}
	
	private void load() {
		
		DCTransService ts = DCTransService.getInstance();
		List<DCTrans> list = ts.findAllDebits();
		
		for(DCTrans t: list) {
			
			int mo = DateUtils.getMonth(t.getTransactionDate());
			int yr = DateUtils.getYear(t.getTransactionDate());
			YearMonth yrmo = new YearMonth(yr, mo);
			add(yrmo);
		}
	}
	
	/**********************
	public void display() {
		
		Iterator<YearMonth> iter = theSet.iterator();
		while(iter.hasNext()) {
			System.out.println("=>" + iter.next());
		}
	}
	**/
	
	public List<YearMonth> getYearMonthList() {
		
		List<YearMonth> list = new ArrayList<YearMonth>();
		Iterator<YearMonth> iter = theSet.iterator();
		while(iter.hasNext()) {
			latestYearMonth = iter.next();
			list.add(latestYearMonth);
		}
		return list;
	}
	
	public YearMonth getLatest() {
		if(latestYearMonth == null) {
			getYearMonthList();
		}
		return latestYearMonth;
	}
	

}
