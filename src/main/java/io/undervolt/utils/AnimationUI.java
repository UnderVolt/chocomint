package io.undervolt.utils;

import io.undervolt.bridge.GameBridge;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class AnimationUI extends GuiScreen {

    protected final Chocomint chocomint;

    public AnimationUI() {
        this.chocomint = GameBridge.getChocomint();
    }

    protected double getAnimationTime(long delta, double speed) {
        double sw = (double) (Minecraft.getSystemTime() - delta) / speed;

        if (sw < 0.0D || sw > 1.0D) {
            return 0;
        }
        this.updateGui();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        double mw = sw * 2.0D;

        if (delta == 0) {
            mw = 2.0D - mw;
        }

        mw = mw * 4.0D;
        mw = 1.0D - mw;

        if (mw < 0.0D) {
            mw = 0.0D;
        }

        mw = mw * mw;

        return mw;
    }

    protected double fromBottom(long delta) {
        double ssw = (double) (Minecraft.getSystemTime() - delta) / 8000.0D;
        if (ssw > 0.5D) {
            ssw = 0.5D;
        }
        double dw = ssw * 2.0D;
        dw = dw * 4.0D;
        dw = 1.0D - dw;
        if (dw < 0.0D) {
            dw = 0.0D;
        }
        dw = dw * dw;
        dw = dw * dw;
        return (-(int) (dw * 36.0D));
    }

    protected double fromLeft(long delta) {
        double ssw = (double) (Minecraft.getSystemTime() - delta) / 8000.0D;
        if (ssw > 0.5D) {
            ssw = 0.5D;
        }
        double dw = ssw * 2.0D;
        dw = dw * 4.0D;
        dw = 1.0D - dw;
        if (dw < 0.0D) {
            dw = 0.0D;
        }
        dw = dw * dw;
        dw = dw * dw;
        return (-(int) (dw * 200.0D));
    }

    protected void updateGui() {
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        this.width = this.mc.displayWidth;
        this.height = this.mc.displayHeight;
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        this.width = scaledresolution.getScaledWidth();
        this.height = scaledresolution.getScaledHeight();
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, this.width, this.height, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);

    }

}
