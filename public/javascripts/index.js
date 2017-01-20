/**
 * Created by Lars on 17.01.2017.
 */

var test = "TEST";

$(document).ready(function () {
    var buttons = $(":button");
    buttons.click(function (e) {
        var theme = "color-" + this.getAttribute("id");
        $("body").removeClass();
        $("body").addClass(theme);
        $.ajax(jsRoutes.controllers.HomeController.setTheme(theme));
    });

    var aware = document.querySelector('#awareness');

    aware.handleSignInError = function (event) {
        this.status = JSON.stringify(event.detail);
    };

    aware.handleSignIn = function (response) {
        var username = gapi.auth2.getAuthInstance().currentUser.get().getBasicProfile().getName();
        //$.ajax(jsRoutes.controllers.HomeController.authenticate(username));
    };

    aware.handleSignOut = function (response) {
        //$.ajax(jsRoutes.controllers.HomeController.logout());
    };
})