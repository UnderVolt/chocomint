package io.undervolt.gui.notifications;

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

    public void draw(final FontRenderer fontRenderer, int x, int y) {
        drawRect(x, y, x + 110, y + 20, Color.WHITE.getRGB());
        fontRenderer.drawString(this.title, x + 2, y + 2, Color.BLACK.getRGB());
    }

}
