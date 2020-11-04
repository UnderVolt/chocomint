package io.undervolt.gui.notifications;

import io.netty.util.concurrent.Promise;

public class Notification {

    public enum Priority {
        NOTICE, SOCIAL, WARNING, ALERT, CRITICAL
    }

    public final Priority priority;
    public final String title, description;

    public Notification(final Priority priority, final String title, final String description) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }
}
