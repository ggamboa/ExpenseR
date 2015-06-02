package com.ggg.et3.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
	
	private static long referenceTime;
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	public static void setStart() {
		referenceTime = System.currentTimeMillis();
	}
	
	public static long getDuration() throws Error {
		if(referenceTime == 0) throw new Error("setStart not previously called!"); 
		return System.currentTimeMillis() - referenceTime;
	}
	
	public static String getCurrentDateTime() {
		return format.format(new Date());
	}
	
	public static int getMonth(java.sql.Date date) {
		Date aDate = new Date(date.getTime());
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(aDate);
		// Need to add 1 since Calendar Month constants are zero-based, i.e. January is 0, December is 11 
		return cal.get(GregorianCalendar.MONTH) + 1;
	}
	
	public static int getYear(java.sql.Date date) {
		Date aDate = new Date(date.getTime());
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(aDate);
		return cal.get(GregorianCalendar.YEAR);
	}
	

}
