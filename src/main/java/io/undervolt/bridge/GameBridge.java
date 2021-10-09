package io.undervolt.bridge;

import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;

import java.time.format.DateTimeFormatter;

public class GameBridge {

    public static Chocomint getChocomint() {
        return Minecraft.getMinecraft().getChocomint();
    }

    public static DateTimeFormatter getHourFormatter() {
        return DateTimeFormatter.ofPattern("HH:mm");
    }
    
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static FontRenderer getFontRenderer() {
        return getMinecraft().fontRendererObj;
    }

    public static TextureManager getTextureManager() {
        return getMinecraft().getTextureManager();
    }

    public static RenderManager getRenderManager() {
        return getMinecraft().getRenderManager();
    }

    public static ScaledResolution getScaledResolution() {
        return new ScaledResolution(getMinecraft());
    }
}
