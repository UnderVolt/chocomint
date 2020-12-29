package io.undervolt.gui.friends;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import io.undervolt.gui.user.User;
import io.undervolt.gui.user.UserManager;
import io.undervolt.instance.Chocomint;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Map;

public class FriendsManager {

    private final Chocomint chocomint;
    private final UserManager userManager;

    public final Map<String, User> friendsPool = Maps.newHashMap();
    public final Map<String, User> friendRequestPool = Maps.newHashMap();

    public FriendsManager(final Chocomint chocomint) {
        this.chocomint = chocomint;
        this.userManager = chocomint.getUserManager();
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
