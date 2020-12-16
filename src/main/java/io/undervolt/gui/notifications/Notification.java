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
        mc.getChocomint().getRenderUtils().drawRoundedRect(x, y, 110, 35, 3, Color.WHITE.getRGB());
        mc.getChocomint().getRenderUtils().drawFilledCircle(x + 12, y + 15, 8, this.getPriorityColor());

        String title = this.title;
        if(mc.fontRendererObj.getStringWidth(title) > 85) {
            title = title.substring(0, Math.min(title.length(), 14)) + "...";
        }

        mc.fontRendererObj.drawString(title, x + 24, y + 4, Color.BLACK.getRGB());

        if(mc.fontRendererObj.getStringWidth(this.description) > 70 ) {
            String splitSeq = this.description.contains(" ") ? " " : "(?!^)";
            String firstLine = "";
            String secondLine = "";

            for (String msgSplit : this.description.split(splitSeq)){
                if(mc.fontRendererObj.getStringWidth(firstLine + msgSplit) < 70) {
                    firstLine = firstLine + msgSplit + splitSeq.replace("(?!^)", "");
                } else {
                    secondLine = secondLine + msgSplit + splitSeq.replace("(?!^)", "");
                }
            }

            if(mc.fontRendererObj.getStringWidth(secondLine) > 65) {
                secondLine = secondLine.substring(0, Math.min(secondLine.length(), 14)) + "...";
            }

            mc.fontRendererObj.drawString(firstLine, x + 24, y + 15, Color.GRAY.getRGB());
            mc.fontRendererObj.drawString(secondLine, x + 24, y + 23, Color.GRAY.getRGB());
        } else {
            mc.fontRendererObj.drawString(this.description, x + 24, y + 15, Color.GRAY.getRGB());
        }
    }

}
