package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Guice;
import com.google.inject.Injector;
import controller.IController;
import game.MuehleModule;
import model.IPlayer;
import observer.IObserver;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Lars on 15.01.2017.
 */
public class MuehleService implements IObserver {

    private static final Injector injector = Guice.createInjector(new MuehleModule());
    private static IController controller = injector.getInstance(IController.class);
    private static final MuehleService MUEHLE_SERVICE = new MuehleService();

    private MuehleService() {
        controller.registerObserver(this);
    }


    public static MuehleService getInstance() {
        return MUEHLE_SERVICE;
    }

    public Map<Integer, Character> handleInput(JsonNode data) {
        if (controller.getCurrentStonesToDelete() > 0) {
            controller.millDeleteStone(data.asInt());
        } else if (controller.requireInitial()) {
            controller.setStone(data.asInt());
        } else {
            controller.moveStone(data.asInt());
        }
        return getMap();
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
        //WebSocketService.sendLog(log);
    }

    public List<Integer> getStoneCounters() {
        return Arrays.asList(
                9 - controller.getSettedStonesPlayer1(),
                9 - controller.getSettedStonesPlayer2());
    }

    public Map<Integer, Character> getMap() {
        return controller.getVertexMap();
    }

    public void resetController() {
        controller = injector.getInstance(IController.class);
        controller.registerObserver(this);
    }
}
