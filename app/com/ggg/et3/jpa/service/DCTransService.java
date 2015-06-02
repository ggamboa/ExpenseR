package com.ggg.et3.jpa.service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;


import com.ggg.et3.domain.CategorySet;
import com.ggg.et3.domain.Month;
import com.ggg.et3.domain.Report;
import com.ggg.et3.domain.YearMonth;
import com.ggg.et3.domain.YearMonthAvailable;
import com.ggg.et3.jpa.entity.Category;
import com.ggg.et3.jpa.entity.DCTrans;
import com.ggg.et3.utils.DateUtils;

public class DCTransService {
	
	private static DCTransService instance;
	
	private EntityManagerFactory emFac;
	
	private DCTransService() {
		emFac = Persistence.createEntityManagerFactory("JpaProject");
	}
	
	public static DCTransService getInstance() {
		//if(instance == null) {
		//	synchronized(DCTransService.class) {
		//		if(instance == null) {
					instance = new DCTransService();
		//		}
		//	}
		//}
		return instance;
	}
	
	public void close() {
		emFac.close();
	}

	public void create(DCTrans t) {
		
		EntityManager em = emFac.createEntityManager();
		em.getTransaction().begin();
		
		em.persist(t);
		
		em.getTransaction().commit();
		em.close();
	}
	
	public List<DCTrans> findAll() {
		
		EntityManager em = emFac.createEntityManager();

		Query q = em.createQuery("SELECT t FROM DCTrans t");
		
		@SuppressWarnings("unchecked")
		List<DCTrans> results = q.getResultList();
		
		em.close();
		
		return results;
	}
	
	public List<DCTrans> findAllDebits() {
		
		EntityManager em = emFac.createEntityManager();

		Query q = em.createQuery("SELECT t FROM DCTrans t where t.amount > 0");
		
		@SuppressWarnings("unchecked")
		List<DCTrans> results = q.getResultList();

		em.close();
		
		return results;
	}

	public List<DCTrans> findAllUncategorizedDebits() {
		
		EntityManager em = emFac.createEntityManager();

		Query q = em.createQuery("SELECT t FROM DCTrans t where t.amount > 0 and t.category.id = 1");
		
		@SuppressWarnings("unchecked")
		List<DCTrans> results = q.getResultList();

		em.close();
		
		return results;
	}
	

	public int countForCategory(int catId) {
		
		EntityManager em = emFac.createEntityManager();

		Query q = em.createQuery("SELECT t FROM DCTrans t where t.amount > 0 and t.category.id = " + catId);
		
		@SuppressWarnings("unchecked")
		List<DCTrans> results = q.getResultList();

		em.close();
		
		return results.size();
	}
	
	
	public List<DCTrans> findDebits(int year, int month) {
		
		EntityManager em = emFac.createEntityManager();

		DecimalFormat monthFormatter = new DecimalFormat("00");
		String sMonth = monthFormatter.format(month);
		
		int nextYear = year;
		int nextMonth = month + 1;
		String sNextMonth = "";
		
		if(nextMonth > 12) {
			nextMonth = 1;
			nextYear++;
		}
		
		sNextMonth = monthFormatter.format(nextMonth);
		
		String query = "SELECT t FROM DCTrans t where t.amount > 0"
						+ " and t.transactionDate >= '" + year + "-" + sMonth + "-01'" //'2014-12-01'"
						+ " and t.transactionDate < '" + nextYear + "-" + sNextMonth + "-01'" //'2015-01-01'"
						+ " order by t.transactionDate";
		
		System.out.println("SQL: " + query);
		
		Query q = em.createQuery(query);
		//Query q = em.createQuery("SELECT t FROM DCTrans t order by t.transactionDate");
		
		@SuppressWarnings("unchecked")
		List<DCTrans> results = q.getResultList();
		
		//List<DCTrans> outputList = new ArrayList<DCTrans>();
		
		/*
		for(DCTrans t: results) {
			
			int mo = DateUtils.getMonth(t.getTransactionDate());
			int yr = DateUtils.getYear(t.getTransactionDate());
			
			// include debits only
			if (yr == year && mo == month && t.getAmount() < 0) {
				outputList.add(t);
			}
		}*/
		
		em.close();
		
		return results;
		//return outputList;
	}
	
