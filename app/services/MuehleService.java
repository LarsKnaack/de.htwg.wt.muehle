package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Guice;
import com.google.inject.Injector;
import controller.IController;
import game.MuehleModule;
import observer.IObserver;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Lars on 15.01.2017.
 */
public class MuehleService {

    private static final Injector injector = Guice.createInjector(new MuehleModule());
    private static final IController controller = injector.getInstance(IController.class);
    private static final MuehleService MUEHLE_SERVICE = new MuehleService();

    private MuehleService() {
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
        return controller.getVertexMap();
    }

    public List<Integer> getStoneCounters() {
        return Arrays.asList(
                9 - controller.getSettedStonesPlayer1(),
                9 - controller.getSettedStonesPlayer2());
    }

    public void register(IObserver observer) {
        controller.registerObserver(observer);
    }
}
