package io.undervolt.gui.contributors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ContributorsPanel extends Gui {
    private final Minecraft mc;
    private final ContributorsManager contributorsManager;
    private boolean isActive;

    public ContributorsPanel(final Minecraft mc, final ContributorsManager contributorsManager, boolean isActive) {
        this.mc = mc;
        this.contributorsManager = contributorsManager;
        this.isActive = isActive;
    }

    public void drawPanel(int screenWidth, int screenHeight) {
        if(this.isActive) {
            drawRect(0, 20, 120, screenHeight,
                    new Color(34,34,34,183).getRGB());
            drawString(this.mc.fontRendererObj,
                    this.getContributorsManager().getContributors().isEmpty()
                            ? "Cargando..." : "Contribuciones", 25,
                    29, Color.WHITE.getRGB());
            int x = 5;
            AtomicInteger y = new AtomicInteger(50);
            this.getContributorsManager().getContributors().forEach((contributorStatistic, textureId) -> {
                GlStateManager.color(1f, 1f, 1f);
                GlStateManager.bindTexture(textureId.getGlTextureId());

                drawScaledCustomSizeModalRect(x, y.get(), 0, 0, 20, 20, 20, 20, 20, 20);
                drawString(this.mc.fontRendererObj, contributorStatistic, x + 25, y.get(), 0xE0E0E0);
                drawString(this.mc.fontRendererObj, this.getContributorsManager().getCommits().get(contributorStatistic) + " commits", x + 25, y.get() + 10, 0x757575);

                y.addAndGet(25);
            });
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ContributorsManager getContributorsManager() {
        return contributorsManager;
    }

    public void toggleActive() {
        this.isActive = !this.isActive;
    }
}
