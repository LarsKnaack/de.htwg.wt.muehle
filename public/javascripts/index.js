/**
 * Created by Lars on 17.01.2017.
 */

$(document).ready(function () {
    var buttons = $(":button");
    $("body").addClass("color-theme-1");
    buttons.click(function (e) {
        var id = this.getAttribute("id");
        $("body").removeClass();

        $("body").addClass("color-" + id);

    });

    var aware = document.querySelector('#awareness');
    aware.userName = '';

    aware.handleSignInError = function (event) {
        this.status = JSON.stringify(event.detail);
    };

    aware.handleSignIn = function (response) {
        this.status = 'Signin granted';
        this.userName = gapi.auth2.getAuthInstance().currentUser.get().getBasicProfile().getName();
        var navbar = $('#my-navbar');
        //alert(this.userName);
        window.location = "/gui";
        // console.log('[Aware] Signin Response', response);

    };

    aware.handleSignOut = function (response) {
        this.status = 'Signed out';
        this.userName = '';
    };
})