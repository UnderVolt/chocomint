package io.undervolt.gui.clickable;

import io.undervolt.bridge.GameBridge;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.function.Consumer;

public class Button extends Clickable {

    public boolean enabled = true;
    private String displayString;

    private final Chocomint chocomint;
    private final Minecraft mc;

    public Button(int x, int y, int width, int height, String displayString, Consumer consumer) {
        super(x, y, width, height, consumer);
        this.chocomint = GameBridge.getChocomint();
        this.mc = GameBridge.getMinecraft();
        this.displayString = displayString;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        boolean isOver = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        Color color = new Color(32,34,37);

        if (!this.enabled) {
            color = new Color(41,43,47);
        } else if (isOver) {
            color = new Color(79, 82, 92);
        }

        this.chocomint.getRenderUtils().drawRoundedRect(this.x, this.y, this.width, this.height, 4, color.getRGB());
        this.drawCenteredString(this.mc.fontRendererObj, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, Color.white.getRGB());
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if(enabled)
            super.click(mouseX, mouseY);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setDisplayString(String displayString) {
        this.displayString = displayString;
    }
}
