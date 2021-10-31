package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.Screen;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;

public class Image extends Drawable {
    protected DynamicTexture dynamicTexture;
    protected BufferedImage bufferedImage;
    protected ResourceLocation resourceLocation;

    protected int imageWidth, imageHeight;

    public Image(DynamicTexture dynamicTexture) {
        this.dynamicTexture = dynamicTexture;
    }

    public Image(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public Image(int width, int height, ResourceLocation resourceLocation)
    {
        this.resourceLocation = resourceLocation;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor3f(1,1, 1);
        if(this.dynamicTexture != null) {
            this.mc.getTextureManager().bindTexture(
                    this.mc.getTextureManager().getDynamicTextureLocation("pfp1", dynamicTexture));
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, (int)width, (int)height, imageWidth, imageHeight);
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor3f(1,1, 1);
        if(this.resourceLocation != null) {
            this.mc.getTextureManager().bindTexture(resourceLocation);
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, (int)width, (int)height, imageWidth, imageHeight);
        }
        super.draw(ui, x, y, mouseX, mouseY, deltaTime);
    }

    public Image setWidth(int width) {
        this.width = width;
        this.imageWidth = width;
        return this;
    }

    public Image setHeight(int height) {
        this.height = height;
        this.imageHeight = height;
        return this;
    }

    public Image setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
        return this;
    }

    public Image setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
        return this;
    }

    public Image setDynamicTexture(DynamicTexture dynamicTexture) {
        this.dynamicTexture = dynamicTexture;
        return this;
    }
}
