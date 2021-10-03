package io.undervolt.gui;

import io.undervolt.bridge.GameBridge;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class FloatingLabel extends Gui {

    private final Chocomint chocomint;
    private final Minecraft mc;
    private final FontRenderer fontRenderer;

    private String text;
    private boolean enabled;

    public FloatingLabel(String text) {
        this.text = text;
        this.enabled = true;
        this.chocomint = GameBridge.getChocomint();
        this.mc = GameBridge.getMinecraft();
        this.fontRenderer = this.mc.fontRendererObj;
    }

    public void drawLabel(int mouseX, int mouseY) {
        int width = 12 + this.fontRenderer.getStringWidth(text);
        int height = 17;
        int x = (mouseX + width > GameBridge.getScaledResolution().getScaledWidth()) ? mouseX - width : mouseX;
        int y = (mouseY < width) ? mouseY : mouseY - 17;
        if (enabled) {
            this.chocomint.getRenderUtils().drawRoundedRect(x, y, width, height, 3,
                    new Color(58, 58, 58, 200).getRGB());
            GL11.glColor3f(255, 255, 255);
            this.fontRenderer.drawString(text, x + 6, y + 5, Color.WHITE.getRGB());
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
