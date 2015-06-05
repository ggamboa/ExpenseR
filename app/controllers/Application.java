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
    	
    	YearMonth yearMonth = null;
    	if(year != null && month != null) {
	    	try {
	    		int yr = Integer.parseInt(year);
	    		int mo = Integer.parseInt(month);
	    		yearMonth = new YearMonth(yr, mo);
	    		
	    	}
	    	catch(Exception e) {
	    		YearMonthAvailable yma = YearMonthAvailable.getInstance();
	    		yearMonth = yma.getLatest();
	    		
	    	}
    	}
    	
    	
    	if (yearMonth == null) {
    		YearMonthAvailable yma = YearMonthAvailable.getInstance();
    		yearMonth = yma.getLatest();
    		
    		if(yearMonth == null) {
    			return notFound("No report found!");
    		}
    	}

   		return displayReport(yearMonth.toString());

	}
	
	
    public static Result displayReport(String yrMo) {
    	
    	YearMonth yearMonth = null;
    	
    	if(yrMo == null) {
    		return index();
    	}
    	else {
    		try {
        		yearMonth = new YearMonth(yrMo);
        	}
        	catch(Exception e) {
        		return notFound("No report found!");
        	}
    	}
    	
    	int year = yearMonth.getYear();
    	int month = yearMonth.getMonthCode();

    	if(month < 1 || month > 12) {
    		return notFound("Not found - invalid month value: " + month);
    	}

    	Session session = Http.Context.current().session();
    	session.put("currentMonth", Integer.toString(month));
    	session.put("currentYear", Integer.toString(year));
    	
    	DCTransService ts = DCTransService.getInstance(); 
		List<DCTrans> list = ts.findDebits(year, month);

		Report report = new Report(year, month);
		report.load(list);
		
		List<CategorySet> catList = report.getAsList();
		
		if (catList.size() > 0) {
			return ok(views.html.report.render(catList, YearMonthAvailable.getInstance().getYearMonthList(), yearMonth, report.getTotalExpenses()));
		}
		else {
			return notFound();
		}    	
    }
	
    
    public static Result createUpdateTransactionForm(int id) {
    	
    	DCTransService ts = DCTransService.getInstance();
    	DCTrans tran = ts.findById(id);
    	return ok(views.html.updateTransactionForm.render(tran, CategoryList.getInstance().getList()));
    }
  
    
    @BodyParser.Of(BodyParser.FormUrlEncoded.class)
    public static Result doUpdateTransaction() {
    	
    	RequestBody body = request().body();
    	Map<String, String[]> formMap = body.asFormUrlEncoded();
    	
    	String[] dArr = formMap.get("description");  
    	String description = "";
    	if(dArr.length > 0) {
    		description = dArr[0];   
    	}
    	
    	String[] tArr = formMap.get("transaction-id");  
    	int tranId = 0;
    	if (tArr.length > 0) {
    		try {
    			tranId = Integer.parseInt(tArr[0]);
    		}
    		catch(Exception e) {}
    	}

    	String[] ciArr = formMap.get("category");
    	int categoryId = 0;
    	if (ciArr.length > 0) {
    		try {
    			categoryId = Integer.parseInt(ciArr[0]);
    		}
    		catch(Exception e) {}
    	}
    	
    	DCTransService ds = DCTransService.getInstance();
    	ds.update(tranId, categoryId, description);
     	
    	// Is tag provided in the form?
    	String[] tagArr = formMap.get("tag");
    	if (tagArr.length > 0 && tagArr[0] != null && tagArr[0].trim().length() > 0) {

    		//Add it to categoryFinder cache and permanent table
    		CategoryFinder cfs = CategoryFinder.getInstance();
    		cfs.addTag(categoryId, tagArr[0]);
    		
    		//Update all transactions with category associated for the tag 
    		try{
    			cfs.updateTransactionCategory();
    		}
    		catch(Exception e) {
    			return status(400, "Error updating transactions!");
    		}
    	}
    	
    	return redirect("/expenser/report");
    }

}
