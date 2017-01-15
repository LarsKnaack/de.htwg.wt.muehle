/**
 * Created by Lars on 14.01.2017.
 */
var WS = window['MozWebSocket'] ? window['MozWebSocket'] : WebSocket;

var socket = new WS('ws://localhost:9000/ws');

var poly_list = {};

$(document).ready(function () {

    socket.onmessage = updateGui;
    var gamefield = $("#gamefield");
    for(var i = 1; i <=24; i++) {
        var div = document.createElement('div');
        var poly_elem = document.createElement('morris-app');
        poly_elem.setAttribute("color", "n");
        poly_elem.setAttribute("vertex", i.toString());
        poly_list[i.toString()] = poly_elem;
        div.appendChild(poly_elem);
        $(div).addClass("mills_fields" + i)
            .appendTo(gamefield);
        poly_elem.onclick = function() {
            sendMessage("update", this.getAttribute("vertex"));
        };
    }
});

function sendMessage(type, data) {
    var message = {"type": type, "data": data};
    socket.send(JSON.stringify(message));
}

function updateGui(event) {
    var json = JSON.parse(event.data);
    var type = json["type"];
    if (type === "update") {
        var data = JSON.parse(json["data"]);
        $.each(data, function (index, value) {
            poly_list[index].setAttribute("color", value);
        });
        Polymer.updateStyles();
    } else {
        var container = $("#log-container");
        container.text(json["data"]);
    }
}