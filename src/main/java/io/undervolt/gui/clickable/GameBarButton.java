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
    private boolean enabled = true;
    private String buttonText;

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
        if(enabled) {
            if (isOverButton)
            {
                drawRect(this.x, this.y, this.x + this.width,
                        this.y + this.height, new Color(54,57,63).getRGB());
            }
            else
            {
                drawRect(this.x, this.y, this.x + this.width,
                        this.y + this.height, new Color(32,34,37).getRGB());
            }
            drawCenteredString(mc.fontRendererObj, this.buttonText, this.x + (this.width / 2), this.y + (this.height / 3), Color.WHITE.getRGB());
        } else {
            drawRect(this.x, this.y, this.x + this.width,
                    this.y + this.height, new Color(41,43,47).getRGB());
            GL11.glColor3f(255, 255, 255);
            drawCenteredString(mc.fontRendererObj, this.buttonText, this.x + (this.width / 2), this.y + (this.height / 3), Color.LIGHT_GRAY.getRGB());
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }
}
