package io.undervolt.api.event.events;

import io.undervolt.api.event.event.Event;

public class InitEvent extends Event {
    public static class PreInitEvent extends Event {}
    public static class ClientInitEvent extends Event {}
    public static class PostInitEvent extends Event {}
}
