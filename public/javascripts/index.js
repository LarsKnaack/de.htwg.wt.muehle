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
})