package io.undervolt.console;

import io.undervolt.gui.chat.Tab;
import io.undervolt.gui.user.User;
import io.undervolt.instance.Chocomint;

public class Command {

    private final String displayName, text, description;
    protected final Chocomint chocomint;
    private final String chocomintUser;

    public Command(final Chocomint chocomint, final String displayName, final String text, final String description) {
        this.chocomint = chocomint;
        this.displayName = displayName;
        this.text = text;
        this.description = description;
        this.chocomintUser = this.chocomint.getChocomintUser();
    }

    public void execute(final Tab tab, String[] args) { }

    public String getDisplayName() {
        return displayName;
    }

    public String getText() {
        return text;
    }

    public String getDescription() {
        return description;
    }

    protected Chocomint getChocomint() {
        return chocomint;
    }

    protected Tab getCurrentTab() {
        return chocomint.getChatManager().getSelectedTab();
    }

    protected void returnMessage(final String message) {
        this.getCurrentTab().addMessage(this.chocomintUser, message);
    }
}
