package com.ggg.et3.loader;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.ggg.et3.loader.FieldInfo.FieldType;

public class DataConfig {
	
	private static String[] dateFormats = { "MM/dd/yyyy" };
	private List<FieldInfo> list;
	
	private int amountPosition=-1;
	private int descriptionPosition=-1;
	private int datePosition=-1;
	private SimpleDateFormat dateFormat;
	private boolean positiveDebit=false;
	
	public DataConfig(File file) throws Exception {
		
		list = new ArrayList<FieldInfo>(10);
		
		CSVParser parser = CSVParser.parse(file, StandardCharsets.UTF_8, CSVFormat.RFC4180);
		
		Iterator<CSVRecord> recordIter = parser.iterator();

		int recCtr = 0;
		boolean dateFound = false;
		
		while(recordIter.hasNext()) {
			
			CSVRecord rec = recordIter.next();
			recCtr++;
			
			// if 1st record - create FieldInfo per column - also try to find the date column
			if (recCtr == 1) {
				
				Iterator<String> fieldIter = rec.iterator();
				
				int fldIdx = 0;
				
				while(fieldIter.hasNext()) {
					
					FieldInfo fieldInfo = new FieldInfo(fldIdx);

					String token = fieldIter.next();
					fieldInfo.setFieldType(FieldType.UNASSIGNED);
					
					// try to parse a date
					for (int i=0; i < dateFormats.length; i++) {
						SimpleDateFormat format = new SimpleDateFormat(dateFormats[i]);
						try {
							format.parse(token);
							
							fieldInfo.setDateFormat(dateFormats[i]);
							fieldInfo.setFieldType(FieldType.DATE);
							dateFound = true;
							System.out.println("Found in pos: " + fldIdx);
							break;
						} 
						catch (ParseException e) {
							System.out.println("Unable to parse date from: " + token);
							break;
						}
					}
					
					fldIdx++;
					list.add(fieldInfo);
				}
			}
			
			// if 2nd record and date column has not been found - try to find date again
			if (recCtr == 2 && !dateFound) {
				
				Iterator<String> fieldIter = rec.iterator();
				
				int fldIdx = 0;
				
				while(fieldIter.hasNext()) {

					String token = fieldIter.next();
					
					// try to parse a date
					for (int i=0; i < dateFormats.length; i++) {
						SimpleDateFormat format = new SimpleDateFormat(dateFormats[i]);
						try {
							format.parse(token);
							
							FieldInfo fieldInfo = list.get(fldIdx);
													
							fieldInfo.setDateFormat(dateFormats[i]);
							fieldInfo.setFieldType(FieldType.DATE);
							
							list.set(fldIdx,  fieldInfo);
							
							dateFound = true;
							System.out.println("2nd record - found in pos: " + fldIdx);
							break;
						} 
						catch (ParseException e) {
							System.out.println("2nd record - Unable to parse date from: " + token);
							break;
						}
					}
					
					fldIdx++;
				}
			}
			
			
			if(dateFound) {
				
				Iterator<String> fieldIter = rec.iterator();
				int fldIdx = 0;
				
				while(fieldIter.hasNext()) {
					
					// remove , and $
					String token = fieldIter.next().replaceAll(",", "").replaceAll("$", "");

					// set token to zero if empty
					if (token.trim().isEmpty()) {
						token = "0";
					}

					// try to parse a number
					
					FieldInfo fieldInfo = list.get(fldIdx);
					
					try {
						Double amount = Double.parseDouble(token);
						
						// assume amounts always contain a decimal point
						if (token.indexOf(".") > -1) {
							if (amount > 0) {
								fieldInfo.incrementPositive();
							}
							else if (amount < 0) {
								fieldInfo.incrementNegative();
							}
							fieldInfo.setFieldType(FieldType.AMOUNT);
						}
					}
					catch(NumberFormatException e) {
						//amount = 0;
						if(fieldInfo.getFieldType() == FieldType.UNASSIGNED) {
							fieldInfo.setFieldType(FieldType.DESCRIPTION);
						}
						if(fieldInfo.getFieldType() == FieldType.DESCRIPTION) {
							fieldInfo.addToLength(token.trim().length());
						}
					}
					
					
					fldIdx++;
					//list.add(token);
				}
			}
		}
		
		computePosition();
	}
	
	private void computePosition() {
		
		int currAmtCount=0;
		int currSumLength=0;
		int prevAmtPos=0;
		boolean prevAmtPositiveDebit=false;
		
		for (FieldInfo fldInfo : list) {
			
			if(fldInfo.getFieldType() == FieldType.DATE) {
				datePosition = fldInfo.getPosition();
				dateFormat = new SimpleDateFormat(fldInfo.getDateFormat());
			}
			
			else if(fldInfo.getFieldType() == FieldType.AMOUNT) {
				
				int count = fldInfo.getCtrNegative() + fldInfo.getCtrPositive();
				
				if(count > currAmtCount) {
					currAmtCount = count;
					amountPosition = fldInfo.getPosition();
					
				}
				
				if (fldInfo.getCtrPositive() > fldInfo.getCtrNegative()) {
					prevAmtPositiveDebit = true;
				}
			}
			
			else if(fldInfo.getFieldType() == FieldType.DESCRIPTION) {
				if (fldInfo.getSumLength() > currSumLength) {
					currSumLength = fldInfo.getSumLength();
					descriptionPosition = fldInfo.getPosition(); 
				}
			}
		}
		
		System.out.println("Date Pos: " + datePosition);
		System.out.println("Amoutnt Pos: " + amountPosition);
		System.out.println("Desc Pos: " + descriptionPosition);
	}

	
	
	
	public int getAmountPosition() {
		return amountPosition;
	}

	public int getDescriptionPosition() {
		return descriptionPosition;
	}

	public int getDatePosition() {
		return datePosition;
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public boolean isPositiveDebit() {
		return positiveDebit;
	}

	/**
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(FieldInfo field : list) {
			sb.append(field.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	*/
	


	@Override
	public String toString() {
		return "DataConfig [amountPosition=" + amountPosition
				+ ", descriptionPosition=" + descriptionPosition
				+ ", datePosition=" + datePosition + ", dateFormat="
				+ dateFormat + ", positiveDebit=" + positiveDebit + "]";
	}

	
	
	public static void main(String[] args) {
		File file = new File("C:\\Users\\gg2712\\workspaces-luna-gg\\Expense-R\\ExpenseRLoader\\data\\wf_feb_to_apr_2015.csv.processed");
		//File file = new File("C:\\Users\\gg2712\\workspaces-luna-gg\\Expense-R\\ExpenseRLoader\\data\\visawf_feb_to_may_2015.csv.processed");
		//File file = new File("C:\\Users\\gg2712\\workspaces-luna-gg\\Expense-R\\ExpenseRLoader\\data\\citi_statement_may_2015.CSV.processed");

		try {
			DataConfig fldList = new DataConfig(file);
			System.out.println(fldList);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}

