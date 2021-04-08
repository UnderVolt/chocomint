package io.undervolt.gui;

import io.undervolt.api.animation.AnimationRender;
import io.undervolt.api.animation.AnimationTimings;
import io.undervolt.bridge.GameBridge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

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
        /* Esto es para un tipo de white mask que no anda :(  */
        ScaledResolution sr = new ScaledResolution(this.mc);


        GlStateManager.pushMatrix();

        glEnable(GL_STENCIL_TEST);
        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
        glStencilFunc(GL_ALWAYS, 1, 0xFF); // all fragments should pass the stencil test
        glStencilMask(0xFF); // enable writing to the stencil buffer
        drawRect(100, 0, 200, sr.getScaledHeight(), Color.red.getRGB());

        glStencilFunc(GL_NOTEQUAL, 1, 0xFF);
        glStencilMask(0x00); // disable writing to the stencil buffer
        glDisable(GL_DEPTH_TEST);


        /*    Rectangle mask        * */
        GlStateManager.pushMatrix();
        Color color = new Color(255, 255, 255, (int) (isReverse() ? 255 - (this.deltaTime * 255) : this.deltaTime * 255));
        this.lastX = this.isReverse() ? reverse(100) : 100;
        GlStateManager.translate(lastX, 0, 0);
        GlStateManager.color(1, 1, 1, 1);
        drawRect(0, 0, 100, 100, color.getRGB());
        GlStateManager.popMatrix();

        glStencilMask(0xFF);
        glStencilFunc(GL_ALWAYS, 1, 0xFF);
        glEnable(GL_DEPTH_TEST);

        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();

    }

    @Override
    public void toggle() {
        if (!this.isReverse()) {
            setTiming(AnimationTimings.LINEAR);
        }
        super.toggle();
    }
}
