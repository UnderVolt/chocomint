package io.undervolt.gui.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class MenuInterfaceButton extends GuiButton {
    private final int x, y, widthIn, heightIn;
    public String buttonText;

    public MenuInterfaceButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.x = x;
        this.y = y;
        this.widthIn = widthIn;
        this.heightIn = heightIn;
        this.buttonText = buttonText;
        this.enabled = true;
    }


    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        boolean isOverButton = (mouseX >= this.x) && (mouseX <= this.x + this.widthIn) && (mouseY >= this.y) && (
                mouseY <= this.y + this.heightIn);
        if(enabled) {
            if (isOverButton)
            {
                drawRect(this.x, this.y, this.x + this.widthIn,
                        this.y + this.heightIn, new Color(67, 74, 82).getRGB());
            }
            else
            {
                drawRect(this.x, this.y, this.x + this.widthIn,
                        this.y + this.heightIn, new Color(54,57,63).getRGB());
            }
            drawCenteredString(mc.fontRendererObj, this.buttonText, this.x + (this.widthIn / 2), this.y + (this.heightIn / 3), Color.WHITE.getRGB());
        } else {
            drawRect(this.x, this.y, this.x + this.widthIn,
                    this.y + this.heightIn, new Color(41,43,47).getRGB());
            GL11.glColor3f(255, 255, 255);
            drawCenteredString(mc.fontRendererObj, this.buttonText, this.x + (this.widthIn / 2), this.y + (this.heightIn / 3), Color.LIGHT_GRAY.getRGB());
        }

    }
}


