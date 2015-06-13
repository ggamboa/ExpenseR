package com.ggg.et3.loader;

public class FieldInfo {
	
	public enum FieldType {
		DATE,
		DESCRIPTION,
		AMOUNT,
		UNASSIGNED;
	}
	
	private int position=-1;
	private String dateFormat;
	private FieldType fieldType;
	private int accumLength=0;
	private int ctrAll=0;
	private int ctrNonZero=0;
	private int ctrNegative=0;
	private int ctrPositive=0;
	private int sumLength=0;
	
	public FieldInfo(int position) {
		this.position = position;
		fieldType = FieldType.UNASSIGNED;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	
	
	public int getPosition() {
		return position;
	}

	public int getAccumLength() {
		return accumLength;
	}

	public int getCtrNegative() {
		return ctrNegative;
	}

	public int getCtrPositive() {
		return ctrPositive;
	}

	public int getSumLength() {
		return sumLength;
	}

	public void incrementNegative() {
		ctrNegative++;
	}
	
	public void incrementPositive() {
		ctrPositive++;
	}
	
	public void addToLength(int len) {
		sumLength = sumLength+len;
	}

	
	@Override
	public String toString() {
		return "FieldInfo [position=" + position + ", dateFormat=" + dateFormat
				+ ", fieldType=" + fieldType + ", accumLength=" + accumLength
				+ ", ctrAll=" + ctrAll + ", ctrNonZero=" + ctrNonZero
				+ ", ctrNegative=" + ctrNegative + ", ctrPositive="
				+ ctrPositive + ", sumLength=" + sumLength + "]";
	}

	
	

}
