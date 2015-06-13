package com.ggg.et3.loader;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.ggg.et3.domain.Transaction;
import com.ggg.et3.jpa.entity.Category;
import com.ggg.et3.jpa.entity.DCTrans;
import com.ggg.et3.jpa.service.CategoryService;
import com.ggg.et3.jpa.service.DCTransService;
import com.ggg.et3.utils.DateUtils;

import play.Logger;

public class DataLoader {
	
	private final static int DEFAULT_CATEGORY_ID = 1;
	
	
	public DataLoader() {
	}
	
	public int process(List<Transaction> transList) {
		
		DateUtils.setStart();
		
		long now = new Date().getTime();

		CategoryService cs = CategoryService.getInstance();
		Category defaultCat = cs.find(DEFAULT_CATEGORY_ID);
		
		DCTransService ds = DCTransService.getInstance();
		
		int ctr = 0;
		
		try {
			

			
			for (Transaction tran: transList) {
				
				DCTrans dcTrans = new DCTrans(tran.getDescription(),
						tran.getAmount(),
						tran.getSource(),
						new java.sql.Date(tran.getTransactionDate().getTime()),
						new java.sql.Timestamp(now),
						defaultCat,
						tran.getBatchId());

				ds.create(dcTrans);
				
				ctr++;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		System.out.println("Data load time: " + DateUtils.getDuration() + " ms");
		Logger.info("Data load time: " + DateUtils.getDuration() + " ms");
		return ctr;
	}
	
	

	public static void main(String[] args) {
		
		System.out.println("ExpenseRDataLoader started...");
		
		File file = new File("C:\\Users\\gg2712\\workspaces-luna-gg\\Expense-R\\ExpenseRLoader\\data\\wf_feb_to_apr_2015.csv.processed");
		DataParser parser = new DataParser(file, "citi");
		List<Transaction> list = parser.getTransactionList();

		DataLoader loader = new DataLoader();
		loader.process(list);
		
		System.out.println("Done!");
		
	}
}
