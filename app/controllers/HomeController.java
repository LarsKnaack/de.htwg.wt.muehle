package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import play.mvc.*;
import services.AuthenticatorService;
import services.UserService;
import services.WebSocketService;
import views.html.index;

import java.util.Optional;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */

public class HomeController extends Controller {
    private static Result LOGIN = redirect(routes.AuthenticationController.login());
    private static UserService userService = UserService.getInstance();

    @Security.Authenticated(AuthenticatorService.class)
    public Result index() {
        String email = session("email");
        Optional<User> user = userService.getUserByMail(email);
        if (!user.isPresent()) {
            session().clear();
            System.out.println("INIT");
            return LOGIN;
        }
        return ok(index.render());
    }

    @Security.Authenticated(AuthenticatorService.class)
    public Result rules() {
        return ok(views.html.rules.render());
    }

    @Security.Authenticated(AuthenticatorService.class)
    public Result gui() {
        return ok(views.html.gui.render());
    }

    @Security.Authenticated(AuthenticatorService.class)
    public Result tui() {
        return ok(views.html.tui.render());
    }

    public Result webSocketJS() {
        return ok(views.js.webSocket.render());
    }

    public LegacyWebSocket<JsonNode> webSocket() {
        return WebSocket.whenReady((in, out) -> WebSocketService.start(in, out));
    }
}