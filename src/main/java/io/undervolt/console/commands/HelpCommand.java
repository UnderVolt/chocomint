package io.undervolt.console.commands;

import io.undervolt.console.Command;
import io.undervolt.gui.chat.Tab;
import io.undervolt.instance.Chocomint;

public class HelpCommand extends Command {
    public HelpCommand(final Chocomint chocomint) {
        super(chocomint, "Help", "help", "Displays all the available commands");
    }

    @Override
    public void execute(Tab tab, String[] args) {
        this.getChocomint().getConsole().getCommandList().forEach(command ->
                tab.addMessage(this.getChocomint().getChocomintUser(),
                        "/" + command.getText() + ": " + command.getDescription())
        );
    }
}
