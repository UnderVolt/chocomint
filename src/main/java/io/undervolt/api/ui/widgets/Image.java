package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.UIView;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;

public class Image extends Drawable {
    protected DynamicTexture dynamicTexture;
    protected BufferedImage bufferedImage;
    protected ResourceLocation resourceLocation;

    public Image(DynamicTexture dynamicTexture) {
        this.dynamicTexture = dynamicTexture;
    }

    public Image(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    @Override
    public void draw(UIView ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor3f(1,1, 1);
        if(this.dynamicTexture != null) {
            this.mc.getTextureManager().bindTexture(
                    this.mc.getTextureManager().getDynamicTextureLocation("pfp1", dynamicTexture));
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, (int)width, (int)height, (int)width, (int)height);
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor3f(1,1, 1);
        if(this.resourceLocation != null) {
            this.mc.getTextureManager().bindTexture(resourceLocation);
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, (int)width, (int)height, (int)width, (int)height);
        }
    }

    public Image setWidth(int width) {
        this.width = width;
        return this;
    }

    public Image setHeight(int height) {
        this.height = height;
        return this;
    }

    public Image setDynamicTexture(DynamicTexture dynamicTexture) {
        this.dynamicTexture = dynamicTexture;
        return this;
    }
}
