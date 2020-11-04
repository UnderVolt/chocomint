package io.undervolt.client.gui.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.List;
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

    public void drawPanel() {
        drawRect( this.mc.displayWidth - 100, 24,
                this.mc.displayWidth, this.mc.displayHeight, new Color(34, 34, 34, 183).getRGB());
        drawString(this.mc.fontRendererObj, "Notificaciones", this.mc.displayWidth - 95,
                29, Color.WHITE.getRGB());
        AtomicInteger x = new AtomicInteger();
        this.notificationManager.getNotifications().forEach(notification -> {
            drawRect(this.mc.displayWidth - 95, 26 + x.get(),
                    this.mc.displayWidth - 5, 50 + x.get(), Color.WHITE.getRGB());
            x.set(x.get() + 50);
        });
    }

    public boolean isActive() {
        return isActive;
    }

    public void toggleActive() {
        this.isActive = !this.isActive;
    }
}
