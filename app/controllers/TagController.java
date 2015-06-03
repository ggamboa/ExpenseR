package controllers;

import java.util.List;

import com.ggg.et3.domain.CategoryTags;
import com.ggg.et3.domain.CategoryTagsSet;
import com.ggg.et3.jpa.entity.CategoryTag;
import com.ggg.et3.jpa.service.CategoryTagService;

import play.mvc.*;

public class TagController extends Controller {

     public static Result displayTags() {
     	
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

    public static Result createDeleteTagScreen(int id) {
    	CategoryTagService cts = CategoryTagService.getInstance();
    	CategoryTag tag = cts.find(id);
    	return ok(views.html.deleteTagConfirmationScreen.render(tag.getCategory().getName(), tag.getTag(), id));
    }  

    @BodyParser.Of(BodyParser.FormUrlEncoded.class)
    public static Result doDeleteTag(int id) {
    	
    	CategoryTagService cts = CategoryTagService.getInstance();
   		cts.delete(id);
    	return redirect("/expenser/tags");
    }
}
