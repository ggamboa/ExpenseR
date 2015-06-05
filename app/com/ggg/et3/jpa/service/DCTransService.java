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



import play.Logger;

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
		
		Logger.debug("SQL: " + query);
		
		Query q = em.createQuery(query);
		
		@SuppressWarnings("unchecked")
		List<DCTrans> results = q.getResultList();
		
		em.close();
		
		return results;
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
	
	public DCTrans update(int id, Category cat, String desc) {
		
		EntityManager em = emFac.createEntityManager();
		
		em.getTransaction().begin();
		
		DCTrans tran = em.find(DCTrans.class, id);
		
		if (tran != null) {
			tran.setCategory(cat);
			if(desc != null && desc.trim().length() > 0) {
				tran.setDescription(desc);
			}
			tran.setLastUpdatedate(new Timestamp(new Date().getTime()));
		}
		
		em.getTransaction().commit();
		em.close();
		
		return tran;
	}
	
	public DCTrans update(int id, int categoryId, String desc) {
		
		EntityManager em = emFac.createEntityManager();
		
		CategoryService cs = CategoryService.getInstance();
		Category newcat = cs.find(categoryId);
		
		em.close();
		
		return update(id, newcat, desc.trim());
	}
	
	public static void main(String[] args) {
		
		DateUtils.setStart();
		DCTransService ts = new DCTransService();
		List<DCTrans> list = ts.findDebits(2014, 07);
		for(DCTrans t : list) {
			System.out.println(t);
		}
		
		System.out.println("Execution time: " +  DateUtils.getDuration());
	}
}
