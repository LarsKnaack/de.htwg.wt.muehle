/**
 * Created by Lars on 11.01.2017.
 */
    // get websocket class, firefox has a different way to get it

var WS = window['MozWebSocket'] ? window['MozWebSocket'] : WebSocket;

var socket = new WS('@routes.HomeController.webSocket().webSocketURL(request)');

var writeMessages = function (event) {
    var json = JSON.parse(event.data);
    console.log("Got message: " + json);
    if(json["type"] === "chat") {
        $('#socket-messages').prepend('<p>' + json["data"] + '</p>');
    } else if (json["type"] === "update") {
        $('#tui-container').html('<pre>' + json["data"] + '</pre>');
    }
};

$(document).ready(function() {
    socket.onmessage = writeMessages;
    $('#tui-input').keyup(function (event) {
        var charCode = (event.which) ? event.which : event.keyCode;

        // if enter (charcode 13) is pushed, send message, then clear input field
        if (charCode === 13) {
            sendMessage("update", $(this).val());
            $(this).val('');
        }
    });

    $('#socket-input').keyup(function (event) {
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