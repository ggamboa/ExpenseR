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

public class Application extends Controller {

	public static Result index() {
		
Session session = Http.Context.current().session();
    	
    	String month = session.get("currentMonth");
    	String year = session.get("currentYear");
    	
    	if(month != null && year != null) {
    		return redirect("/expenser/report/" + year + "/" + month);
    	}
    	else {
    		YearMonthAvailable yma = YearMonthAvailable.getInstance();
    		YearMonth ym = yma.getLatest();
    		if(ym != null) {
    			return redirect("/expenser/report/" + ym.getYear() + "/" + ym.getMonthCode());
    		}
    		else {
    			return redirect("/expenser/report/2015/12");
    		}
    	}
	}
	
    public static Result displayReport(String yrMo) {
    	
    	YearMonth yearMonth = null;
    	try {
    		yearMonth = new YearMonth(yrMo);
    		return redirect("/expenser/report/" + yearMonth.getYear() + "/" + yearMonth.getMonthCode());
    	}
    	catch(Exception e) {
    		return notFound();
    	}
    }
    
    public static Result deleteCategory(int id) {
    	return status(NOT_IMPLEMENTED, views.html.notImplemented.render());
    }
 
    
    public static Result displayReport2(int year, int month) {
    	
    	Session session = Http.Context.current().session();
    	
    	if(month < 1 || month > 12) {
    		return notFound();
    	}

    	session.put("currentMonth", Integer.toString(month));
    	session.put("currentYear", Integer.toString(year));
    	
    	DCTransService ts = DCTransService.getInstance(); 
		List<DCTrans> list = ts.findDebits(year, month);

		Report report = new Report(year, month);
		report.load(list);
		
		List<CategorySet> catList = report.getAsList();
		
		if (catList.size() > 0) {
			return ok(views.html.report.render(catList, YearMonthAvailable.getInstance().getYearMonthList(), new YearMonth(year, month), report.getTotalExpenses()));
		}
		else {
			return notFound();
		}    	

    }
    
    public static Result displayCategories() {
    	
    	Session session = Http.Context.current().session();
    	
    	String month = session.get("currentMonth");
    	String year = session.get("currentYear");
    	
    	CategoryService cs = CategoryService.getInstance(); 
		List<Category> list = cs.find();
		
		
		if (list.size() > 0) {
			return ok(views.html.categoryList.render(list));
		}
		else {
			return notFound();
		}    	

    }
       
    public static Result displayTags() {
    	
    	Session session = Http.Context.current().session();
    	
    	String month = session.get("currentMonth");
    	String year = session.get("currentYear");

    	CategoryTagsSet cts = new CategoryTagsSet();
    	List<CategoryTags> list = cts.getCategoryTagsList();
    	
 		if (list.size() > 0) {
			return ok(views.html.categoryTagList.render(list));
		}
		else {
			return ok(views.html.categoryTagList.render(list));
			//return notFound();
		}    	

    }

    public static Result createDeleteTagScreen(String category, String tag, int id) {
    	Session session = Http.Context.current().session();
    	
    	
   	   	return ok(views.html.deleteTagConfirmationScreen.render(category, tag, id));

    }  

    @BodyParser.Of(BodyParser.FormUrlEncoded.class)
    public static Result doDeleteTag(int id) {
    	
    	CategoryTagService cts = CategoryTagService.getInstance();
    	
   		cts.delete(id);

    	return redirect("/expenser/tags");
    }
    
    public static Result createUpdateTransactionForm(int id) {
    	Session session = Http.Context.current().session();
    	
    	String year = session.get("currentYear");
    	String month = session.get("currentMonth");
    	
    	DCTransService ts = DCTransService.getInstance();
    	DCTrans tran = ts.findById(id);
    	return ok(views.html.updateTransactionForm.render(tran, CategoryList.getInstance().getList(), year, month ));
    }
  

    
    public static Result createDeleteOrUpdateCategoryForm(int id, String action) {
    	Session session = Http.Context.current().session();
    	
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
    public static Result doUpdateTransaction() {
    	
    	RequestBody body = request().body();
    	Map<String, String[]> formMap = body.asFormUrlEncoded();
    	
    	String[] descriptionArr = formMap.get("description");  
    	String description = descriptionArr[0];   
    	
    	String[] tranIdArr = formMap.get("transaction-id");  
    	int tranId = Integer.parseInt(tranIdArr[0]);   
    	
    	String[] categoryIdArr = formMap.get("category");
    	int categoryId = Integer.parseInt(categoryIdArr[0]);
    	
    	//String[] yrmoArr = formMap.get("yearMonth");
    	//String yrmo = yrmoArr[0];
    	
    	DCTransService ds = DCTransService.getInstance();
    	ds.updateCategory(tranId, categoryId);
    	
     	
    	Session session = Http.Context.current().session();
    	
    	String year = session.get("currentYear");
    	String month = session.get("currentMonth");

    	//add tag if provided
    	String[] tagArr = formMap.get("tag");
    	if (tagArr.length > 0 && tagArr[0] != null && tagArr[0].trim().length() > 0) {
    		//CategoryFinderService cfs = CategoryFinderService.getInstance();
    		CategoryFinder cfs = CategoryFinder.getInstance();
    		cfs.addTag(categoryId, tagArr[0]);
    		try{
    			cfs.updateTransactionCategory(new YearMonth(Integer.parseInt(year), Integer.parseInt(month)));
    		}
    		catch(Exception e) {}
    	}
    	
    	return redirect("/expenser/report/" + year + "/" + month);
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
