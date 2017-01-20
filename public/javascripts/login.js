/**
 * Created by Lars on 20.01.2017.
 */
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