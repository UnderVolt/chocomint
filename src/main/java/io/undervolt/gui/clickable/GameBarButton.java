package io.undervolt.gui.clickable;

import io.undervolt.bridge.GameBridge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.function.Consumer;

public class GameBarButton extends Clickable {

    protected final ScaledResolution sr;
    protected final Minecraft mc;
    protected boolean enabled = true;
    protected String buttonText;

    public GameBarButton(int x, int y, int width, int height, String buttonText, Consumer consumer) {
        super(x, y, width, height, consumer);
        this.buttonText = buttonText;
        this.sr = GameBridge.getScaledResolution();
        this.mc = GameBridge.getMinecraft();
    }

    @Override
    public void draw(int mouseX, int mouseY) {

        boolean isOverButton = (mouseX >= this.x) && (mouseX <= this.x + this.width) && (mouseY >= this.y) && (
                mouseY <= this.y + this.height);

        drawRect(this.x, this.y, this.x + this.width,
                    this.y + this.height, new Color(32,34,37).getRGB());
        if(enabled) {
            if (isOverButton)
                drawRect(this.x, this.y, this.x + this.width,
                    this.y + this.height, new Color(54,57,63).getRGB());
            drawCenteredString(mc.fontRendererObj, this.buttonText, this.x + (this.width / 2), this.y + (this.height / 3), Color.WHITE.getRGB());
        } else {
            drawCenteredString(mc.fontRendererObj, this.buttonText, this.x + (this.width / 2), this.y + (this.height / 3), Color.LIGHT_GRAY.getRGB());
        }
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        if(enabled)
            super.click(mouseX, mouseY, mouseButton);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }
}
