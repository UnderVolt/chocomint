package io.undervolt.gui.notifications;

import com.google.common.collect.Lists;
import io.undervolt.api.event.EventManager;
import io.undervolt.api.event.events.NotificationEvent;
import io.undervolt.instance.Chocomint;

import java.util.List;

public class NotificationManager {

    private List<Notification> notificationList = Lists.newArrayList();
    private final EventManager eventManager;

    public NotificationManager(Chocomint chocomint) {
        this.eventManager = chocomint.getEventManager();
    }

    public void updateNotifications(final List<Notification> notifications) {
        this.clearNotifications();
        this.notificationList = notifications;
    }

    public final List<Notification> getNotifications() {
        return this.notificationList;
    }

    public List<Notification> addNotification(Notification notification) {
        this.notificationList.add(notification);
        this.eventManager.callEvent(new NotificationEvent.Add(notification));
        return this.notificationList;
    }

    public List<Notification> deleteNotification(Notification notification) {
        this.notificationList.remove(notification);
        this.eventManager.callEvent(new NotificationEvent.Remove(notification));
        return this.notificationList;
    }

    public List<Notification> clearNotifications() {
        this.notificationList.clear();
        this.eventManager.callEvent(new NotificationEvent.Clear());
        return this.notificationList;
    }
}
