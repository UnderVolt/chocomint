package io.undervolt.gui.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationPanel extends Gui {

    private final Minecraft mc;
    private boolean isActive;

    private final NotificationManager notificationManager;

    public NotificationPanel(final Minecraft mc, boolean isActive, final NotificationManager notificationManager) {
        this.mc = mc;
        this.isActive = isActive;
        this.notificationManager = notificationManager;
    }

    public void drawPanel(int screenWidth, int screenHeight) {
        if(this.isActive) {
            drawRect(screenWidth - 120, 20,
                    screenWidth, screenHeight, new Color(34, 34, 34, 183).getRGB());
            drawString(this.mc.fontRendererObj, "Notificaciones", screenWidth - 115,
                    29, Color.WHITE.getRGB());
            AtomicInteger x = new AtomicInteger();
            this.notificationManager.getNotifications().forEach(notification -> {
                notification.draw(this.mc.fontRendererObj, screenWidth - 115, 40 + x.get());
                x.set(x.get() + 28);
            });
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void toggleActive() {
        this.isActive = !this.isActive;
    }


    public void setActive(boolean active) {
        isActive = active;
    }
}
