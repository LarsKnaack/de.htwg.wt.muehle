package services;

import controllers.HomeController;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by Lars on 17.01.2017.
 */
public class MyAuthenticator extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        return ctx.session().get("email");
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return HomeController.GO_HOME;
    }
}
