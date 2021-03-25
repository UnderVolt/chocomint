package io.undervolt.gui.friends;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.gui.user.User;
import io.undervolt.gui.user.UserManager;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.RestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class FriendsManager {

    private final Chocomint chocomint;
    private final UserManager userManager;
    private final RestUtils restUtils;
    private final Gson gson;

    public final Map<String, User> friendsPool = Maps.newHashMap();
    public final Map<String, User> friendRequestPool = Maps.newHashMap();

    public FriendsManager() {
        this.chocomint = GameBridge.getChocomint();
        this.userManager = chocomint.getUserManager();
        this.restUtils = chocomint.getRestUtils();
        this.gson = new GsonBuilder().create();
    }

    public void loadFriends(List<String> friendList) {
        for(String friendName : friendList) {
            this.friendsPool.put(friendName, userManager.getUser(friendName));
        }
    }

    public void loadFriends(JsonArray friendList) throws JSONException {
        int len = friendList.size();
        for (int i = 0; i < len; i++){
            this.friendsPool.put(friendList.get(i).getAsString(), userManager.getUser(friendList.get(i).getAsString()));
        }
    }

    public void loadFriendRequests(JsonArray friendList) throws JSONException {
        int len = friendList.size();
        for (int i = 0; i < len; i++){
            this.friendRequestPool.put(friendList.get(i).getAsString(), userManager.getUser(friendList.get(i).getAsString()));
        }
    }

    public void setFriendStatus(String username, User.Status status) {
        this.friendsPool.get(username).setStatus(status);
    }

    public void removeFriend(String username) {
        this.friendsPool.remove(username);
    }

    public void addFriend(String username) {
        this.friendsPool.put(username, userManager.getUser(username));
    }
}
