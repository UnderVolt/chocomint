package io.undervolt.bridge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;

public class GameBridge {
    
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static FontRenderer getFontRenderer() {
        return GameBridge.getMinecraft().fontRendererObj;
    }

    public static TextureManager getTextureManager() {
        return GameBridge.getMinecraft().getTextureManager();
    }

    public static RenderManager getRenderManager() {
        return GameBridge.getMinecraft().getRenderManager();
    }

    public static ScaledResolution getScaledResolution() {
        return new ScaledResolution(GameBridge.getMinecraft());
    }
}
