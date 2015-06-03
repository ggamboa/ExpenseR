package com.ggg.et3.jpa.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.ggg.et3.jpa.entity.Category;
import com.ggg.et3.jpa.entity.CategoryTag;

public class CategoryTagService {
	
	private static CategoryTagService instance;
	
	private EntityManagerFactory emFac;
	
	public static CategoryTagService getInstance() {
		//if (instance == null) {
		//	synchronized(CategoryTagService.class) {
		//		if (instance == null) {
					instance = new CategoryTagService();
		//		}
		//	}
		//}
		return instance;
	}
	
	private CategoryTagService() {
		emFac = Persistence.createEntityManagerFactory("JpaProject");
	}
	
	public void create(CategoryTag ct) {

		EntityManager em = emFac.createEntityManager();
		em.getTransaction().begin();
		
		em.persist(ct);
		
		em.getTransaction().commit();
		em.close();
	}
	
	public List<CategoryTag> find() {
		
		EntityManager em = emFac.createEntityManager();

	    Query q = em.createQuery("select ct from CategoryTag ct");
		
		@SuppressWarnings("unchecked")
		List<CategoryTag> results = q.getResultList();
		
	    em.close();
	
		return results;
	}
	
	public List<CategoryTag> findByCategory(Category cat) {
		
		EntityManager em = emFac.createEntityManager();
		
	    Query q = em.createQuery("select ct from CategoryTag ct where ct.category.id = " + cat.getId() + " order by ct.tag");
		
		@SuppressWarnings("unchecked")
		List<CategoryTag> results = q.getResultList();
		
	    em.close();
	
		return results;
	}
	
	public CategoryTag find(int id) {
		
		EntityManager em = emFac.createEntityManager();
		
	    CategoryTag categoryTag = em.find( CategoryTag.class, id );
	    
	    em.close();
	
		return categoryTag;
	}
	
	public void delete(int id) {
		
		EntityManager em = emFac.createEntityManager();
		
	    CategoryTag categoryTag = em.find(CategoryTag.class, id );
	    
	    em.getTransaction().begin();
	    em.remove(categoryTag);
	    em.getTransaction().commit();
	    
	    em.close();

	}
	
	
	public static void main(String[] args) {
		CategoryTagService cts = CategoryTagService.getInstance();
		List<CategoryTag> list = cts.find();
		for( CategoryTag tag : list) {
			System.out.println(tag);
		}

	}
}
