package io.undervolt.console.commands;

import io.undervolt.console.Command;
import io.undervolt.gui.chat.Tab;
import io.undervolt.instance.Chocomint;

public class VersionCommand extends Command {
    public VersionCommand(final Chocomint chocomint) {
        super(chocomint,"Version", "version", "Displays the current chocomint's version");
    }

    @Override
    public void execute(Tab tab, String[] args) {
        tab.addMessage(this.chocomint.getChocomintUser(), "Unknown");
    }
}
