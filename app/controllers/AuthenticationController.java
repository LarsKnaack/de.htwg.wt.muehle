package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import services.UserService;
import views.html.login;
import views.html.signup;

import java.util.Optional;

import static play.mvc.Controller.flash;
import static play.mvc.Controller.session;
import static play.mvc.Results.*;

/**
 * Created by Lars on 19.01.2017.
 */
public class AuthenticationController {

    private UserService userService = UserService.getInstance();

    private static Result GO_HOME = redirect(routes.HomeController.index());

    public Result login() {
        return ok(login.render(Form.form(User.class)));
    }

    public Result logout() {
        session().clear();
        return GO_HOME;
    }

    public Result signupForm() {
        return ok(signup.render(Form.form(User.class)));
    }

    public Result authenticate() {
        Form<User> loginform = DynamicForm.form(User.class).bindFromRequest();
        Optional<User> user = Optional.empty();
        if (!loginform.hasErrors()) {
            user = userService.authenticate(loginform.get());
        }
        if (user.isPresent()) {
            session().clear();
            session("email", user.get().email);
            return GO_HOME;
        } else {
            ObjectNode response = Json.newObject();
            response.put("success", false);
            response.put("errors", loginform.errorsAsJson());
            return badRequest(login.render(loginform));
        }
    }

    public Result signup() {
        Form<User> loginform = DynamicForm.form(User.class).bindFromRequest();

        ObjectNode response = Json.newObject();
        User account = loginform.get();
        boolean exists = userService.getUser(account).isPresent();

        if (loginform.hasErrors() || exists) {
            response.put("success", false);
            response.put("errors", loginform.errorsAsJson());
            if (exists)
                flash("errors", "Account already exists");
            return badRequest(signup.render(loginform));

        } else {
            userService.addUser(account);
            session().clear();
            session("email", account.email);
            return GO_HOME;
        }
    }
}
