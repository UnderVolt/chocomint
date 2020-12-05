package io.undervolt.api.almendra;

import com.google.common.collect.Maps;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.undervolt.api.sambayon.Sambayon;
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
    private Socket socket;
    private final Sambayon sambayon;
    private final String ALMENDRA_ENDPOINT;
    private final Map<String, Tab> availableRooms = Maps.newHashMap();

    public Almendra(final Chocomint chocomint) throws URISyntaxException {
        this.chocomint = chocomint;
        this.chatManager = chocomint.getChatManager();
        this.sambayon = chocomint.getSambayon();
        this.ALMENDRA_ENDPOINT = this.sambayon.getServer("chat");

        System.out.println("Loaded Almendra");
        this.connectToSocket(this.ALMENDRA_ENDPOINT);

    }

    public void connectToSocket(final String endpoint) throws URISyntaxException {
        this.socket = IO.socket(endpoint).connect();

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
        if(message.getString("from").equals(this.chocomint.getUser())) return;
        if(message.getString("to").startsWith("#")) {
            this.getAvailableRooms().get(message.getString("to"))
                    .addMessage(message.getString("from"), message.getString("message"));
        } else {
            Tab tab = this.chatManager.getOrCreateTabByName(message.getString("from"));
            this.chatManager.addTab(tab);
            tab.addMessage(message.getString("from"), message.getString("message"));
        }

    }

    public void sendMessage(final Tab tab, final String message, final String user) {
        try {
            this.socket.emit("sendMessage", new JSONObject(new AlmendraMessage(user, tab.getName(), message).toString()));
            this.chatManager.getSelectedTab().addMessage(user, message);
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
