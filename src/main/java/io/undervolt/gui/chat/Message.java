package io.undervolt.gui.chat;

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

    public ContentType getMessageType() {
        if(this.getMessage().toLowerCase().contains("http://") || this.getMessage().toLowerCase().contains("https://")) {
            return ContentType.LINK;
        }else {
            return ContentType.PLAINTEXT;
        }
    }

    public enum ContentType {
        PLAINTEXT, LINK, COMMAND
    }
}
