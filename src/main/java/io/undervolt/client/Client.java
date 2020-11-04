package io.undervolt.client;

import io.undervolt.client.gui.notifications.NotificationManager;
import net.minecraft.client.Minecraft;

public class Client {

    private final Minecraft mc;
    private final NotificationManager notificationManager;

    public Client(final Minecraft mc) {
        this.mc = mc;
        this.notificationManager = new NotificationManager();
    }

    public void startClient() {
        System.out.println("Iniciado cliente.");
    }


    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

}
