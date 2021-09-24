package io.undervolt.gui.user;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.notifications.Notification;
import org.json.JSONObject;

import java.awt.*;

public class User {

    public enum Status {
        ONLINE, AWAY, BUSY, OFFLINE;
    }

    private final String username, image, alias, banner, createDate;
    private Status status;
    private final String countryCode;
    private final boolean developer;

    public User(final String username, final Status status, String countryCode, boolean developer, String image, String alias, String banner, String createDate) {
        this.status = status;
        this.username = username;
        this.countryCode = countryCode;
        this.developer = developer;
        this.image = image;
        this.alias = alias;
        this.banner = banner;
        this.createDate = createDate;
    }

    public final String getUsername() {
        return this.username;
    }

    public final Status getStatus() {
        return this.status;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public boolean isDeveloper() {
        return developer;
    }

    public String getAlias() {
        return alias;
    }

    public String getBanner() {
        if(banner == null || banner.equals("default")) return null;
        else return banner;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getImage() {
        if(image == null) return "a,iVBORw0KGgoAAAANSUhEUgAAApcAAAKXCAYAAADXbGGRAAAcNElEQVR4nO3dL2xc+bnH4bftDRiTgAlIV2MSMAFZyZaigAmpFNOYLHCgTQJS0IKQkpUKSgpCSgK6IIFNoU0nYEEGbMEYBHgqdcEYtJJdqQZjYqkXpM7uZjeJHb8+v/PnechFt/5J+e74c86cGf/s9u3b/w0AAEjw89IHAACgPcQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQl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";
        if(image.contains("default")) return "a,iVBORw0KGgoAAAANSUhEUgAAApcAAAKXCAYAAADXbGGRAAAcNElEQVR4nO3dL2xc+bnH4bftDRiTgAlIV2MSMAFZyZaigAmpFNOYLHCgTQJS0IKQkpUKSgpCSgK6IIFNoU0nYEEGbMEYBHgqdcEYtJJdqQZjYqkXpM7uZjeJHb8+v/PnechFt/5J+e74c86cGf/s9u3b/w0AAEjw89IHAACgPcQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQl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";
        else return image;
    }

    public boolean isOnline() {
        return this.status != Status.OFFLINE;
    }

    public String getStatusString() {
        switch(this.status) {
            case OFFLINE:
                return "Fuera de línea";
            case BUSY:
                return "Ocupado";
            case AWAY:
                return "Ausente";
            default:
                return "En línea";
        }
    }

    public int getStatusColor() {
        switch(this.status) {
            case OFFLINE:
                return new Color(77, 77, 77).getRGB();
            case BUSY:
                return new Color(255, 62, 62).getRGB();
            case AWAY:
                return new Color(255, 183, 62).getRGB();
            default:
                return new Color(83, 255, 62).getRGB();
        }
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void removeFriend() {
        this.setStatus(GameBridge.getChocomint().getAlmendra().getConnectedUsers().contains(this.getUsername()) ? Status.ONLINE : Status.OFFLINE);
        if (!this.getUsername().equals(GameBridge.getChocomint().getUser().getUsername()) && GameBridge.getChocomint().getFriendsManager().friendsPool.containsKey(this.username)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("token", GameBridge.getChocomint().getConfig().getToken());
                jsonObject.put("username", username);
            } catch (Exception e) {
                GameBridge.getChocomint().getNotificationManager().addNotification(new Notification(
                        Notification.Priority.ALERT, "Error", e.getMessage(), a -> {
                }
                ));
            }
            GameBridge.getChocomint().getRestUtils().sendJsonRequest("/api/deleteFriend", jsonObject, res -> {
                JsonObject response = new GsonBuilder().create().fromJson(res, JsonObject.class);
                if (response.get("code").getAsInt() == 200) {
                    GameBridge.getChocomint().getFriendsManager().friendsPool.remove(this.username);
                    GameBridge.getChocomint().getAlmendra().deleteFriendPacket(this.username);
                    GameBridge.getChocomint().getNotificationManager().addNotification(new Notification(
                            Notification.Priority.SOCIAL, "Amigo eliminado", username + " ahora ya no es tu amigo", a -> {
                    }
                    ));
                }
            });
        }
    }

    public void acceptFriendRequest() {
        this.setStatus(GameBridge.getChocomint().getAlmendra().getConnectedUsers().contains(this.getUsername()) ? Status.ONLINE : Status.OFFLINE);
        if (!this.getUsername().equals(GameBridge.getChocomint().getUser().getUsername()) && GameBridge.getChocomint().getFriendsManager().friendRequestPool.containsKey(this.username)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("token", GameBridge.getChocomint().getConfig().getToken());
                jsonObject.put("username", username);
            } catch (Exception e) {
                GameBridge.getChocomint().getNotificationManager().addNotification(new Notification(
                        Notification.Priority.ALERT, "Error", e.getMessage(), a -> {
                }
                ));
            }
            GameBridge.getChocomint().getRestUtils().sendJsonRequest("/api/acceptFriendRequest", jsonObject, res -> {
                JsonObject response = new GsonBuilder().create().fromJson(res, JsonObject.class);
                if (response.get("code").getAsInt() == 200) {
                    GameBridge.getChocomint().getFriendsManager().friendRequestPool.remove(this.username);
                    GameBridge.getChocomint().getFriendsManager().friendsPool.put(this.username, this);
                    GameBridge.getChocomint().getAlmendra().acceptFriendRequestPacket(this.username);
                    GameBridge.getChocomint().getNotificationManager().addNotification(new Notification(
                            Notification.Priority.SOCIAL, "Solicitud aceptada", username + " ahora es tu amigo!", a -> {
                    }
                    ));
                }
            });
        }
    }

    public void sendFriendRequest() {
        this.setStatus(GameBridge.getChocomint().getAlmendra().getConnectedUsers().contains(this.getUsername()) ? Status.ONLINE : Status.OFFLINE);
        if (!this.getUsername().equals(GameBridge.getChocomint().getUser().getUsername()) && !GameBridge.getChocomint().getFriendsManager().friendRequestPool.containsKey(this.username)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("token", GameBridge.getChocomint().getConfig().getToken());
                jsonObject.put("username", username);
            } catch (Exception e) {
                GameBridge.getChocomint().getNotificationManager().addNotification(new Notification(
                        Notification.Priority.ALERT, "Error", e.getMessage(), a -> {
                }
                ));
            }
            GameBridge.getChocomint().getRestUtils().sendJsonRequest("/api/sendFriendRequest", jsonObject, res -> {
                JsonObject response = new GsonBuilder().create().fromJson(res, JsonObject.class);
                if (response.get("code").getAsInt() == 200) {
                    GameBridge.getChocomint().getAlmendra().sendFriendRequestPacket(this.username);
                    GameBridge.getChocomint().getNotificationManager().addNotification(new Notification(
                            Notification.Priority.SOCIAL, "Solicitud enviada", "Se ha enviado la solicitud a " + username, a -> {
                    }
                    ));
                }
            });
        }
    }
}
