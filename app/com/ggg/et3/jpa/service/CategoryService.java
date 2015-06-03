package com.ggg.et3.jpa.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.ggg.et3.jpa.entity.Category;
import com.ggg.et3.jpa.entity.DCTrans;

public class CategoryService {
	
	private static CategoryService instance;
	
	private EntityManagerFactory emFac;
	
	public static CategoryService getInstance() {
		//if (instance == null) {
		//	synchronized(CategoryService.class) {
		//		if (instance == null) {
					instance = new CategoryService();
		//		}
		//	}
		//}
		return instance;
	}
	
	private CategoryService() {
		emFac = Persistence.createEntityManagerFactory("JpaProject");
	}
	
	public void create(Category c) {

		EntityManager em = emFac.createEntityManager();
		em.getTransaction().begin();
		
		em.persist(c);
		
		em.getTransaction().commit();
		em.close();
	}
	
	public Category find(int id) {
		
		EntityManager em = emFac.createEntityManager();
		
	    Category category = em.find( Category.class, id );
	    
	    em.close();
	
		return category;
	}
	
	public void delete(int id) {
		
		EntityManager em = emFac.createEntityManager();
		
	    Category category = em.find( Category.class, id );
	    
	    em.getTransaction().begin();
	    em.remove(category);
	    em.getTransaction().commit();
	    
	    em.close();

	}
	
	public Category update(int id, String name) {
		
		EntityManager em = emFac.createEntityManager();
		
		em.getTransaction().begin();
		
		Category cat = em.find(Category.class, id);
		
		if (cat != null) {
			System.out.println("Before update: " + cat);
			cat.setName(name);
		}
		
		em.getTransaction().commit();
		em.close();
		
		return cat;
	}
	
	public List<Category> find() {
		
		EntityManager em = emFac.createEntityManager();

	    Query q = em.createQuery("select c from Category c order by c.name");
		
		@SuppressWarnings("unchecked")
		List<Category> results = q.getResultList();
		
	    em.close();
	
		return results;
	}

	public static void main(String[] args) {
		CategoryService cs = CategoryService.getInstance();
		
		Category cat;
		
		cat = new Category("Grocery/Supplies");
		cs.create(cat);
		
		cat = new Category("Restaurant");
		cs.create(cat);
		
		cat = new Category("Utilities/Services");
		cs.create(cat);
		
		cat = new Category("Cash Allowance");
		cs.create(cat);
		
		cat = new Category("Gas");
		cs.create(cat);
		
		cat = new Category("Insurance/HOA");
		cs.create(cat);

		cat = new Category("Loan Payments");
		cs.create(cat);
		
		cat = new Category("*Credit Card Payments");
		cs.create(cat);

		cat = new Category("Miscellaneous");
		cs.create(cat);

		cat = new Category("Shopping");
		cs.create(cat);
		
		cat = new Category("Entertainment");
		cs.create(cat);

		cat = new Category("Gifts/Special Occasions");
		cs.create(cat);

		cat = new Category("Vacation");
		cs.create(cat);
		
		cat = new Category("Auto Maintenance");
		cs.create(cat);
		
		cat = new Category("Home Maintenence");
		cs.create(cat);
		
		cat = new Category("Appliance Purchase");
		cs.create(cat);
		
		cat = new Category("Home Insurance/Tax");
		cs.create(cat);

	}
	
}
