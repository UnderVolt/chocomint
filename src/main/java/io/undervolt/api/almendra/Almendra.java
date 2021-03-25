package io.undervolt.api.almendra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.undervolt.api.event.events.ConnectToAlmendraEvent;
import io.undervolt.api.event.events.GameShutdownEvent;
import io.undervolt.api.event.events.UserLoginEvent;
import io.undervolt.api.event.handler.EventHandler;
import io.undervolt.api.event.handler.Listener;
import io.undervolt.api.sambayon.Sambayon;
import io.undervolt.gui.chat.Chat;
import io.undervolt.gui.chat.ChatManager;
import io.undervolt.gui.chat.Tab;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.gui.user.User;
import io.undervolt.gui.user.UserScreen;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private boolean isAuthenticated = false;

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
            this.connectToSocket(this.ALMENDRA_ENDPOINT);
        }
    }

    public void connectToSocket(final String endpoint) {
        try {
            this.socket = IO.socket(endpoint).connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.on(Socket.EVENT_CONNECT, args -> {
            System.out.println("Establecida conexión con los servidores de Almendra");
            socket.emit("join", new AlmendraSession(this.chocomint.getConfig().getToken(), this.chocomint.getUser().getUsername()).getParsedData());

            socket.on("auth-fail", response -> {
                this.isAuthenticated = false;
                try {
                    throw new Exception(Arrays.toString(response));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            socket.on("welcome", response -> {

                System.out.println("Conectado a Almendra con éxito.");
                this.isAuthenticated = true;

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
                            tab.addMessage(null, "\247aConectando a Almendra...");
                            tab.addMessage(null, "\247e¡Bienvenido a Almendra!");
                            tab.addMessage(null, "\247c" + MOTD);
                        }
                    }

                    for (int i = 0; i < connectedUsers.length(); i++) {
                        this.connectedUsers.add(connectedUsers.getString(i));
                        if(this.chocomint.getFriendsManager().friendsPool.get(connectedUsers.getString(i)) != null)
                            this.chocomint.getFriendsManager().setFriendStatus(connectedUsers.getString(i), User.Status.ONLINE);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                this.setMOTD(r);
                this.chocomint.getEventManager().callEvent(new ConnectToAlmendraEvent());

            });

            socket.on("userConnect", response -> {

                String username = Arrays.toString(response);
                username = username.substring(1, username.length() - 1);

                this.connectedUsers.add(username);

                if(this.chocomint.getFriendsManager().friendsPool.get(username) != null) {
                    this.chocomint.getFriendsManager().setFriendStatus(username, User.Status.ONLINE);
                    this.chocomint.getNotificationManager().addNotification(
                            new Notification(Notification.Priority.NOTICE, username, "Se ha conectado", (a)->{}));
                }

                System.out.println("Añadido " + username + " a la lista de usuarios conectados.");

            });

            socket.on("userDisconnected", response -> {
                String username = Arrays.toString(response);
                username = username.substring(1, username.length() - 1);

                this.connectedUsers.remove(username);

                if(this.chocomint.getFriendsManager().friendsPool.get(username) != null)
                    this.chocomint.getFriendsManager().setFriendStatus(username, User.Status.OFFLINE);

                System.out.println("Removido " + username + " a la lista de usuarios conectados.");
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

            socket.on("receiveFriendRequest", username -> {
                String r = Arrays.toString(username);
                r = r.substring(1, r.length() - 1);

                if(!this.chocomint.getFriendsManager().friendRequestPool.containsKey(r)) {
                    this.chocomint.getFriendsManager().friendRequestPool.put(r, this.chocomint.getUserManager().getUser(r));
                    String finalR = r;
                    this.chocomint.getNotificationManager().addNotification(
                            new Notification(Notification.Priority.SOCIAL, "Nueva solicitud de amistad", r + "quiere ser tu amigo",
                                    a->this.mc.displayGuiScreen(new UserScreen(a, chocomint, this.chocomint.getUserManager().getUser(finalR))))
                    );
                }
            });

            socket.on("friendRequestAccepted", username -> {
                String r = Arrays.toString(username);
                r = r.substring(1, r.length() - 1);

                this.chocomint.getFriendsManager().friendsPool.put(r, this.chocomint.getUserManager().getUser(r));
                String finalR = r;
                this.chocomint.getNotificationManager().addNotification(
                        new Notification(Notification.Priority.SOCIAL, "Solicitud de amistad aceptada", "Ahora " + r + "es tu amigo!",
                                a->this.mc.displayGuiScreen(new UserScreen(a, chocomint, this.chocomint.getUserManager().getUser(finalR))))
                );
            });

            socket.on("friendDeletion", username -> {
                String r = Arrays.toString(username);
                r = r.substring(1, r.length() - 1);

                this.chocomint.getFriendsManager().friendsPool.remove(r);
                this.chocomint.getNotificationManager().addNotification(
                        new Notification(Notification.Priority.SOCIAL, "Amigo eliminado", "Ahora " + r + "ya no es tu amigo", a-> {})
                );
            });

            socket.on(Socket.EVENT_DISCONNECT, data -> {
                this.handleEndOfData();
                this.chocomint.getNotificationManager().addNotification(new Notification(Notification.Priority.CRITICAL, "Desconectado de Almendra", "Reconectando...", (a)->{}));
            });

            socket.on(Socket.EVENT_CONNECT_ERROR, data -> {
                this.handleEndOfData();
            });

            socket.on(Socket.EVENT_RECONNECT_FAILED, data -> {
                this.handleEndOfData();
            });

            socket.on(Socket.EVENT_CONNECT_TIMEOUT, data -> {
                this.handleEndOfData();
            });

        });
    }

    public void handleEndOfData() {
        this.setMOTD(null);
        this.getConnectedUsers().clear();
        this.getAvailableRooms().clear();
        this.isAuthenticated = false;
        if(this.mc.currentScreen instanceof Chat) {
            ((Chat) this.mc.currentScreen).update(false);
        }
    }

    public void sendFriendRequestPacket(String username) {
        this.socket.emit("sendFriendRequest", username);
        System.out.println("[A] Se ha enviado una solicitud de amistad a " + username);
    }

    public void acceptFriendRequestPacket(String username) {
        this.socket.emit("acceptFriendRequest", username);
        System.out.println("[A] Se ha aceptado la solicitud de amistad de " + username);
    }

    public void deleteFriendPacket(String username) {
        this.socket.emit("deleteFriend", username);
        System.out.println("[A] Se ha eliminado a " + username + " como amigo.");
    }

    public void receiveMessage(final JSONObject message) throws JSONException {
        System.out.println((message.getBoolean("developer") ? "§9" : "") +  message.getString("from") + " (" + message.getString("to") + "): " + message.getString("message"));
        if(message.getString("from").equals(this.chocomint.getUser().getUsername())) return;
        if(message.getString("to").startsWith("#")) {
            this.getAvailableRooms().get(message.getString("to"))
                    .addMessage((message.getBoolean("developer") ? "§9" : "") +  message.getString("from"), message.getString("message"));
        } else {
            Tab tab = this.chatManager.getOrCreateTabByName(message.getString("from"));
            this.chatManager.addTab(tab);
            tab.addMessage((message.getBoolean("developer") ? "§9" : "") + message.getString("from"), message.getString("message"));
            if(this.chatManager.getSelectedTab() != tab) {
                tab.setUnread();
                if(this.chocomint.getMinecraft().currentScreen != null && this.chocomint.getMinecraft().currentScreen instanceof Chat) {
                    System.out.println("Updated chat screen");
                    ((Chat) this.chocomint.getMinecraft().currentScreen).update(false);
                } else {
                    this.chocomint.getNotificationManager().addNotification(
                            new Notification(Notification.Priority.SOCIAL, (message.getBoolean("developer") ? "§9" : "") +  message.getString("from"), message.getString("message"),
                                    (previous) -> {
                                        try {
                                            this.chatManager.setSelectedTab(this.chatManager.getOrCreateTabByName(message.getString("from")));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        this.mc.displayGuiScreen(new Chat("", previous, this.chocomint, this.chocomint.getMinecraft().getCurrentServerData()));
                                    })
                    );
                }
            }
        }

    }

    @EventHandler public void gameShutdownEvent(GameShutdownEvent event) {
        if(!this.chocomint.getUser().getUsername().equals("Guest"))
            this.disconnect();
    }

    public void disconnect() {
        this.socket.emit("userDisconnect", this.chocomint.getUser().getUsername());
        this.socket.disconnect();
    }

    public void sendMessage(final Tab tab, final String message, final User user) {
        if(this.getConnectedUsers().contains(tab.getName()) || tab.getName().startsWith("#")) {
            this.socket.emit("sendMessage", new AlmendraMessage(user, tab.getName(), message).getParsedData());
            this.chatManager.getSelectedTab().addMessage((user.isDeveloper() ? "§9" : "") + user.getUsername(), message);
        } else {
            this.chatManager.getSelectedTab().addMessage(null, "\247cEl usuario que estás intentando contactar está desconectado.");
        }
    }

    public Map<String, Tab> getAvailableRooms() {
        return availableRooms;
    }

    static class AlmendraSession {
        public final String token, username;

        public AlmendraSession(String token, String username) {
            this.token = token;
            this.username = username;
        }

        public JSONObject getParsedData() {
            final JSONObject theObject = new JSONObject();
            try {
                theObject.put("token", this.token);
                theObject.put("username", this.username);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return theObject;
        }
    }

    static class AlmendraMessage {
        public final String from, to, message;
        public boolean developer;

        public AlmendraMessage(User user, String to, String message) {
            this.from = user.getUsername();
            this.developer = user.isDeveloper();
            this.to = to;
            this.message = message;
        }

        public JSONObject getParsedData() {
            final JSONObject theObject = new JSONObject();
            try {
                theObject.put("from", this.from);
                theObject.put("to", this.to);
                theObject.put("message", this.message);
                theObject.put("developer", this.developer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return theObject;
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

    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}
