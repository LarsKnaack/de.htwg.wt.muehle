package controllers;

import actors.WebSocketActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.Materializer;
import com.google.inject.Inject;
import com.typesafe.config.ConfigValue;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.api.Play;
import play.libs.streams.ActorFlow;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.WebSocket;
import play.routing.JavaScriptReverseRouter;
import scala.Option;
import scala.collection.immutable.Set;
import service.RestService;
import services.AuthenticatorService;
import services.UserService;
import views.html.index;

import java.util.Arrays;
import java.util.Map;
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
    private WSClient client;
    private RestService restService;

    @Inject
    public HomeController(ActorSystem actorSystem, Materializer materializer, WSClient client, RestService restService) {
        this.actorSystem = actorSystem;
        this.materializer = materializer;
        this.client = client;
        this.restService = restService;
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
        return ok(views.html.webSocket.render(Play.current().asJava().config().getString("play.server.http.address")));
    }

    public WebSocket webSocket() {
        return WebSocket.Json.accept(request ->
                ActorFlow.actorRef(actorRef -> Props.create(WebSocketActor.class, actorRef, client),
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