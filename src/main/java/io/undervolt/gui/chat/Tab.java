package io.undervolt.gui.chat;

import com.google.common.collect.Lists;
import io.undervolt.gui.user.User;

import java.util.List;

public class Tab {
    private final String name;
    private final int connected;
    private final boolean isClientReserved;
    private List<Message> messages = Lists.newArrayList();
    private final boolean canSendMessages;

    public Tab(final boolean canSendMessages, final String name, final int connected, final boolean isClientReserved) {
        this.connected = connected;
        this.name = name;
        this.canSendMessages = canSendMessages;
        this.isClientReserved = isClientReserved;
    }

    public void addMessage(final Message message) {
        this.messages.add(message);
    }

    public void addMessage(final String user, final String message) {
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
