package io.undervolt.api.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class AnimatedOverlay extends Overlay {

    protected int overlayWidth, overlayHeight;

    private double tw = Integer.MAX_VALUE;
    private long ftime;
    private boolean backwards = false;

    public AnimatedOverlay(GuiScreen parentScreen, OverlayPosition overlayPosition) {
        super(parentScreen, overlayPosition);
        this.ftime = Minecraft.getSystemTime();
    }

    @Override protected void update() {
        if(tw != 0) {
            switch (overlayPosition) {
                case BOTTOM:
                    tw = this.getAnimationTime(this.ftime, 3500.0D) * (overlayHeight + 170);
                    if(backwards) tw = this.getAnimationTime(this.ftime, 1600.0D) * overlayHeight;
                case RIGHT:
                    tw = this.getAnimationTime(this.ftime, 3500.0D) * overlayWidth;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int hue;

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor3f(1, 1, 1);

        if(!backwards) {
            hue = 130 - (int)(this.getAnimationTime(this.ftime, 4000.0D) * 130);
        } else {
            hue = (int)(this.getAnimationTime(this.ftime, 4000.0D) * 130);
        }

        if(this.mc.theWorld == null && this.mc.thePlayer == null)
            parentScreen.drawScreen(mouseX, mouseY, partialTicks);

        drawRect(0, 0, width, height, new Color(0, 0, 0, hue).getRGB());

        GL11.glPushMatrix();
        switch (overlayPosition) {
            case BOTTOM:
                GlStateManager.translate(0, backwards ? overlayHeight - tw : tw,0);
            case RIGHT:
                GlStateManager.translate(backwards ? overlayWidth - tw : tw, 0, 0);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glPopMatrix();
    }
}
