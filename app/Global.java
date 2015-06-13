import com.ggg.et3.global.DCTransServiceThreadLocal;
import com.ggg.et3.jpa.service.DCTransService;
import com.ggg.et3.tagutil.CategoryFinder;
import com.ggg.et3.tagutil.CategoryFinderService;

import play.*;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        Logger.info("Application has started");

        Logger.info("CategoryFinder will update all uncategorized transactions based on existing tags now.");
        CategoryFinder cfs = CategoryFinder.getInstance();
 		cfs.updateTransactionCategory();

    }

    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }
}