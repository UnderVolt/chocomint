package io.undervolt.gui;

import io.undervolt.bridge.GameBridge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

public class MenuScrollClickableButton {

    private ResourceLocation texture;
    private final Consumer consumer;
    private int w, h, c, hc;
    private int x = 100, y;
    private final Minecraft mc;
    private boolean enabled = true;

    public MenuScrollClickableButton(final String textureName, Consumer consumer, int w, int h, int color, int hc) {
        this(new ResourceLocation("/chocomint/icon/" + textureName + ".png"), consumer, w, h, color, hc);
    }

    public MenuScrollClickableButton(final ResourceLocation resourceLocation, Consumer consumer, int w, int h, int color, int hc) {
        this.texture = resourceLocation;
        this.mc = GameBridge.getMinecraft();
        this.consumer = consumer;
        this.w = w;
        this.h = h;
        this.c = color;
        this.hc = hc;
    }

    public void draw(int mouseX, int mouseY, int x, int y) {
        this.x = x; this.y = y;
        this.mc.getChocomint().getRenderUtils().drawRoundedRect(this.x, this.y, w, h, 2, this.isOverButton(mouseX, mouseY) || !enabled ? hc : c);
        GL11.glColor3f(255,255,255);
        mc.getTextureManager().bindTexture(this.texture);
        Gui.drawModalRectWithCustomSizedTexture(x+2,y+2,0,0,w-4, h-4, w-4, h-4);
    }

    public void registerClick(int mouseX, int mouseY) {
        if(this.isOverButton(mouseX, mouseY) && this.enabled) {
            this.consumer.accept(new Object());
        }
    }

    public boolean isOverButton(int mouseX, int mouseY) {
        return (mouseX > this.x && mouseY > this.y && mouseX < this.x + this.w && mouseY < this.y + this.h);
    }

    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
    }
    public void setTexture(String textureName) {
        this.setTexture(new ResourceLocation("/chocomint/icon/" + textureName + ".png"));
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setC(int c) {
        this.c = c;
    }

    public void setHc(int hc) {
        this.hc = hc;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int getC() {
        return c;
    }

    public int getHc() {
        return hc;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
