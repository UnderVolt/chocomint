package io.undervolt.gui.notifications;

import com.google.common.collect.Lists;
import io.undervolt.api.event.EventManager;
import io.undervolt.api.event.events.NotificationEvent;
import io.undervolt.api.event.events.TickEvent;
import io.undervolt.api.event.handler.EventHandler;
import io.undervolt.api.event.handler.Listener;
import io.undervolt.instance.Chocomint;

import java.util.List;

public class NotificationManager implements Listener {

    private List<Notification> notificationList = Lists.newArrayList();
    private final List<Notification> notificationsToRemove = Lists.newArrayList();
    private final EventManager eventManager;
    private boolean read;

    public NotificationManager(Chocomint chocomint) {
        this.eventManager = chocomint.getEventManager();
    }

    @EventHandler public void tick(TickEvent.ClientTickEvent event) {
        if(!notificationsToRemove.isEmpty()) {
            this.deleteNotification(notificationsToRemove.get(0));
            this.notificationsToRemove.remove(0);
        }
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

    public void scheduleNotificationDeletion(Notification notification) {
        this.notificationsToRemove.add(notification);
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
