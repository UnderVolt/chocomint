package io.undervolt.mod;

import io.undervolt.api.event.events.RenderGameOverlayEvent;
import io.undervolt.api.event.handler.EventHandler;
import io.undervolt.api.event.handler.Listener;
import io.undervolt.utils.config.Configurable;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderMod extends Mod implements Listener {

    public final transient String name;
    public int x = 0, y = 0;
    public float scale = 1.0F;
    public int color1 = Color.WHITE.getRGB(), color2 = Integer.MIN_VALUE;

    public RenderMod(String name) {
        super(name);
        this.name = name;
    }

    public void render() {}
}
