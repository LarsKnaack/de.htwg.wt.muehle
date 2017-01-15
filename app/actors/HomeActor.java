package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import controller.IController;
import game.MuehleModule;
import model.IPlayer;
import observer.IObserver;
import view.Tui;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lars on 10.01.2017.
 */
public class HomeActor extends UntypedActor implements IObserver{

    private Tui tui;
    private final ActorRef out;
    private Injector injector;
    private final IController controller;
    private final ObjectMapper mapper;

    public HomeActor(ActorRef out) {
        this.out = out;
        injector = Guice.createInjector(new MuehleModule());
        controller = injector.getInstance(IController.class);
        controller.registerObserver(this);
        tui = new Tui(controller);
        mapper = new ObjectMapper();
    }

    public static Props props(ActorRef out) {
        return Props.create(HomeActor.class, out);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        Map response = new HashMap<>();
        if(message instanceof JsonNode) {
            String messageType = ((JsonNode) message).get("type").asText();
            String data = ((JsonNode) message).get("data").asText();
            response.put("type", messageType);
            if(messageType.equals("chat")) {
                response.put("data", data);
            } else if(messageType.equals("update")) {
                try {
                    tui.handleInput(Integer.parseInt(data));
                    tui.getVertexMap();
                    response.put("data", createJson(tui.getVertexMap()));
                } catch (NumberFormatException nfe) {
                    unhandled(message);
                    return;
                }
            }
        } else {
            unhandled(message);
            return;
        }
        JsonNode output = mapper.convertValue(response, JsonNode.class);
        out.tell(output, self());
    }

    private String createJson(Map<Integer, Character> vertexMap) {
        try {
            String result = mapper.writeValueAsString(vertexMap);
            return result;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(IPlayer currentPlayer, int anzMills, boolean gameEnded) {
        String log = "";
        if(gameEnded) {
            log = currentPlayer.getName() + " hat gewonnen!";
        } else if(anzMills > 0) {
            if(anzMills == 1) {
                log = currentPlayer.getName() + " hat eine Muehle, loesche einen Stein!";
            } else if(anzMills == 2) {
                log = currentPlayer.getName() + " hat zwei Muehlen, loesche zwei Steine!";
            }
        } else {
            log = currentPlayer.getName() + " ist an der Reihe!";
        }
    }
}
