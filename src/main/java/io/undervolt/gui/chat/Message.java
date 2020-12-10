package io.undervolt.gui.chat;

import io.undervolt.gui.user.User;

public class Message {
    private final long timeMillis;
    private final String user;
    private final String message;

    public Message(final String user, final String message) {
        this.message = message;
        this.user = user;
        this.timeMillis = System.currentTimeMillis();
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
