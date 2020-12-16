package io.undervolt.api.almendra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.undervolt.api.event.events.GameShutdownEvent;
import io.undervolt.api.event.events.UserLoginEvent;
import io.undervolt.api.event.handler.EventHandler;
import io.undervolt.api.event.handler.Listener;
import io.undervolt.api.sambayon.Sambayon;
import io.undervolt.gui.chat.Chat;
import io.undervolt.gui.chat.ChatManager;
import io.undervolt.gui.chat.Message;
import io.undervolt.gui.chat.Tab;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Almendra implements Listener {
    private final Chocomint chocomint;
    private final ChatManager chatManager;
    private Socket socket;
    private final Sambayon sambayon;
    private final Minecraft mc;
    private final FontRenderer fontRenderer;
    private final String ALMENDRA_ENDPOINT;
    private final Map<String, Tab> availableRooms = Maps.newHashMap();
    private List<String> connectedUsers = Lists.newArrayList();
    private String MOTD;

    public Almendra(final Chocomint chocomint) throws URISyntaxException {
        this.chocomint = chocomint;
        this.chatManager = chocomint.getChatManager();
        this.sambayon = chocomint.getSambayon();
        this.ALMENDRA_ENDPOINT = this.sambayon.getServer("chat");
        this.mc = chocomint.getMinecraft();
        this.fontRenderer = this.mc.fontRendererObj;

        System.out.println("Loaded Almendra");
        if(!this.chocomint.getUser().getUsername().equals("Guest"))
            this.connectToSocket(this.ALMENDRA_ENDPOINT);

    }

    @EventHandler public void handleUserLogin(UserLoginEvent event) {
        if(!event.getUser().getUsername().equals("Guest")) {
            try {
                this.connectToSocket(this.ALMENDRA_ENDPOINT);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public void connectToSocket(final String endpoint) throws URISyntaxException {
        this.socket = IO.socket(endpoint).connect();

        socket.on(Socket.EVENT_CONNECT, args -> {
            System.out.println("Establecida conexión con los servidores de Almendra");
            socket.emit("join", this.chocomint.getUser().getUsername());

            socket.on("welcome", response -> {

                System.out.println("Conectado a Almendra con éxito.");

                String r = Arrays.toString(response);
                r = r.substring(1, r.length() - 1);

                JSONObject json;

                JSONArray rooms;
                JSONArray connectedUsers;
                String MOTD;

                try {

                    json = new JSONObject(r);

                    rooms = json.getJSONArray("rooms");
                    connectedUsers = json.getJSONArray("connectedUsers");
                    MOTD = json.getString("MOTD");

                    this.setMOTD(MOTD);

                    for (int i = 0; i < rooms.length(); i++) {

                        Tab tab = new Tab(true, rooms.getString(i), 0, false);
                        this.availableRooms.put(rooms.getString(i), tab);

                        if(i == 0) {
                            this.chatManager.addTab(tab);
                            this.chatManager.setSelectedTab(tab);
                            tab.addMessage(null, "\247e¡Bienvenido a chocomint!");
                            tab.addMessage(null, "\247c" + MOTD);
                        }
                    }

                    for (int i = 0; i < connectedUsers.length(); i++) {
                        this.connectedUsers.add(connectedUsers.getString(i));
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                this.setMOTD(r);

            });

            socket.on("userConnect", response -> {

                String username = Arrays.toString(response);
                username = username.substring(1, username.length() - 1);

                this.connectedUsers.add(username);
                System.out.println("Añadido " + username + " a la lista de usuarios conectados.");

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
        if(message.getString("from").equals(this.chocomint.getUser().getFormattedUsername())) return;
        if(message.getString("to").startsWith("#")) {
            this.getAvailableRooms().get(message.getString("to"))
                    .addMessage(message.getString("from"), message.getString("message"));
        } else {
            Tab tab = this.chatManager.getOrCreateTabByName(message.getString("from"));
            this.chatManager.addTab(tab);
            tab.addMessage(message.getString("from"), message.getString("message"));
            if(this.chatManager.getSelectedTab() != tab) {
                tab.setUnread();
                if(this.chocomint.getMinecraft().currentScreen != null && this.chocomint.getMinecraft().currentScreen instanceof Chat) {
                    System.out.println("Updated chat screen");
                    ((Chat) this.chocomint.getMinecraft().currentScreen).update(false);
                }
            }
        }

    }

    @EventHandler public void gameShutdownEvent(GameShutdownEvent event) {
        this.socket.emit("userDisconnect", this.chocomint.getUser().getUsername());
    }

    public void sendMessage(final Tab tab, final String message, final String user) {
        try {
            this.socket.emit("sendMessage", new JSONObject(new AlmendraMessage(user, tab.getName(), message).toString()));
            int width = this.chocomint.getGameBridge().getScaledResolution().getScaledWidth() - 5 - fontRenderer.getStringWidth(user + ": ");
            if(this.fontRenderer.getStringWidth(user + ": " + message) > width) {
                String splitSeq = message.contains(" ") ? " " : "(?!^)";
                String firstLine = "";
                String secondLine = "\247f";

                for (String msgSplit : message.split(splitSeq)){
                    if(this.fontRenderer.getStringWidth(firstLine + msgSplit) < width) {
                        firstLine = firstLine + msgSplit + splitSeq.replace("(?!^)", "");
                    } else {
                        secondLine = secondLine + msgSplit + splitSeq.replace("(?!^)", "");
                    }
                }

                this.chatManager.getSelectedTab().addMessage(user, firstLine);
                this.chatManager.getSelectedTab().addMessage(null, secondLine);
            } else {
                this.chatManager.getSelectedTab().addMessage(user, message);
            }
            this.chatManager.getSentMessages().add(new Message(user, message));
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

    public void addConnectedUser(final String username) {
        if(!this.connectedUsers.contains(username)) this.connectedUsers.add(username);
    }

    public void removeConnectedUser(final String username) {
        this.connectedUsers.remove(username);
    }

    public String getMOTD() {
        return MOTD;
    }

    public void setMOTD(String MOTD) {
        this.MOTD = MOTD;
    }

    public List<String> getConnectedUsers() {
        return connectedUsers;
    }
}
