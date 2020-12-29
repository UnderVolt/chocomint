package io.undervolt.gui.user;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.RestUtils;
import io.undervolt.utils.config.Config;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class UserManager {
    private final Chocomint chocomint;
    private final RestUtils restUtils;
    private final Gson gson;
    private final Config config;

    private final UserProfilePictureManager userProfilePictureManager;
    private final CountryFlagManager countryFlagManager;
    private final Map<String, User> userPool = Maps.newHashMap();

    public UserManager(Chocomint chocomint) {
        this.chocomint = chocomint;
        this.restUtils = chocomint.getRestUtils();
        this.config = chocomint.getConfig();
        this.gson = new GsonBuilder().create();
        this.userProfilePictureManager = new UserProfilePictureManager();
        this.countryFlagManager = new CountryFlagManager();
    }

    public User setUser(final JsonObject tokenObj) {
        return this.setUser(this.gson.toJson(tokenObj));
    }

    public User setUser(final String token) {
        AtomicReference<User> user = new AtomicReference<>(new User("Guest", User.Status.ONLINE, null, false, null, null, null, null));
        if(token == null) return user.get();
        JSONObject json = new JSONObject();
        try {
            json.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.restUtils.sendJsonRequest("/api/user", json, res -> {
            System.out.println(res);
            JsonObject jsonObject = this.gson.fromJson(res, JsonObject.class);
            if(jsonObject.get("code").getAsInt() == 200) {
                JsonObject userObject = jsonObject.getAsJsonObject("user");
                user.set(new User(userObject.get("user").getAsString(),
                        User.Status.ONLINE,
                        userObject.get("country").getAsString(),
                        userObject.get("developer").getAsBoolean(),
                        userObject.get("image").getAsString(),
                        userObject.get("alias").getAsString(),
                        userObject.get("banner").getAsString(),
                        userObject.get("created").getAsString())
                );
                try {
                    this.chocomint.getFriendsManager().loadFriends(userObject.get("friends").getAsJsonObject().get("list").getAsJsonArray());
                } catch (JSONException e) {
                    e.printStackTrace();
                    this.chocomint.getNotificationManager().addNotification(new Notification(Notification.Priority.CRITICAL, "Error", "Failed to load friends list", (a)->{}));
                }
            }
        });
        return user.get();
    }

    public User getUser(final String username) {
        if(username == null) return null;
        else {
            if (this.userPool.containsKey(username)) return userPool.get(username);
            else {
                AtomicReference<User> user = new AtomicReference<>(new User(username, User.Status.OFFLINE, null, false, "default", null, "default", null));
                JSONObject json = new JSONObject();
                try {
                    json.put("username", username);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                this.restUtils.sendJsonRequest("/api/userInfo", json, res -> {
                    JsonObject jsonObject = this.gson.fromJson(res, JsonObject.class);
                    if (jsonObject.get("code").getAsInt() == 200) {
                        JsonObject userObject = jsonObject.getAsJsonObject("user");
                        user.set(new User(userObject.get("user").getAsString(),
                                User.Status.ONLINE,
                                userObject.get("country").getAsString(),
                                userObject.get("developer").getAsBoolean(),
                                userObject.get("image").getAsString(),
                                userObject.get("alias").getAsString(),
                                userObject.get("banner").getAsString(),
                                userObject.get("created").getAsString())
                                );
                        this.userPool.put(username, user.get());
                    }
                });
                return user.get();
            }
        }
    }

    public UserProfilePictureManager getUserProfilePictureManager() {
        return userProfilePictureManager;
    }

    public CountryFlagManager getCountryFlagManager() {
        return countryFlagManager;
    }
}
