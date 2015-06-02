package com.ggg.et3.tagutil;

public class Tag {

	private int categoryId;
	private String tag;
	
	public Tag(int categoryId, String tag) {
		super();
		this.categoryId = categoryId;
		this.tag = tag;
	}

	
	public int getCategoryId() {
		return categoryId;
	}

	public String getTag() {
		return tag;
	}

	@Override
	public String toString() {
		return "Tag [categoryId=" + categoryId + ", tag=" + tag + "]";
	}
	
	

}
