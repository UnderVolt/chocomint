package io.undervolt.gui.clickable;

import io.undervolt.bridge.GameBridge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;
import java.util.function.Consumer;

public class ClickableLabel extends Clickable {

    private final Minecraft mc;
    private final FontRenderer fontRenderer;
    private String text;
    private int color;

    public ClickableLabel(int x, int y, String text, Consumer consumer) {
        this(x, y, text, new Color(200, 200, 200).getRGB(), consumer);
    }

    public ClickableLabel(int x, int y, String text, int color, Consumer consumer) {
        super(x, y, 1, 1, consumer);
        this.mc = GameBridge.getMinecraft();
        this.fontRenderer = this.mc.fontRendererObj;
        this.text = text;
        this.color = color;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        boolean isOverButton = (mouseX >= this.x) && (mouseX <= this.x + this.width) && (mouseY >= this.y) && (
                mouseY <= this.y + this.height);

        this.width = this.fontRenderer.getStringWidth(text);
        this.height = this.fontRenderer.FONT_HEIGHT;
        this.fontRenderer.drawString(text, x, y,
                isOverButton ? new Color(255, 218, 108).getRGB() : color);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
