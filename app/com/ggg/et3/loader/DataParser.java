package com.ggg.et3.loader;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import play.Logger;

import com.ggg.et3.domain.Transaction;
import com.ggg.et3.utils.DateUtils;

public class DataParser {

	private DataConfig config = null;
	private List<Transaction> transactionList;
	
	public DataParser(File file, String source) {

		try {
			config = new DataConfig(file);
			transactionList = new ArrayList<Transaction>();
			process(file, source);
		}
		catch(Exception e) {
			System.err.println("Unable to create configuration object!");
			System.exit(100);
		}
	}
	

	private void process(File file, String source) {
		SimpleDateFormat dateFormat = config.getDateFormat();
		readData(file, dateFormat, source, getBatchId(file.getName()));
	}

	private String getBatchId(String fileName) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmsSSS");
		String batchId = format.format(new Date());
		return fileName.replace('.', '~') + "-" + batchId;
	}
	
	private void readData(File file, SimpleDateFormat dateFormat, String source, String batchId) {
		
		DateUtils.setStart();
		String filename = file.getName();
		Logger.info("Processing: " + filename + " - Batch Id: " + batchId);

		Date date;
		double amount;
		String description;
		
		String sDate, sAmount, sDescription;
		
		try {
			
			CSVParser parser = CSVParser.parse(file, StandardCharsets.UTF_8, CSVFormat.RFC4180);
			
			Iterator<CSVRecord> recordIter = parser.iterator();

			int ctr = 0;
			int debitCtr = 0;
			int creditCtr = 0;
			
			// determine rightmost field
			int maxPos = 0;
			maxPos = config.getAmountPosition();
			if(config.getDatePosition() > maxPos) maxPos = config.getDatePosition();
			if(config.getDescriptionPosition() > maxPos) maxPos = config.getDescriptionPosition();
			
			System.out.println("Maxpos=" + maxPos);
			
			while(recordIter.hasNext()) {
				
				CSVRecord rec = recordIter.next();
			
				if (rec.size() > maxPos) { // make sure there are enough fields in the record to prevent ArrayIndexOutOfBoundsException
					
					
					sDate = rec.get(config.getDatePosition());
					sAmount = rec.get(config.getAmountPosition());
					sDescription = rec.get(config.getDescriptionPosition());
	
					try {
						date = dateFormat.parse(sDate);
					} 
					catch (ParseException e) {
						date = new Date();
						e.printStackTrace();
					}
	
					try {
						amount = Double.parseDouble(sAmount.replace("$", ""));
						if (!config.isPositiveDebit()) {
							amount = amount * -1;
						}
						
					}
					catch(NumberFormatException e) {
						amount = 0;
					}
					
					description = sDescription;
					
					if (amount > 0) {
						Transaction t = new Transaction(description, amount, source, date, batchId);
						transactionList.add(t);
						debitCtr++;
					}
					else {
						creditCtr++;
					}
					
					ctr++;
				}
			}
			
			Logger.info("Credits bypassed: " + creditCtr);
			Logger.info("Debits processed: " + debitCtr);
			Logger.info("Total records read: " + ctr);
			Logger.info("Process duration: " + DateUtils.getDuration() + " ms");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public List<Transaction> getTransactionList() {
		return transactionList;
	}

	public static void main(String[] args) {
		
		//File file = new File("C:\\Users\\gg2712\\workspaces-luna-gg\\Expense-R\\ExpenseRLoader\\data\\wf_feb_to_apr_2015.csv.processed");
		File file = new File("C:\\gilbert\\data\\wf-jun-11-2015.csv");
		DataParser parser = new DataParser(file, "citi");
		List<Transaction> list = parser.getTransactionList();
		for(Transaction t : list) {
			System.out.println(t);
		}
	}	
}
