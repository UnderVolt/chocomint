package io.undervolt.gui.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class Notification extends Gui {

    public enum Priority {
        NOTICE, SOCIAL, WARNING, ALERT, CRITICAL
    }

    public final Priority priority;
    public final String title, description;

    public Notification(final Priority priority, final String title, final String description) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public int getPriorityColor() {
        switch(this.priority) {
            case ALERT:
                return new Color(255, 80, 0).getRGB();
            case NOTICE:
                return Color.GREEN.getRGB();
            case SOCIAL:
                return Color.CYAN.getRGB();
            case WARNING:
                return Color.ORANGE.getRGB();
            case CRITICAL:
                return Color.RED.getRGB();
            default:
                return Color.GRAY.getRGB();
        }
    }

    public void draw(final Minecraft mc, int x, int y) {
        mc.getChocomint().getRenderUtils()
            .drawRoundedRect(x, y, 110, 30, 3, Color.WHITE.getRGB());
        mc.getChocomint().getRenderUtils()
            .drawFilledCircle(x + 12, y + 15, 8, this.getPriorityColor());
        mc.fontRendererObj.drawString(this.title, x + 24, y + 4, Color.BLACK.getRGB());
    }

}
