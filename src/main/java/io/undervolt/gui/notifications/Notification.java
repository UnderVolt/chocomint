package io.undervolt.gui.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.function.Consumer;

public class Notification extends Gui {

    public enum Priority {
        NOTICE, SOCIAL, WARNING, ALERT, CRITICAL
    }

    public final Priority priority;
    public final String title, description;
    private final Consumer<GuiScreen> consumer;

    private int x, y;

    public Notification(final Priority priority, final String title, final String description, Consumer<GuiScreen> consumer) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.consumer = consumer;
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
        this.draw(mc, x, y, 120);
    }

    public void draw(final Minecraft mc, int x, int y, int width) {

        this.x = x;
        this.y = y;

        mc.getChocomint().getRenderUtils().drawRoundedRect(x, y, width, 35, 3, Color.WHITE.getRGB());
        mc.getChocomint().getRenderUtils().drawRoundedRect(x + 3, y + 3, 10, 29, 3, this.getPriorityColor());

        String title = this.title;
        if(mc.fontRendererObj.getStringWidth(title) > width - 35) {
            title = title.substring(0, Math.min(title.length(), 14)) + "...";
        }

        mc.fontRendererObj.drawString(title, x + 20, y + 4, Color.BLACK.getRGB());

        if(mc.fontRendererObj.getStringWidth(this.description) > width - 44 ) {
            String splitSeq = this.description.contains(" ") ? " " : "(?!^)";
            StringBuilder firstLine = new StringBuilder();
            StringBuilder secondLine = new StringBuilder();

            for (String msgSplit : this.description.split(splitSeq)){
                if(mc.fontRendererObj.getStringWidth(firstLine + msgSplit) < width - 40) {
                    firstLine.append(msgSplit).append(splitSeq.replace("(?!^)", ""));
                } else {
                    secondLine.append(msgSplit).append(splitSeq.replace("(?!^)", ""));
                }
            }

            if(mc.fontRendererObj.getStringWidth(secondLine.toString()) > width - 45) {
                secondLine = new StringBuilder(secondLine.substring(0, Math.min(secondLine.length(), 14)) + "...");
            }

            mc.fontRendererObj.drawString(firstLine.toString(), x + 20, y + 15, Color.GRAY.getRGB());
            mc.fontRendererObj.drawString(secondLine.toString(), x + 20, y + 23, Color.GRAY.getRGB());
        } else {
            mc.fontRendererObj.drawString(this.description, x + 20, y + 15, Color.GRAY.getRGB());
        }
    }

    public Consumer<GuiScreen> getConsumer() {
        return consumer;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
