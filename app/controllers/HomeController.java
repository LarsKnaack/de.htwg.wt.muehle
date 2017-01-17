package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.Controller;
import play.mvc.LegacyWebSocket;
import play.mvc.Result;
import play.mvc.WebSocket;
import play.routing.JavaScriptReverseRouter;
import utils.WebSocketUtils;
import views.html.index;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */

public class HomeController extends Controller {

    public static Result GO_HOME = redirect(routes.HomeController.index());

    public Result index() {
        return ok(index.render());
    }

    public Result rules() {
        return ok(views.html.rules.render());
    }

    public Result gui() {
        return ok(views.html.gui.render());
    }

    public Result tui() {
        return ok(views.html.tui.render());
    }

    public Result webSocketJS() {
        return ok(views.js.webSocket.render());
    }

    public Result authenticate(String username) {
        session("user", username);
        System.out.println("Authenticating user " + username);
        return GO_HOME;
    }

    public Result logout() {
        session().clear();
        return GO_HOME;
    }

    public Result javascriptRoutes() {
        return ok(
                JavaScriptReverseRouter.create("jsRoutes",
                        routes.javascript.HomeController.authenticate(),
                        routes.javascript.HomeController.logout())
        ).as("text/javascript");
    }

    public LegacyWebSocket<JsonNode> webSocket() {
        return WebSocket.whenReady((in, out) -> WebSocketUtils.start(in, out));
    }
}
