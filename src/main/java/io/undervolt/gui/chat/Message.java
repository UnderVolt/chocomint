package io.undervolt.gui.chat;

import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.user.UserScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message extends Gui {
    private final LocalDateTime now = LocalDateTime.now();
    private final String user, message;
    private final Minecraft mc;
    private final DateTimeFormatter dtf;
    private int y, font_size;

    public Message(final String user, final String message) {
        this.message = message;
        this.user = user;
        this.mc = GameBridge.getMinecraft();
        this.dtf = GameBridge.getHourFormatter();
        this.font_size = mc.fontRendererObj.FONT_HEIGHT;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public ContentType getMessageType() {
        if(this.getMessage().toLowerCase().contains("http://") || this.getMessage().toLowerCase().contains("https://")) {
            return ContentType.LINK;
        } else {
            return ContentType.PLAINTEXT;
        }
    }

    public void click(final Chat chat, int mouseX, int mouseY) {
        if(mouseY > y && mouseY < y + font_size) {
            if(this.user != null) {
                if (mouseX > 35 && mouseX < (35 + this.mc.fontRendererObj.getStringWidth(this.getUser()))) {
                    this.mc.displayGuiScreen(new UserScreen(chat, GameBridge.getChocomint(),
                            this.user.startsWith("\247") ? this.user.substring(2) : this.user
                    ));
                }
            }
        }
    }

    public void drawMessage(int y, int chatHeight, int mouseX, int mouseY) {
        this.y = y;
        int x = (35 + this.mc.fontRendererObj.getStringWidth(this.getUser()));
        boolean mouseOver = (mouseY > y && mouseY < y + font_size && mouseX > 35 && mouseX < (35 + this.mc.fontRendererObj.getStringWidth(this.getUser())));
        if(y > chatHeight) {
            this.mc.fontRendererObj.drawString(this.dtf.format(now), 5, y, Color.GRAY.getRGB());
            if(this.getUser() != null) {
                this.mc.fontRendererObj.drawStringWithShadow(this.getUser(), 35, y, mouseOver ? new Color(255, 218, 108).getRGB() : Color.WHITE.getRGB());
                this.mc.fontRendererObj.drawStringWithShadow("\247f: " + this.getMessage(), x, y, Color.WHITE.getRGB());
            } else this.mc.fontRendererObj.drawStringWithShadow("\247f" + this.getMessage(), 35, y, Color.WHITE.getRGB());
        }
    }

    public enum ContentType {
        PLAINTEXT, LINK, COMMAND
    }
}
