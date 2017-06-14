package controllers;

import actors.WebSocketActor;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import com.google.inject.Inject;
import models.User;
import play.libs.streams.ActorFlow;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.WebSocket;
import play.routing.JavaScriptReverseRouter;
import services.AuthenticatorService;
import services.UserService;
import views.html.index;

import java.util.Optional;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */

public class HomeController extends Controller {
    private static Result LOGIN = redirect(routes.AuthenticationController.login());
    private static UserService userService = UserService.getInstance();
    private String theme = "color-theme-4";
    private ActorSystem actorSystem;
    private Materializer materializer;

    @Inject
    public HomeController(ActorSystem actorSystem, Materializer materializer) {
        this.actorSystem = actorSystem;
        this.materializer = materializer;
    }

    @Security.Authenticated(AuthenticatorService.class)
    public Result index() {
        if (null != session("google")) {
            return ok(index.render(theme));
        }
        String email = session("email");
        Optional<User> user = userService.getUserByMail(email);
        if (!user.isPresent()) {
            session().clear();
            return LOGIN;
        }
        return ok(index.render(theme));
    }

    @Security.Authenticated(AuthenticatorService.class)
    public Result rules() {
        return ok(views.html.rules.render(theme));
    }

    @Security.Authenticated(AuthenticatorService.class)
    public Result gui() {
        return ok(views.html.gui.render(theme));
    }

    @Security.Authenticated(AuthenticatorService.class)
    public Result tui() {
        return ok(views.html.tui.render(theme));
    }

    /****************************** Utility Methods ****************************************/

    public Result webSocketJS() {
        return ok(views.js.webSocket.render());
    }

    public WebSocket webSocket() {
        return WebSocket.Json.accept(request ->
                ActorFlow.actorRef(WebSocketActor::props,
                        actorSystem, materializer
                )
        );
    }

    public Result setTheme(String theme) {
        this.theme = theme;
        return ok();
    }

    public Result jsRoutes() {
        return ok(
                JavaScriptReverseRouter.create("jsRoutes",
                        routes.javascript.HomeController.setTheme(),
                        routes.javascript.AuthenticationController.googleLogin(),
                        routes.javascript.AuthenticationController.logout()))
                .as("text/javascript");
    }

}