	public List<DCTrans> find(int year, int month) {
		
		EntityManager em = emFac.createEntityManager();

		//Query q = em.createQuery("SELECT t FROM DCTrans t where t.transactionDate > '2014-12-29' and t.transactionDate < '2015-01-01' order by t.transactionDate");
		Query q = em.createQuery("SELECT t FROM DCTrans t ");
		
		@SuppressWarnings("unchecked")
		List<DCTrans> results = q.getResultList();
		List<DCTrans> outputList = new ArrayList<DCTrans>();
		
		for(DCTrans t: results) {
			
			System.out.println(t);
			int mo = DateUtils.getMonth(t.getTransactionDate());
			int yr = DateUtils.getYear(t.getTransactionDate());
			
			if (yr == year && mo == month) {
				outputList.add(t);
			}
		}
		
		em.close();
		
		return outputList;
	}
	
	public List<DCTrans> find(int year, Month month) {
		return find(year, month.getMonthCode());
	}
	
	public List<DCTrans> findDebits(YearMonth yearMonth) {
		return findDebits(yearMonth.getYear(), yearMonth.getMonthCode());
	}

	
	public DCTrans findById(int id) {
		
		EntityManager em = emFac.createEntityManager();
		
		DCTrans tran = em.find(DCTrans.class, id);
		
		em.close();
		
		return tran;
	}
	
	public DCTrans updateCategory(int id, Category cat) {
		
		EntityManager em = emFac.createEntityManager();
		
		em.getTransaction().begin();
		
		DCTrans tran = em.find(DCTrans.class, id);
		
		if (tran != null) {
			System.out.println("Before update: " + tran);
			tran.setCategory(cat);
			tran.setLastUpdatedate(new Timestamp(new Date().getTime()));
		}
		
		em.getTransaction().commit();
		em.close();
		
		return tran;
	}
	
	public DCTrans updateCategory(int id, int categoryId) {
		
		EntityManager em = emFac.createEntityManager();
		
		CategoryService cs = CategoryService.getInstance();
		Category newcat = cs.find(categoryId);
		
		em.close();
		
		return updateCategory(id, newcat);
	}
	
	public static void main(String[] args) {
		
		DateUtils.setStart();
		
		/**
		CategoryService cs = new CategoryService();
		Category catGrocery = new Category("Grocery");
		Category catUtilities = new Category("Utilites");
		Category catGas = new Category("Gas");
		
		cs.create(catGrocery);
		cs.create(catUtilities);
		cs.create(catGas);

		TransactionsService dts = new TransactionsService();
		
		java.sql.Date curdateSql = new java.sql.Date(new Date().getTime());
		java.sql.Timestamp curtimestampSql = new java.sql.Timestamp(new Date().getTime());
		
		Transaction t = new Transaction("Safeway", 36.23, "Wells Fargo", curdateSql, curtimestampSql, catGrocery);
		Transaction t2 = new Transaction("Sprouts", 24.76, "Wells Fargo", curdateSql, curtimestampSql, catGrocery);
		Transaction t3 = new Transaction("PG&E", 36.23,  "Wells Fargo", curdateSql, curtimestampSql, catUtilities);
		Transaction t4 = new Transaction("Chevron", 36.23, "Citicards", curdateSql, curtimestampSql, catGas);
		
		dts.create(t);
		dts.create(t2);
		dts.create(t3);
		dts.create(t4);
		
		**/
		DCTransService ts = new DCTransService();
		List<DCTrans> list = ts.findDebits(2014, 07);
		for(DCTrans t : list) {
			System.out.println(t);
		}
		
		//ts.find(2014, Month.JULY);
		
		//CategoryService cs = new CategoryService();
		//Category newcat = cs.find(2);
		
		//DCTrans t = ts.updateCategory(53, newcat);
		//System.out.println(t);
		
		//YearMonthAvailable yma = YearMonthAvailable.getInstance();
		//yma.display();
		
		//System.out.println("Execution time: " +  DateUtils.getDuration());
		
		//DateUtils.setStart();
		
		//YearMonthAvailable yma2 = YearMonthAvailable.getInstance();
		//yma2.display();
		
		//System.out.println("Execution time: " +  DateUtils.getDuration());
		
		//DCTransService ts = new DCTransService();
		//List<DCTrans> list = ts.findAllDebits();
		//for(DCTrans t : list) {
	//		System.out.println(t);
	//	}
		
		//YearMonth yrmo = new YearMonth(2014, Month.DECEMBER);
		
		//Report report = new Report(yrmo);
		//report.load(list);
		//report.display();
		
		//List<CategorySet> myList = report.getAsList();
		//for(CategorySet cs : myList) {
		//	List<DCTrans> tlist = cs.getTranList();
		//	System.out.println(cs.getCategory() + " Total: " + cs.getCategoryTotal());
		//	for(DCTrans t : tlist) {
		//		System.out.println(t);
		//	}
		//}
		
		//System.out.println("Execution time: " +  DateUtils.getDuration());
		
	}

}
