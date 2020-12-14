package io.undervolt.gui.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TexturedMenuInterfaceButton extends MenuInterfaceButton {

    private final ResourceLocation texture;

    public TexturedMenuInterfaceButton(int buttonId, int x, int y, int w, int h, final String textureName) {
        super(buttonId, x, y, w, h,"");
        this.texture = new ResourceLocation("/chocomint/icon/" + textureName + ".png");
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        super.drawButton(mc, mouseX, mouseY);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
        GL11.glColor3f(255,255,255);
        Gui.drawModalRectWithCustomSizedTexture(this.xPosition+2,this.yPosition+2,0,0,this.width-4, this.height-4, this.width-4, this.height-4);
    }
}
