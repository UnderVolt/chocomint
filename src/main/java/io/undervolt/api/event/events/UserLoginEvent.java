package io.undervolt.api.event.events;

import io.undervolt.api.event.event.Event;
import io.undervolt.gui.user.User;

public class UserLoginEvent extends Event {
    private final User user;

    public UserLoginEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
