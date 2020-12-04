package io.undervolt.api.almendra;

import com.google.common.collect.Maps;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.undervolt.gui.chat.ChatManager;
import io.undervolt.gui.chat.Message;
import io.undervolt.gui.chat.Tab;
import io.undervolt.instance.Chocomint;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;

public class Almendra {
    private final Chocomint chocomint;
    private final ChatManager chatManager;
    private final Socket socket;
    private final Map<String, Tab> availableRooms = Maps.newHashMap();

    public Almendra(final Chocomint chocomint) throws URISyntaxException {
        this.chocomint = chocomint;
        this.chatManager = chocomint.getChatManager();

        System.out.println("Loaded Almendra");
        socket = IO.socket("http://localhost:1356").connect();

        socket.on(Socket.EVENT_CONNECT, args -> {
            System.out.println("Conectado a Almendra con éxito.");
            socket.emit("join", this.chocomint.getUser());

            socket.on("availableRooms", response -> {
                System.out.println("Obtenida lista de salas disponibles.");

                String r = Arrays.toString(response);
                r = r.substring(1, r.length() - 1);

                JSONArray rooms;

                try {
                    rooms = new JSONObject(r).getJSONArray("rooms");

                    for (int i = 0; i < rooms.length(); i++) {
                        this.availableRooms.put(rooms.getString(i), new Tab(true, rooms.getString(i), 0, false));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

            socket.on("receiveMessage", message -> {

                String r = Arrays.toString(message);
                r = r.substring(1, r.length() - 1);

                try {
                    this.receiveMessage(new JSONObject(r));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public void receiveMessage(final JSONObject message) throws JSONException {
        System.out.println(message.getString("from") + " (" + message.getString("to") + "): " + message.getString("message"));
        if(message.getString("to").startsWith("#")) {
            this.getAvailableRooms().get(message.getString("to"))
                    .addMessage(message.getString("from"), message.getString("message"));
        } else {
            this.chatManager.getOrCreateTabByName(message.getString("from"));
            this.chatManager.getOrCreateTabByName(message.getString("from"))
                    .addMessage(message.getString("from"), message.getString("message"));
        }
        //this.chatManager.getOrCreateTabByName("#español").addMessage(new Message(message.getString("from"), message.getString("message")));

    }

    public void sendMessage(final Tab tab, final String message, final String user) {
        try {
            this.socket.emit("sendMessage", new JSONObject(new AlmendraMessage(user, tab.getName(), message).toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Tab> getAvailableRooms() {
        return availableRooms;
    }

    static class AlmendraMessage {
        public final String from, to, message;

        public AlmendraMessage(String from, String to, String message) {
            this.from = from;
            this.to = to;
            this.message = message;
        }

        public String toString() {
            return "{\"from\":\"" + from + "\", \"to\":\"" + to + "\", \"message\":\"" + message + "\"}";
        }
    }
}
