package io.undervolt.client.gui.notifications;

import com.google.common.collect.Lists;

import java.util.List;

public class NotificationManager {

    private final List<Notification> notificationList = Lists.newArrayList();

    public final List<Notification> getNotifications() {
        return this.notificationList;
    }

    public List<Notification> addNotification(Notification notification) {
        this.notificationList.add(notification);
        return this.notificationList;
    }

    public List<Notification> deleteNotification(Notification notification) {
        if(this.notificationList.contains(notification))
            this.notificationList.remove(notification);
        return this.notificationList;
    }
}
