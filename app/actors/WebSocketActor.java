package actors;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.routes;
import play.libs.ws.WSClient;
import play.mvc.Call;
import play.mvc.Result;
import play.mvc.Results;
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
            //processMessage(routes.RestController.
              //      handleInput(jsonNode.get("data").asInt()));
        }
    }

    private void processMessage(Call call) {
        Result result = Results.redirect(call);
        System.out.println(result.body().toString());
    }

    private void broadcastMessage(JsonNode jsonNode) {
        for (ActorRef out : outConnections) {
            out.tell(jsonNode, getSelf());
        }
    }
}
