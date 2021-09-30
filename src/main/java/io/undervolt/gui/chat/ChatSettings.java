package io.undervolt.gui.chat;

import io.undervolt.utils.config.Configurable;

public class ChatSettings extends Configurable {

    public int chatHeight = 100;

    public ChatSettings() {
        super("Chat");
    }

    public int getChatHeight() {
        return chatHeight;
    }

    public void setChatHeight(int chatHeight) {
        this.chatHeight = chatHeight;
    }
}
