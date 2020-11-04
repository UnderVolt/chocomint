package io.undervolt.gui.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationPanel extends Gui {

    private final Minecraft mc;
    private boolean isActive;
    private NotificationManager notificationManager;

    public NotificationPanel(final Minecraft mc, boolean isActive, final NotificationManager notificationManager) {
        this.mc = mc;
        this.isActive = isActive;
        this.notificationManager = notificationManager;
    }

    public void drawPanel(int screenWidth, int screenHeight) {
        drawRect( screenWidth - 100, 24,
                screenWidth, screenHeight, new Color(34, 34, 34, 183).getRGB());
        drawString(this.mc.fontRendererObj, "Notificaciones", screenWidth - 95,
                29, Color.WHITE.getRGB());
        AtomicInteger x = new AtomicInteger();
        this.notificationManager.getNotifications().forEach(notification -> {
            drawRect(screenWidth - 95, 40 + x.get(),
                    screenWidth - 5, 60 + x.get(), Color.WHITE.getRGB());
            this.mc.fontRendererObj.drawString(notification.title, screenWidth - 93,
                    42 + x.get(), Color.BLACK.getRGB()
            );
            x.set(x.get() + 28);
        });
    }

    public boolean isActive() {
        return isActive;
    }

    public void toggleActive() {
        this.isActive = !this.isActive;
    }
}
