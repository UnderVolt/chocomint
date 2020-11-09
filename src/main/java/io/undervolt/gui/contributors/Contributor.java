package io.undervolt.gui.contributors;


public class Contributor {
    private final String login, avatar_url;
    private final int contributions;

    public Contributor(final String name, final String avatarURL, final int contributions) {
        this.login = name;
        this.avatar_url = avatarURL;
        this.contributions = contributions;
    }

    public String getName() {
        return login;
    }

    public String getAvatarURL() {
        return avatar_url;
    }

    public int getContributions() {
        return contributions;
    }
}
