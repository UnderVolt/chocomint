package io.undervolt.gui.notifications;

import com.google.common.collect.Lists;

import java.util.List;

public class NotificationManager {

    private List<Notification> notificationList = Lists.newArrayList();

    public void updateNotifications(final List<Notification> notifications) {
        this.clearNotifications();
        this.notificationList = notifications;
    }

    public final List<Notification> getNotifications() {
        return this.notificationList;
    }

    public List<Notification> addNotification(Notification notification) {
        this.notificationList.add(notification);
        return this.notificationList;
    }

    public List<Notification> deleteNotification(Notification notification) {
        this.notificationList.remove(notification);
        return this.notificationList;
    }

    public List<Notification> clearNotifications() {
        this.notificationList.clear();
        return this.notificationList;
    }
}
