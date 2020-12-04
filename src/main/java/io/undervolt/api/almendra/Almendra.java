package io.undervolt.api.almendra;

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
import java.util.Map;

public class Almendra {
    private final Chocomint chocomint;
    private final ChatManager chatManager;
    private final Socket socket;
    private Map<String, Tab> availableRooms;

    public Almendra(final Chocomint chocomint) throws URISyntaxException {
        this.chocomint = chocomint;
        this.chatManager = chocomint.getChatManager();

        System.out.println("Loaded Almendra");
        socket = IO.socket("http://192.168.0.2:1356").connect();

        socket.on(Socket.EVENT_CONNECT, args -> {
            System.out.println("Conectado a Almendra con éxito.");
            socket.emit("join", this.chocomint.getUser());
            socket.on("availableRooms", response -> {
                System.out.println("Obtenida lista de rooms disponibles");
                JSONArray rooms;

                try {
                    rooms = new JSONObject(response).getJSONArray("rooms");
                    for (int i = 0; i < rooms.length(); i++){
                        System.out.println("Room: " + rooms.getString(i));
                        this.availableRooms.put(rooms.getString(i), new Tab(true, rooms.getString(i), 0, false));
                    }

                    this.chatManager.addTab(this.availableRooms.get("#english"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            })
            .on("message", message -> {
                try {
                    this.receiveMessage(new JSONObject(message));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public void receiveMessage(final JSONObject message) throws JSONException {
        Tab theTab = this.chatManager.getOrCreateTabByName(message.getString("tabName"));
        theTab.addMessage(new Message(message.getString("from"), message.getString("message")));
    }

    public void sendMessage(final Tab tab, final String message, final String user) {
        this.socket.emit("message", new JSONObject(new AlmendraMessage(user, tab.getName(), message)));
        tab.addMessage(new Message(user, message));
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
    }
}
