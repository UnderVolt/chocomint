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

    private float lastX = -1;

    public RectangleAnimated() {
        super(1000, AnimationTimings.QUAD, false);
        this.fontRendererObj = GameBridge.getMinecraft().fontRendererObj;
        this.mc = GameBridge.getMinecraft();
    }

    @Override
    public void render() {

        super.render();

       /* ScaledResolution scaledresolution = new ScaledResolution(this.mc);

        GlStateManager.pushMatrix();

        float modifier =  (scaledresolution.getScaledWidth() - this.deltaTime * 200);

        float reverseMod = scaledresolution.getScaledWidth() + 200;

        System.out.println(255 - (this.deltaTime * 255));

        this.lastX = this.isReverse() ?  (scaledresolution.getScaledWidth() - 200) + (this.deltaTime * reverseMod) : modifier;

        GlStateManager.translate(lastX, 0, 0);

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        Color color = new Color(255, 255, 255, (int) (isReverse() ? 255 - (this.deltaTime * 255) : this.deltaTime * 255));
        drawRect(0, 0, 200, 200, color.getRGB());
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();*/

    }

    @Override
    public void toggle() {
        if(!this.isReverse()){
            setTiming(AnimationTimings.LINEAR);
        }
        super.toggle();
    }
}
