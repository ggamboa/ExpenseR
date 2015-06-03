import com.ggg.et3.global.DCTransServiceThreadLocal;
import com.ggg.et3.jpa.service.DCTransService;
import com.ggg.et3.tagutil.CategoryFinderService;

import play.*;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        Logger.info("Application has started");

        //CategoryFinderService categoryFinder = CategoryFinderService.getInstance();
        //Logger.info("Category finder service initialized.");
    }

    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }
}