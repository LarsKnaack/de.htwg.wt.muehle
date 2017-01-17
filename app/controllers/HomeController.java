package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.Controller;
import play.mvc.LegacyWebSocket;
import play.mvc.Result;
import play.mvc.WebSocket;
import utils.WebSocketUtils;
import views.html.index;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */

public class HomeController extends Controller {

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

    public LegacyWebSocket<JsonNode> webSocket() {
        return WebSocket.whenReady((in, out) -> WebSocketUtils.start(in, out));
    }
}
