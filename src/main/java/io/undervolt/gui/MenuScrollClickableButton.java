package io.undervolt.gui;

import io.undervolt.gui.GameBarButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

public class MenuScrollClickableElement {

    private ResourceLocation texture;
    private final Consumer consumer;

    public MenuScrollClickableElement(Consumer consumer, int x, int y, int w, int h, int color, final String textureName) {
        this.texture = new ResourceLocation("/chocomint/icon/" + textureName + ".png");
    }

    public MenuScrollClickableElement(Consumer consumer, int x, int y, int w, int h, int color, final ResourceLocation resourceLocation) {
        this.texture = resourceLocation;
    }

    public void draw(int mouseX, int mouseY, int ) {
        mc.getTextureManager().bindTexture(this.texture);

        GL11.glColor3f(255,255,255);
        Gui.drawModalRectWithCustomSizedTexture(this.xPosition+2,this.yPosition+2,0,0,this.width-4, this.height-4, this.width-4, this.height-4);
    }

    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
    }
}
