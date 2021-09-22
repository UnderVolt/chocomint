package io.undervolt.api.event.events;

import io.undervolt.api.event.event.Event;
import net.minecraft.client.gui.GuiScreen;

public class ScreenChangeEvent extends Event {
    private final GuiScreen screen;

    public ScreenChangeEvent(final GuiScreen screen) {
        this.screen = screen;
    }

    public GuiScreen getScreen() {
        return screen;
    }
}
