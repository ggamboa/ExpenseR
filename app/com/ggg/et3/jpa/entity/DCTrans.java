package com.ggg.et3.jpa.entity;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="trans")
public class DCTrans {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name="id")
	private int tranId;
	private String description;
	private double amount;
	private String source;
	@Column (name="transaction_date")
	private java.sql.Date transactionDate;
	@Column (name="last_update_date")
	private java.sql.Timestamp lastUpdatedate;
	
	//@ManyToOne(cascade = { javax.persistence.CascadeType.REFRESH, javax.persistence.CascadeType.PERSIST })
	@ManyToOne
	private Category category;
	
	@Column (name="batch_id")
	private String batchId;
	
	public DCTrans() {
		super();
	}
	
	public DCTrans(String description, double amount, String source,
			Date transactionDate, Timestamp lastUpdatedate, Category category) {
		super();
		this.description = description;
		this.amount = amount;
		this.source = source;
		this.transactionDate = transactionDate;
		this.lastUpdatedate = lastUpdatedate;
		this.category = category;
	}

	public DCTrans(String description, double amount, String source,
			Date transactionDate, Timestamp lastUpdatedate, Category category,
			String batchId) {
		super();
		this.description = description;
		this.amount = amount;
		this.source = source;
		this.transactionDate = transactionDate;
		this.lastUpdatedate = lastUpdatedate;
		this.category = category;
		this.batchId = batchId;
	}

	public int getTranId() {
		return tranId;
	}
	public void setTranId(int tranId) {
		this.tranId = tranId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public java.sql.Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(java.sql.Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public java.sql.Timestamp getLastUpdatedate() {
		return lastUpdatedate;
	}
	public void setLastUpdatedate(java.sql.Timestamp lastUpdatedate) {
		this.lastUpdatedate = lastUpdatedate;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	@Override
	public String toString() {
		return "DCTrans [tranId=" + tranId + ", description=" + description
				+ ", amount=" + amount + ", source=" + source
				+ ", transactionDate=" + transactionDate + ", lastUpdatedate="
				+ lastUpdatedate + ", batchId=" + batchId + ", category="
				+ category + "]";
	}


}
