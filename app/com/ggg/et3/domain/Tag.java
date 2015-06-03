package com.ggg.et3.domain;

public class Tag {
	
	private int id;
	private String tag;
	private String category;
	
	public Tag(int id, String tag, String category) {
		super();
		this.id = id;
		this.tag = tag;
		this.category = category;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "Tag [id=" + id + ", tag=" + tag + ", category=" + category
				+ "]";
	}
}
