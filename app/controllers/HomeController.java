package controllers;

import actors.HomeActor;
import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.Controller;
import play.mvc.LegacyWebSocket;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.gui;
import views.html.index;
import views.html.rules;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private final LegacyWebSocket<JsonNode> socket = WebSocket.withActor(HomeActor::props);

    public Result index() {
        return ok(index.render("Lars"));
    }

    public Result rules() {
        return ok(rules.render());
    }

    public Result gui() {
        return ok(gui.render());
    }

    public Result tui() {
        return ok(views.html.tui.render());
    }

    public Result webSocketJS() {
        return ok(views.js.webSocket.render());
    }

    public LegacyWebSocket<JsonNode> webSocket(){
        return socket;
    }

}
