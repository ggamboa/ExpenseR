package controllers;

import java.util.List;
import java.util.Map;

import com.ggg.et3.domain.CategoryList;
import com.ggg.et3.domain.CategorySet;
import com.ggg.et3.domain.CategoryTags;
import com.ggg.et3.domain.CategoryTagsSet;
import com.ggg.et3.domain.Month;
import com.ggg.et3.domain.Report;
import com.ggg.et3.domain.YearMonth;
import com.ggg.et3.domain.YearMonthAvailable;
import com.ggg.et3.global.DCTransServiceThreadLocal;
import com.ggg.et3.jpa.entity.Category;
import com.ggg.et3.jpa.entity.DCTrans;
import com.ggg.et3.jpa.service.CategoryService;
import com.ggg.et3.jpa.service.CategoryTagService;
import com.ggg.et3.jpa.service.DCTransService;
import com.ggg.et3.tagutil.CategoryFinder;
import com.ggg.et3.tagutil.CategoryFinderService;

import play.*;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.*;
import views.html.*;

public class CategoryContoller extends Controller {
    
    public static Result displayCategories() {
    	
    	CategoryService cs = CategoryService.getInstance(); 
		List<Category> list = cs.find();
		
		if (list.size() > 0) {
			return ok(views.html.categoryList.render(list));
		}
		else {
			return notFound("Categories not found!");
		}    	
    }
       
    public static Result createDeleteOrUpdateCategoryForm(int id, String action) {
    	
    	CategoryService cs = CategoryService.getInstance();
    	Category cat = cs.find(id);
    	
    	if (action.equalsIgnoreCase("update")) {
    		return ok(views.html.updateCategoryForm.render(cat));
    	}
    	else {
    	   	return ok(views.html.deleteCategoryConfirmationForm.render(cat));
    	}
    }  
    
    public static Result createAddCategoryForm() {
    	return ok(views.html.addCategoryForm.render());
    }  
    
    @BodyParser.Of(BodyParser.FormUrlEncoded.class)
    public static Result doDeleteOrUpdateCategory(int id, String action) {
    	
    	CategoryService cs = CategoryService.getInstance();
    	
    	if (action.equalsIgnoreCase("Update")) {
    		
        	RequestBody body = request().body();
        	Map<String, String[]> formMap = body.asFormUrlEncoded();
        	
        	String categoryName = "";
        	String[] categoryNameArr = formMap.get("category-name");  
        	if (categoryNameArr.length > 0) {
        		categoryName = categoryNameArr[0]; 
        	}
        	
        	cs.update(id, categoryName);
    	}
    	else {
    		DCTransService ds = DCTransService.getInstance();
    		int count = ds.countForCategory(id);
    		
    		
    		if(count > 0) {
    			String catName = cs.find(id).getName();
    			return status(BAD_REQUEST, views.html.errorPage.render("Cannot delete category " + catName + " because one or more transactions are assigned to it. ", "Back to categories.", "/expenser/categories"));
    		}
    		
    		cs.delete(id);
    	}
    	return redirect("/expenser/categories");
    }

    @BodyParser.Of(BodyParser.FormUrlEncoded.class)
    public static Result doAddCategory() {
    	
    	CategoryService cs = CategoryService.getInstance();
    	
    	RequestBody body = request().body();
    	Map<String, String[]> formMap = body.asFormUrlEncoded();
    	
    	String categoryName = "";
    	String[] categoryNameArr = formMap.get("category-name");  
    	if (categoryNameArr.length > 0) {
    		categoryName = categoryNameArr[0]; 
    	}
    	
    	Category cat = new Category(categoryName);
    	
    	cs.create(cat);

    	return redirect("/expenser/categories");
    }    
}
