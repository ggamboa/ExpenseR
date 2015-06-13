package com.ggg.et3.domain;

import java.util.Date;


public class Transaction {
	
	private String description;
	private double amount;
	private String source;
	private java.util.Date transactionDate;
	String batchId;
	
	public Transaction(String description, double amount, String source, Date transactionDate, String batchId) {
		super();
		this.description = description;
		this.amount = amount;
		this.source = source;
		this.transactionDate = transactionDate;
		this.batchId = batchId;
	}

	public String getDescription() {
		return description;
	}

	public double getAmount() {
		return amount;
	}

	public String getSource() {
		return source;
	}

	public java.util.Date getTransactionDate() {
		return transactionDate;
	}

	public String getBatchId() {
		return batchId;
	}

	@Override
	public String toString() {
		return "Transaction [description=" + description + ", amount=" + amount
				+ ", source=" + source + ", transactionDate=" + transactionDate
				+ ", batchId=" + batchId + "]";
	}


	
	
}
