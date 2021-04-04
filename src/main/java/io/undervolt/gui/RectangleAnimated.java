package io.undervolt.gui;

import io.undervolt.api.animation.AnimationRender;
import io.undervolt.api.animation.AnimationTimings;
import io.undervolt.bridge.GameBridge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

import java.awt.*;

public class RectangleAnimated extends AnimationRender {

    private final FontRenderer fontRendererObj;
    private Minecraft mc;

    public RectangleAnimated() {
        super(1000, AnimationTimings.QUAD);
        this.fontRendererObj = GameBridge.getMinecraft().fontRendererObj;
        this.mc = GameBridge.getMinecraft();
    }

    @Override
    public void render() {

        super.render();

        ScaledResolution scaledresolution = new ScaledResolution(this.mc);

        GlStateManager.pushMatrix();

        float modifier =  (scaledresolution.getScaledWidth() - this.deltaTime * 120);

        System.out.println(modifier);

        GlStateManager.translate(this.isReverse() ? (this.deltaTime * scaledresolution.getScaledWidth()) : modifier, 0, 0);

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        Color color = new Color(255, 255, 255, (int) (isReverse() ? reverse(183) : this.deltaTime * 255));
        drawRect(0, 0, 200, 200, color.getRGB());
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

    }

}
