package io.undervolt.gui;

import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.clickable.Clickable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

public class MenuScrollClickableButton extends Clickable {

    private ResourceLocation texture;
    private int c, hc;
    private final Minecraft mc;
    private boolean enabled = true;

    public MenuScrollClickableButton(final String textureName, Consumer consumer, int x, int y, int w, int h, int color, int hc) {
        this(new ResourceLocation("/chocomint/icon/" + textureName + ".png"), consumer, x, y, w, h, color, hc);
    }

    public MenuScrollClickableButton(final ResourceLocation resourceLocation, Consumer consumer, int x, int y, int w, int h, int color, int hc) {
        super(x, y, w, h, consumer);
        this.texture = resourceLocation;
        this.mc = GameBridge.getMinecraft();
        this.c = color;
        this.hc = hc;
    }

    public void draw(int mouseX, int mouseY) {
        this.mc.getChocomint().getRenderUtils().drawRoundedRect(this.x, this.y, this.width, this.height,
                2, this.isOverButton(mouseX, mouseY) || !enabled ? hc : c);
        GL11.glColor3f(255,255,255);
        mc.getTextureManager().bindTexture(this.texture);
        Gui.drawModalRectWithCustomSizedTexture(x+2,y+2,0,0,this.width - 4, this.height - 4,
                this.width - 4, this.height - 4);
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        if(enabled)
            super.click(mouseX, mouseY, mouseButton);
    }

    public boolean isOverButton(int mouseX, int mouseY) {
        return (mouseX > this.x && mouseY > this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
    }

    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
    }
    public void setTexture(String textureName) {
        this.setTexture(new ResourceLocation("/chocomint/icon/" + textureName + ".png"));
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
