package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Guice;
import com.google.inject.Injector;
import controller.IController;
import game.MuehleModule;
import model.IPlayer;
import observer.IObserver;

import java.util.Map;

/**
 * Created by Lars on 15.01.2017.
 */
public class MuehleUtils implements IObserver {

    private static final Injector injector = Guice.createInjector(new MuehleModule());
    private static final IController controller = injector.getInstance(IController.class);

    public MuehleUtils() {
        controller.registerObserver(this);
    }


    public Map<Integer, Character> handleInput(JsonNode data) {
        if (controller.getCurrentStonesToDelete() > 0) {
            controller.millDeleteStone(data.asInt());
        } else if (controller.requireInitial()) {
            controller.setStone(data.asInt());
        } else {
            controller.moveStone(data.asInt());
        }
        return controller.getVertexMap();
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
        WebSocketUtils.sendLog(log);
    }
}
