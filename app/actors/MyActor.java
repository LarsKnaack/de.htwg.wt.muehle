package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.IPlayer;
import observer.IObserver;
import play.libs.Json;
import services.MuehleService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Lars on 22.01.2017.
 */
public class MyActor extends UntypedActor implements IObserver {

    private static final Set<ActorRef> subscribers = new HashSet<>();
    private static MuehleService muehleService = MuehleService.getInstance();
    private static final ObjectMapper mapper = new ObjectMapper();

    public MyActor(ActorRef out) {
        subscribers.add(out);
        muehleService.register(this);
    }

    public static Props props(ActorRef actorRef) {
        return Props.create(MyActor.class, actorRef);
    }

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof JsonNode) {
            handleMessage((JsonNode) msg);
        } else {
            unhandled(msg);
        }
    }

    private void handleMessage(JsonNode msg) {
        Map response = new HashMap<>();
        String type = msg.get("type").asText();
        JsonNode data = msg.get("data");
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

    private void broadcastMessage(JsonNode output) {
        for (ActorRef out : subscribers) {
            out.tell(output, self());
        }
    }

    @Override
    public void update(IPlayer currentPlayer, int anzMills, boolean gameEnded) {
        String log = "";
        if (gameEnded) {
            log = currentPlayer.getName() + " hat gewonnen!";
        } else if (anzMills > 0) {
            if (anzMills == 1) {
                log = currentPlayer.getName() + " hat eine Muehle, loesche einen Stein!";
            } else if (anzMills == 2) {
                log = currentPlayer.getName() + " hat zwei Muehlen, loesche zwei Steine!";
            }
        } else {
            log = currentPlayer.getName() + " ist an der Reihe!";
        }
        JsonNode node = Json.newObject().put("type", "log").put("data", log);
        broadcastMessage(node);
    }
}
