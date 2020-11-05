package io.undervolt.gui.user;

import java.awt.*;

public class User {

    public enum Status {
        ONLINE, AWAY, BUSY, OFFLINE;
    }

    private final String username;
    private final Status status;

    public User(final String username, final Status status) {
        this.status = status;
        this.username = username;
    }

    public final String getUsername() {
        return this.username;
    }

    public final Status getStatus() {
        return this.status;
    }

    public String getStatusString() {
        switch(this.status) {
            case OFFLINE:
                return "Fuera de línea";
            case BUSY:
                return "Ocupado";
            case AWAY:
                return "Ausente";
            default:
                return "En línea";
        }
    }

    public int getStatusColor() {
        switch(this.status) {
            case OFFLINE:
                return new Color(77, 77, 77).getRGB();
            case BUSY:
                return new Color(255, 62, 62).getRGB();
            case AWAY:
                return new Color(255, 183, 62).getRGB();
            default:
                return new Color(83, 255, 62).getRGB();
        }
    }
}
