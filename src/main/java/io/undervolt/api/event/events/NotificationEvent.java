package io.undervolt.api.event.events;

import io.undervolt.api.event.event.Event;
import io.undervolt.gui.notifications.Notification;

public class NotificationEvent {
    public static class Add extends Event {
        public final Notification notification;

        public Add(Notification notification) {
            this.notification = notification;
        }
    }
    public static class Remove extends Event {
        public final Notification notification;

        public Remove(Notification notification) {
            this.notification = notification;
        }
    }
    public static class Clear extends Event {}
}
