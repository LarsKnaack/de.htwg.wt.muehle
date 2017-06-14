package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.libs.Json;
import services.MuehleService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WebSocketActor extends UntypedAbstractActor {

    private static List<ActorRef> outConnections = new LinkedList<>();

    private static MuehleService muehleService = MuehleService.getInstance();

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Props props(ActorRef out) {
        return Props.create(WebSocketActor.class, out);
    }

    public WebSocketActor(ActorRef out) {
        outConnections.add(out);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof JsonNode) {
            handleMessage((JsonNode) message);
        }
    }

    public void handleMessage(JsonNode jsonNode) {

        Map response = new HashMap<>();
        String type = jsonNode.get("type").asText();
        JsonNode data = jsonNode.get("data");
        response.put("type", type);
        if (type.equals("chat")) {
            response.put("data", data);
        } else if (type.equals("update")) {
            Map<Integer, Character> vertexMap = muehleService.handleInput(data);
            response.put("data", createJson(vertexMap));
            response.put("stones", muehleService.getStoneCounters());
        }
        JsonNode output = mapper.convertValue(response, JsonNode.class);
        broadcastMessage(output);
    }


    private static String createJson(Map<Integer, Character> vertexMap) {
        try {
            String result = mapper.writeValueAsString(vertexMap);
            return result;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendLog(String log) {
        JsonNode json = Json.newObject().put("type", "log")
                .put("data", log);
        broadcastMessage(json);
    }

    private void broadcastMessage(JsonNode output) {
        for (ActorRef out : outConnections) {
            out.tell(output, getSelf());
        }
    }
}
