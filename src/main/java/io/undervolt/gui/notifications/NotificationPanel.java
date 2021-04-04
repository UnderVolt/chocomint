package io.undervolt.gui.notifications;

import io.undervolt.api.animation.AnimationRender;
import io.undervolt.api.animation.AnimationScreen;
import io.undervolt.api.animation.AnimationTimings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationPanel extends AnimationRender {

    private final Minecraft mc;
    private boolean isActive;
    private boolean read;

    private final NotificationManager notificationManager;

    public NotificationPanel(final Minecraft mc, boolean isActive, final NotificationManager notificationManager) {
        super(250, AnimationTimings.QUAD, false);
        this.mc = mc;
        this.isActive = isActive;
        this.notificationManager = notificationManager;
    }

    public void drawPanel(int screenWidth, int screenHeight, int scroll) {



        if(this.isActive) {
            super.render();

            GlStateManager.pushMatrix();
            float modifier =  (screenWidth - this.deltaTime * 120);
            GlStateManager.translate(modifier, 0, 0);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            drawRect(0, 20, 120, screenHeight, new Color(34, 34, 34, (int) (this.deltaTime * 183)).getRGB());
            drawString(this.mc.fontRendererObj, "Notificaciones", 5,
                    29, new Color(255, 255, 255, (int) (this.deltaTime * 255)).getRGB());

            if(this.deltaTime == 1){
                GL11.glPushMatrix();
                GlStateManager.translate(0, scroll, 0);

                AtomicInteger x = new AtomicInteger();
                this.notificationManager.getNotifications().forEach(notification -> {
                    notification.draw(this.mc, 5, 45 + x.get());
                    x.set(x.get() + 45);
                });
                GL11.glPopMatrix();
            }
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
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
