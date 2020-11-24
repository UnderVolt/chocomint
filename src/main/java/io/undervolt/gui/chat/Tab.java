package io.undervolt.gui.chat;

import com.google.common.collect.Lists;
import io.undervolt.gui.user.User;

import java.util.List;

public class Tab {
    private final String name;
    private final int connected;
    private List<Message> messages = Lists.newArrayList();
    private final boolean canSendMessages;

    public Tab(final boolean canSendMessages, final String name, final int connected) {
        this.connected = connected;
        this.name = name;
        this.canSendMessages = canSendMessages;
    }

    public void addMessage(final User user, final String message) {
        if(this.canSendMessages) this.messages.add(new Message(user, message));
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
