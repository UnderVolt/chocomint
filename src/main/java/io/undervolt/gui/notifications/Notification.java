package io.undervolt.gui.notifications;

import io.undervolt.bridge.GameBridge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.function.Consumer;

public class Notification extends Gui {

    public enum Priority {
        NOTICE, SOCIAL, WARNING, ALERT, CRITICAL
    }

    public final Priority priority;
    public final String title, description;
    private final Consumer<GuiScreen> consumer;
    private final Minecraft mc;

    private int width = 120;

    private int x, y;

    public Notification(final Priority priority, final String title, final String description, Consumer<GuiScreen> consumer) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.consumer = consumer;
        this.mc = GameBridge.getMinecraft();
    }

    public int getPriorityColor() {
        switch(this.priority) {
            case ALERT:
                return new Color(255, 80, 0).getRGB();
            case NOTICE:
                return new Color(1, 247, 33).getRGB();
            case SOCIAL:
                return new Color(1, 99, 247).getRGB();
            case WARNING:
                return new Color(255, 216, 0).getRGB();
            case CRITICAL:
                return new Color(255, 71, 71).getRGB();
            default:
                return Color.GRAY.getRGB();
        }
    }

    public void draw(final Minecraft mc, int x, int y) {
        this.draw(mc, x, y, 120, -50, -50);
    }

    public void draw(final Minecraft mc, int x, int y, int mouseX, int mouseY) {
        this.draw(mc, x, y, 120, mouseX, mouseY);
    }

    public void draw(final Minecraft mc, int x, int y, int width, int mouseX, int mouseY) {

        this.x = x;
        this.y = y;
        this.width = width;

        mc.getChocomint().getRenderUtils().drawRoundedRect(x, y, width, 40, 3, new Color(32, 34, 36).getRGB());
        mc.getChocomint().getRenderUtils().drawRoundedRect(x, y, x + 16, y + 40, 3, 0, 3, 0, new Color(28, 32, 34).getRGB());
        mc.getChocomint().getRenderUtils().drawFilledCircle(x + 8, y + 20, 2, this.getPriorityColor());

        String title = this.title;
        if(mc.fontRendererObj.getStringWidth(title) > width - 35) {
            title = title.substring(0, Math.min(title.length(), 14)) + "...";
        }

        mc.fontRendererObj.drawString(title, x + 23, y + 7, this.getPriorityColor());

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

            mc.fontRendererObj.drawString(firstLine.toString(), x + 23, y + 18, new Color(111, 112, 113).getRGB());
            mc.fontRendererObj.drawString(secondLine.toString(), x + 23, y + 26, new Color(111, 112, 113).getRGB());
        } else {
            mc.fontRendererObj.drawString(this.description, x + 23, y + 18, new Color(111, 112, 113).getRGB());
        }

        if(mouseX > this.x && mouseY > this.y && mouseX < this.x + this.width && mouseY < this.y + 35) {
            mc.getChocomint().getRenderUtils().drawFilledCircle(this.x + this.width - 10, this.y + 20, 5,
                    (mouseX > this.x + this.width - 15 && mouseY > this.y + 15 && mouseX < this.x + this.width - 5 && mouseY < this.y + 25)
                    ? new Color(255, 81, 81).getRGB() : new Color(28, 32, 34).getRGB());
            GL11.glPushMatrix();
            GL11.glTranslatef(this.x + this.width - 13, this.y + 16.5f, 0);
            mc.fontRendererObj.drawString("\247l✕", 0, 0,
                    (mouseX > this.x + this.width - 15 && mouseY > this.y + 15 && mouseX < this.x + this.width - 5 && mouseY < this.y + 25)
                    ? Color.white.getRGB() : Color.gray.getRGB());
            GL11.glPopMatrix();
        }
    }

    public void click(int mouseX, int mouseY, int mouseButton) {
        if(mouseX > this.x && mouseY > this.y && mouseX < this.x + this.width && mouseY < this.y + 35) {
            if(mouseX > this.x + this.width - 15 && mouseY > this.y + 15 && mouseX < this.x + this.width - 5 && mouseY < this.y + 25) {
                mc.getChocomint().getNotificationManager().scheduleNotificationDeletion(this);
            } else {
                this.consumer.accept(mc.currentScreen);
            }
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
