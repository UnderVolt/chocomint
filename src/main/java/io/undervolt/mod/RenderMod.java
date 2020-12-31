package io.undervolt.mod;

import io.undervolt.api.event.handler.Listener;

import java.awt.*;

public class RenderMod extends Mod implements Listener {

    public final transient String name;
    public transient int width, height;
    public int x = 0, y = 0;
    public float scale = 1.0F;
    public int color1 = Color.WHITE.getRGB(), color2 = Integer.MIN_VALUE;

    public RenderMod(String name, int width, int height) {
        super(name);
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public void render() {}
}
