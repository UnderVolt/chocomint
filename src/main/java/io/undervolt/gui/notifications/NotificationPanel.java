package io.undervolt.gui.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationPanel extends Gui {

    private final Minecraft mc;
    private boolean isActive;
    private boolean read;

    private final NotificationManager notificationManager;

    public NotificationPanel(final Minecraft mc, boolean isActive, final NotificationManager notificationManager) {
        this.mc = mc;
        this.isActive = isActive;
        this.notificationManager = notificationManager;
    }

    public void drawPanel(int screenWidth, int screenHeight, int scroll) {
        if(this.isActive) {
            drawRect(screenWidth - 120, 20,
                    screenWidth, screenHeight, new Color(34, 34, 34, 183).getRGB());
            drawString(this.mc.fontRendererObj, "Notificaciones", screenWidth - 115,
                    29, Color.WHITE.getRGB());

            GL11.glPushMatrix();
            GlStateManager.translate(0, scroll, 0);

            AtomicInteger x = new AtomicInteger();
            this.notificationManager.getNotifications().forEach(notification -> {
                notification.draw(this.mc, screenWidth - 115, 45 + x.get());
                x.set(x.get() + 45);
            });

            GL11.glPopMatrix();
        } else {
            if (!this.read || this.notificationManager.getNotifications().size() > 0) {
                //TODO: Modify icon with "unread notification"
            }
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void toggleActive() {
        this.isActive = !this.isActive;
        this.setRead();
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setRead() {
        this.read = true;
    }
}
