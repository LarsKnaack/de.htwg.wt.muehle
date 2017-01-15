/**
 * Created by Lars on 14.01.2017.
 */
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

function sendMessage(type, data) {
    var message = {"type": type, "data": data};
    console.log(JSON.stringify(message));
    socket.send(JSON.stringify(message));
}