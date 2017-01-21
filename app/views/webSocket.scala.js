/**
 * Created by Lars on 11.01.2017.
 */
    // get websocket class, firefox has a different way to get it

var WS = window['MozWebSocket'] ? window['MozWebSocket'] : WebSocket;

var socket = new WS('@routes.HomeController.webSocket().webSocketURL(request)');

var poly_list = {};

var writeMessages = function (event) {
    var json = JSON.parse(event.data);
    console.log("Got message: " + json);
    if(json["type"] === "chat") {
        $('#chat-messages').prepend('<p>' + json["data"] + '</p>');
    } else if (json["type"] === "update") {
        var data = JSON.parse(json["data"]);
        $.each(data, function (index, value) {
            poly_list[index].setAttribute("color", value);
        });
        Polymer.updateStyles();
    }
};

$(document).ready(function() {
    socket.onmessage = writeMessages;
    var gamefield = $("#gamefield");
    for (var i = 1; i <= 24; i++) {
        var div = document.createElement('div');
        var poly_elem = document.createElement('morris-app');
        poly_elem.setAttribute("color", "n");
        poly_elem.setAttribute("vertex", i.toString());
        poly_list[i.toString()] = poly_elem;
        div.appendChild(poly_elem);
        $(div).addClass("mills_fields" + i)
            .appendTo(gamefield);
        poly_elem.onclick = function () {
            sendMessage("update", this.getAttribute("vertex"));
        };
    }

    $('#chat-input').keyup(function (event) {
        var charCode = (event.which) ? event.which : event.keyCode;

        // if enter (charcode 13) is pushed, send message, then clear input field
        if (charCode === 13) {
            sendMessage("chat", $(this).val());
            $(this).val('');
        }
    });
})

function sendMessage(type, data) {
    var message = {"type": type, "data": data};
    console.log(JSON.stringify(message));
    socket.send(JSON.stringify(message));
}