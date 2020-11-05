package io.undervolt.bridge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;

public class GameBridge {
    
    public Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public FontRenderer getFontRenderer() {
        return this.getMinecraft().fontRendererObj;
    }

    public TextureManager getTextureManager() {
        return this.getMinecraft().getTextureManager();
    }

    public RenderManager getRenderManager() {
        return this.getMinecraft().getRenderManager();
    }

    public ScaledResolution getScaledResolution() {
        return new ScaledResolution(this.getMinecraft());
    }
}
