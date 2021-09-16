package io.undervolt.api.event.events;

import io.undervolt.api.event.event.Event;

public class KeyInputEvent extends Event {
    private final int key;

    public KeyInputEvent(final int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
