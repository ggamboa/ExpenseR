package com.ggg.et3.domain;

public class YearMonth implements Comparable<YearMonth> {
	
	private int year;
	private int mo;
	private Month month;
	
	
	public YearMonth(String sYearMonth) throws Exception{
		String[] s = sYearMonth.split("-");
		year = Integer.parseInt(s[0]);
		month = translate(s[1]);
		if (month == null) throw new InstantiationException("Invalid month string");
	}
	
	

	private Month translate(String s) {
		Month month;
		switch(s) {
			case "JANUARY": month = Month.JANUARY;
					break;
			case "FEBRUARY": month = Month.FEBRUARY;
					break;
			case "MARCH": month = Month.MARCH;
					break;
			case "APRIL": month = Month.APRIL;
					break;
			case "MAY": month = Month.MAY;
					break;
			case "JUNE": month = Month.JUNE;
					break;
			case "JULY": month = Month.JULY;
					break;
			case "AUGUST": month = Month.AUGUST;
					break;
			case "SEPTEMBER": month = Month.SEPTEMBER;
					break;
			case "OCTOBER": month = Month.OCTOBER;
					break;
			case "NOVEMBER": month = Month.NOVEMBER;
					break;
			case "DECEMBER": month = Month.DECEMBER;
					break;
			default: month = null;		

		}
		return month;
	}

	
	/**
	public YearMonth(int yr, Month mo) {
		year = yr;
		month = mo;
	}
	*/
	
	public YearMonth(int yr, int mo) {
		
		year = yr;
		this.mo = mo;
		
		switch(mo) {
			case 1: month = Month.JANUARY;
					break;
			case 2: month = Month.FEBRUARY;
					break;
			case 3: month = Month.MARCH;
					break;
			case 4: month = Month.APRIL;
					break;
			case 5: month = Month.MAY;
					break;
			case 6: month = Month.JUNE;
					break;
			case 7: month = Month.JULY;
					break;
			case 8: month = Month.AUGUST;
					break;
			case 9: month = Month.SEPTEMBER;
					break;
			case 10: month = Month.OCTOBER;
					break;
			case 11: month = Month.NOVEMBER;
					break;
			case 12: month = Month.DECEMBER;
					break;			
		}
		
	}

	@Override
	public String toString() {
		return Integer.toString(year) + "-" + month;
	}
	
	public String getMonthYearString() {
		return month + " " + Integer.toString(year);
	}
	
	public int getYear() {
		return year;
	}

	/**
	public Month getMonth() {
		return month;
	}
	*/
	
	public int getMonthCode() {
		return month.getMonthCode();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		YearMonth other = (YearMonth) obj;
		if (month != other.month)
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	@Override
	public int compareTo(YearMonth that) {

		if(this.year < that.year) return -1;
		else {
			if(this.year > that.year) return 1;
			else {
				//year are equal
				if(this.month.getMonthCode() < that.month.getMonthCode()) return -1;
				else {
					if(this.month.getMonthCode() > that.month.getMonthCode()) return 1;
					else return 0;
				}
			}
		}

	}

}
