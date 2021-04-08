package io.undervolt.gui.contributors;

import io.undervolt.api.animation.AnimationRender;
import io.undervolt.api.animation.AnimationTimings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ContributorsPanel extends AnimationRender {
    private final Minecraft mc;
    private final ContributorsManager contributorsManager;

    public ContributorsPanel(final Minecraft mc, final ContributorsManager contributorsManager, boolean isActive) {
        super(250, AnimationTimings.QUAD, !isActive);
        this.mc = mc;
        this.contributorsManager = contributorsManager;
    }

    public void drawPanel(int screenWidth, int screenHeight) {
        this.render();
        GlStateManager.pushMatrix();

        GlStateManager.translate(this.isReverse() ? reverse(120) : this.deltaTime * 120, 0, 0);

        drawRect(-120, 20, 0, screenHeight, new Color(34,34,34,183).getRGB());
        drawString(this.mc.fontRendererObj,  this.getContributorsManager().getContributors().isEmpty() ? "Cargando..." : "Contribuciones", -120 + 25, 29, Color.WHITE.getRGB());

        int x = -115;

        AtomicInteger y = new AtomicInteger(50);

        this.getContributorsManager().getContributors().forEach((contributorStatistic, textureId) -> {
            GlStateManager.color(1f, 1f, 1f);
            GlStateManager.bindTexture(textureId.getGlTextureId());

            drawScaledCustomSizeModalRect(x, y.get(), 0, 0, 20, 20, 20, 20, 20, 20);
            drawString(this.mc.fontRendererObj, contributorStatistic, x + 25, y.get(), 0xE0E0E0);
            drawString(this.mc.fontRendererObj, this.getContributorsManager().getCommits().get(contributorStatistic) + " commits", x + 25, y.get() + 10, 0x757575);

            y.addAndGet(25);
        });

        GlStateManager.popMatrix();
    }

    public boolean isActive() {
        return !this.isReverse();
    }


    public ContributorsManager getContributorsManager() {
        return contributorsManager;
    }

    @Override
    public void toggle() {
        if (!this.isReverse()) {
            setTiming(AnimationTimings.LINEAR);
        }
        super.toggle();
    }
}
