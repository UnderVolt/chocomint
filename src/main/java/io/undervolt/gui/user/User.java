package io.undervolt.gui.user;

import java.awt.*;

public class User {

    public enum Status {
        ONLINE, AWAY, BUSY, OFFLINE;
    }

    private final String username;
    private final Status status;
    private final String countryCode;
    private final boolean developer;
    private final String image;

    public User(final String username, final Status status, String countryCode, boolean developer, String image) {
        this.status = status;
        this.username = username;
        this.countryCode = countryCode;
        this.developer = developer;
        this.image = image;
    }

    public final String getUsername() {
        return this.username;
    }

    public final Status getStatus() {
        return this.status;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public boolean isDeveloper() {
        return developer;
    }

    public String getImage() {
        return image;
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
