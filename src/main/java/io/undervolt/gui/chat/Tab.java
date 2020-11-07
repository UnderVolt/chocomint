package io.undervolt.gui.chat;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.undervolt.gui.user.User;

import java.util.List;
import java.util.Map;

public class Tab {
    private final String name;
    private final int connected;
    private List<Message> messages = Lists.newArrayList();

    public Tab(final String name, final int connected) {
        this.connected = connected;
        this.name = name;
    }

    public void addMessage(final User user, final String message) {
        this.messages.add(new Message(user, message));
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getName() {
        return name;
    }

    public int getConnected() {
        return connected;
    }
}
