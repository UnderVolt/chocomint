package io.undervolt.console;

import com.google.common.collect.Lists;
import io.undervolt.gui.chat.Tab;
import io.undervolt.instance.Chocomint;

import java.util.List;

public class Console {

    private final List<Command> commandList = Lists.newArrayList();
    private final Chocomint chocomint;

    public Console(final Chocomint chocomint) {
        this.chocomint = chocomint;
    }

    public void registerCommand(final Command command) {
        if(!commandList.contains(command))
            this.commandList.add(command);
    }

    public void processCommand(final Tab debugTab, final String text) {
        final String[] split = text.split(" ");
        this.commandList.forEach(command -> {
            if(text.equalsIgnoreCase("/" + command.getText())) {
                command.execute(debugTab, split);
            }
        });
    }

    public List<Command> getCommandList() {
        return commandList;
    }
}
