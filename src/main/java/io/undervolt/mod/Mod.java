package io.undervolt.mod;

import io.undervolt.api.event.handler.Listener;
import io.undervolt.utils.config.Configurable;

public class Mod extends Configurable implements Listener {
    private final transient String name;
    public boolean enabled = true;

    public Mod(String name) {
        super(name);
        this.name = name;
    }
}
