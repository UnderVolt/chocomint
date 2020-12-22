package io.undervolt.utils.config;

import io.undervolt.api.event.handler.Listener;
import net.minecraft.client.gui.Gui;

public class Configurable extends Gui implements Listener {
    private final transient String name;

    public Configurable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
