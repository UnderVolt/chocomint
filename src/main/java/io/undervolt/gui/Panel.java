package io.undervolt.gui;

import io.undervolt.bridge.GameBridge;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.AnimationUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class Panel extends AnimationUI {

    public final GuiScreen previousScreen;
    private final String name;
    protected final Chocomint chocomint;

    private boolean backwards = false;
    private double tw = Integer.MAX_VALUE;

    protected long ftime;

    protected int scroll = 0;
    protected int pageSize;

    public Panel(GuiScreen previousScreen, String name, int pageSize) {
        this.previousScreen = previousScreen;
        this.name = name;
        this.pageSize = pageSize;
        this.chocomint = GameBridge.getChocomint();
    }

    @Override
    public void initGui() {
        super.initGui();
        previousScreen.width = width;
        previousScreen.height = height;
        this.ftime = Minecraft.getSystemTime();
        this.chocomint.getGameBar().init(width, height);
    }

    public void fadeOut() {
        this.backwards = true;
        this.ftime = Minecraft.getSystemTime();
        this.tw = 0.1;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        int hue = 0;
        int tilt = 0;

        if(tw != 0) {
            tw = this.getAnimationTime(this.ftime, 3500.0D) * getPanelWidth();
        } else {
            if(backwards) this.mc.displayGuiScreen(this.previousScreen);
        }

        if(!backwards) {
            hue = 130 - (int)(this.getAnimationTime(this.ftime, 3500.0D) * 130);
            tilt = 30 - (int)(this.getAnimationTime(this.ftime, 3500.0D) * 30);
        } else {
            hue = (int)(this.getAnimationTime(this.ftime, 3500.0D) * 130);
            tilt = (int)(this.getAnimationTime(this.ftime, 3500.0D) * 30);
        }

        GL11.glPushMatrix();
        GlStateManager.translate(-tilt, 0, 0);
        if(this.mc.theWorld == null && this.mc.thePlayer == null)
            previousScreen.drawScreen(0, 0, partialTicks);
        GL11.glPopMatrix();

        drawRect(0, 0, width, height, new Color(0, 0, 0, hue).getRGB());

        GL11.glPushMatrix();
        GlStateManager.translate(backwards ? getPanelWidth() - tw : tw, 0, 0);

        GL11.glPushMatrix();
        GlStateManager.translate(this.width - this.getPanelWidth(), 20, 0);

        GL11.glPushMatrix();
        GlStateManager.translate(0, 0, 0);
        GL11.glRotatef(90, 0, 0, 1);
        drawGradientRect(0, 0, height, 10, new Color(0, 0, 0, 100).getRGB(), 0);
        GL11.glPopMatrix();

        drawRect(0, 0, this.width, this.height, new Color(22, 24, 26).getRGB());
        this.mc.fontRendererObj.drawString(this.name, 5, 5, Color.WHITE.getRGB());

        GL11.glPushMatrix();
        GlStateManager.translate(0, 20, 0);
        drawContent(mouseX, mouseY, partialTicks, getPanelWidth(), scroll);
        GL11.glPopMatrix();

        GL11.glPopMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glPopMatrix();

        this.chocomint.getGameBar().draw(mouseX, mouseY, partialTicks, width, height);
    }

    public void drawContent(int mouseX, int mouseY, float partialTicks, int margin, int scroll) {}

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int i = Mouse.getEventDWheel();

        if (i > 0 && (this.scroll <= 0)) this.scroll += 8;
        else if (i < 0 && !(pageSize <= (height - 45) - this.scroll))
            this.scroll -= 8;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.chocomint.getGameBar().mouseClicked(mouseX, mouseY, mouseButton, width, height);
        if(mouseX < this.width - this.getPanelWidth()) this.fadeOut();
    }

    protected int getPanelWidth() {
        if(this.width / 3 < 250) {
            return this.width / 3;
        } else {
            return 250;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 1) this.fadeOut();
    }
}
