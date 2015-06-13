package controllers;


import java.io.File;
import java.util.List;
import java.util.Map;

import com.ggg.et3.domain.Transaction;
import com.ggg.et3.domain.YearMonthAvailable;
import com.ggg.et3.loader.DataLoader;
import com.ggg.et3.loader.DataParser;
import com.ggg.et3.tagutil.CategoryFinder;

import play.Logger;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

public class DataController extends Controller {

    public static Result importData() {
     	
    	return ok(views.html.importData.render(("")));

    }

    public static Result processFile() {
    
    	MultipartFormData body = request().body().asMultipartFormData();
    	Map<String, String[]> map = request().body().asMultipartFormData().asFormUrlEncoded(); //request().getQueryString("data-source");
    	
    	String[] sources = map.get("data-source");
    	
    	FilePart uploadFile = body.getFile("data-file");
    	String fileName = uploadFile.getFilename();
    	
    	if (uploadFile != null) {
    
    		
    		String contentType = uploadFile.getContentType(); 
    		File file = uploadFile.getFile();
    		
    		DataParser parser = new DataParser(file, sources[0]);
    		DataLoader loader = new DataLoader();
    		
    		int recordsLoaded = 0;
    		try {
    			List<Transaction> list = parser.getTransactionList();
    			recordsLoaded = loader.process(list);
    			
    			YearMonthAvailable.refresh();
    		}
    		catch(Exception e) {
    			
    		}
    		
    		String msg = "File " + fileName + " uploaded! " + " " + recordsLoaded + " records were processed!";
    		
    		Logger.info("CategoryFinder will update all uncategorized transactions based on existing tags now.");
    	    CategoryFinder cfs = CategoryFinder.getInstance();
    	 	cfs.updateTransactionCategory();
    	 		
    		return ok(views.html.importData.render(msg));
    	} 
    	else {
     		flash("error", "Missing file");
    		return redirect(routes.Application.index());    
    	}
    }

}
