package com.ggg.et3.domain;

import java.util.ArrayList;
import java.util.List;

import com.ggg.et3.jpa.entity.Category;
import com.ggg.et3.jpa.entity.DCTrans;

/**
 * This class holds a list of transactions for a given category
 * It also contains a field that holds the total amount for the list of transactions
 * @author gg2712
 *
 */
public class CategorySet {
	
	private Category category;
	private List<DCTrans> tranList;
	private double categoryTotal;
	
	public CategorySet(Category cat) {
		
		category = cat;
		tranList = new ArrayList<DCTrans>();
		categoryTotal = 0;
	}
	
	public void add(DCTrans t) {
		tranList.add(t);
		categoryTotal += t.getAmount();
	}

	public Category getCategory() {
		return category;
	}

	public List<DCTrans> getTranList() {
		return tranList;
	}

	public double getCategoryTotal() {
		return categoryTotal;
	}
}
