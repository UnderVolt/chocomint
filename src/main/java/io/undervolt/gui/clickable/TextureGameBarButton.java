package io.undervolt.gui.clickable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

public class TextureGameBarButton extends GameBarButton {

    private ResourceLocation texture;

    public TextureGameBarButton(int x, int y, int width, int height, String textureName, Consumer consumer) {
        super(x, y, width, height, "", consumer);
        this.texture = new ResourceLocation("/chocomint/icon/" + textureName + ".png");
    }

    public TextureGameBarButton(int x, int y, int width, int height, ResourceLocation texture, Consumer consumer) {
        super(x, y, width, height, "", consumer);
        this.texture = texture;
    }

    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        this.mc.getTextureManager().bindTexture(this.texture);
        drawModalRectWithCustomSizedTexture(this.x + 2,this.y + 2,0,0,this.width - 4,
                this.height - 4, this.width - 4, this.height - 4);
    }

    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
    }
}
