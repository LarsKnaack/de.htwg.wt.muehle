package controllers;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CompletionStage;

import com.google.inject.Guice;
import com.google.inject.Injector;
import controller.IController;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.WebSocket;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.LegacyWebSocket;

import play.data.Form;
import play.data.DynamicForm;

import play.libs.F;
import play.libs.Json;
import play.libs.openid.*;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fasterxml.jackson.databind.JsonNode;
import play.routing.JavaScriptReverseRouter;
import scala.util.parsing.json.JSON;
import services.MyAuthenticator;
import utils.WebSocketUtils;
import views.html.*;

import game.MuehleModule;
import model.IPlayer;
import observer.IObserver;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */

public class HomeController extends Controller {

    public static Result GO_HOME = redirect(routes.HomeController.index());

    private static final Injector injector = Guice.createInjector(new MuehleModule());

    private static Map<String, IController> controllers = new HashMap<>();

    private static Map<String, String> users = new HashMap<>();

    @Security.Authenticated(MyAuthenticator.class)
    public Result index() {
        String email = session("email");
        IController controller = controllers.get(email);
        if(controller == null) {
            session().clear();
            System.out.println("INIT");
            return this.login();
        }
        return ok(index.render());
    }

    public Result login() {
        return ok(login.render(Form.form(User.class)));
    }

    public Result logout() {
        String email = session("email");
        controllers.remove(email);
        session().clear();
        return redirect(routes.HomeController.index());
    }

    public Result signupForm() {
        return ok(signup.render(Form.form(User.class)));
    }

    public Result authenticate() {
        Form<User> loginform = DynamicForm.form(User.class).bindFromRequest();

        User user = User.authenticate(loginform.get());

        if (loginform.hasErrors() || user == null) {
            ObjectNode response = Json.newObject();
            response.put("success", false);
            response.put("errors", loginform.errorsAsJson());
            if(user == null) {
                flash("errors", "Wrong username or password");
            }
            return badRequest(login.render(loginform));
        } else {
            session().clear();
            session("email", user.email);
            IController controller = injector.getInstance(IController.class);
            controllers.put(user.email, controller);
            return GO_HOME;
        }
    }

    public Result signup() {
        Form<User> loginform = DynamicForm.form(User.class).bindFromRequest();

        ObjectNode response = Json.newObject();
        User account = loginform.get();
        boolean exists = users.containsKey(account.email);

        if (loginform.hasErrors() ||exists) {
            response.put("success", false);
            response.put("errors", loginform.errorsAsJson());
            if(exists)
                flash("errors", "Account already exists");

            return badRequest(signup.render(loginform));
        } else {
            users.put(loginform.get().email, loginform.get().password);
            session().clear();
            session("email", loginform.get().email);
            return redirect(routes.HomeController.index());
        }
    }

    public static class User {
        public String email;
        public String password;

        public User() { }

        private User(final String email, final String password) {
            this.email = email;
            this.password = password;
        }

        public static User authenticate(User user){
            if (user != null && users.containsKey(user.email) && users.get(user.email).equals(user.password)) {
                return new User(user.email, user.password);
            }
            return null;
        }
    }


    @Security.Authenticated(MyAuthenticator.class)
    public Result rules() {
        return ok(views.html.rules.render());
    }

    @Security.Authenticated(MyAuthenticator.class)
    public Result gui() {
        return ok(views.html.gui.render());
    }

    @Security.Authenticated(MyAuthenticator.class)
    public Result tui() {
        return ok(views.html.tui.render());
    }

    public Result webSocketJS() {
        return ok(views.js.webSocket.render());
    }

    /*public Result setsession(String username) {
        session("user", username);
        System.out.println("Authenticating user " + username);
        return GO_HOME;
    }

    public Result javascriptRoutes() {
        return ok(
                JavaScriptReverseRouter.create("jsRoutes",
                        routes.javascript.HomeController.setsession(),
                        routes.javascript.HomeController.logout())
        ).as("text/javascript");
    }*/

    public LegacyWebSocket<JsonNode> webSocket() {
        return WebSocket.whenReady((in, out) -> WebSocketUtils.start(in, out));
    }
}