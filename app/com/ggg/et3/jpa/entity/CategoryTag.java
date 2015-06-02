package com.ggg.et3.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity 
@Table(name="category_tag")
public class CategoryTag {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;	
	
	@ManyToOne
	private Category category;	
	
	private String tag;

	public CategoryTag() {
		super();
	}

	public CategoryTag(Category category, String tag) {
		super();
		this.category = category;
		this.tag = tag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "CategoryTag [id=" + id + ", category=" + category + ", tag="
				+ tag + "]";
	}



	
}
