package actors;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.routes;
import play.libs.ws.WSClient;
import play.mvc.Call;
import play.mvc.Http;
import service.Endpoints;

import java.util.LinkedList;
import java.util.List;

public class WebSocketActor extends UntypedAbstractActor {

    private static List<ActorRef> outConnections = new LinkedList<>();

    private static final ObjectMapper mapper = new ObjectMapper();
    WSClient client;

    public WebSocketActor(ActorRef out, WSClient client) {
        outConnections.add(out);
        this.client = client;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof JsonNode) {
            receiveMessage((JsonNode) message);
        }
    }

    public void receiveMessage(JsonNode jsonNode) {
        ObjectNode objectNode = mapper.createObjectNode();
        String type = jsonNode.get("type").asText();
        if (type.equals("chat") || type.equals("log")) {
            broadcastMessage(jsonNode);
        } else if (type.equals("update")) {
            //processMessage(routes.RestController.update());
        } else if (type.equals("input")) {
            String url = Endpoints.HANDLE_INPUT + jsonNode.get("data").asInt();
            processMessage(routes.RestController.
                    handleInput(jsonNode.get("data").asInt()));
        }
    }

    private void processMessage(Call call) {
        String url = call.url();
        System.out.println(url);
        ObjectNode objectNode = mapper.createObjectNode();
        client.url(url).get()
                .thenAcceptAsync((wsResponse) -> {
                    int status = wsResponse.getStatus();
                    if (200 <= status && status < 300) {
                        objectNode.put("type", "update");
                        objectNode.setAll((ObjectNode) wsResponse.asJson());
                    } else {
                        objectNode.put("type", "error");
                        objectNode.put("data",
                                String.format("%d: %s", wsResponse.getStatus(), wsResponse.getStatusText()));
                    }
                    broadcastMessage(objectNode);
                });
    }

    private void broadcastMessage(JsonNode jsonNode) {
        for (ActorRef out : outConnections) {
            out.tell(jsonNode, getSelf());
        }
    }
}
