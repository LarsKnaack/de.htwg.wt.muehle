package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.mvc.WebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lars on 15.01.2017.
 */
public class WebSocketUtils {

    private static List<WebSocket.Out<JsonNode>> connections = new ArrayList<>();

    private static final MuehleUtils muehleUtils = new MuehleUtils();

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void start(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out) {
        connections.add(out);
        in.onMessage(jsonNode -> handleMessage(jsonNode));
    }

    public static void handleMessage(JsonNode jsonNode) {
        Map response = new HashMap<>();
        String type = jsonNode.get("type").asText();
        JsonNode data = jsonNode.get("data");
        response.put("type", type);
        if (type.equals("chat")) {
            response.put("data", data);
        } else if (type.equals("update")) {
            Map<Integer, Character> vertexMap = muehleUtils.handleInput(data);
            response.put("data", createJson(vertexMap));
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

    public static void sendLog(String log) {
        String json = "{\"type\": \"log\", \"data\": \"" + log + "\"}";
        try {
            broadcastMessage(mapper.readTree(json));
        } catch (IOException e) {
            //should not happen
            e.printStackTrace();

        }
    }

    private static void broadcastMessage(JsonNode output) {
        for (WebSocket.Out<JsonNode> out : connections) {
            out.write(output);
        }
    }
}
