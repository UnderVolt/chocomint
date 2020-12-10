package io.undervolt.gui;

import io.undervolt.instance.Chocomint;
import io.undervolt.utils.AnimationUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Menu extends AnimationUI {

    private final Chocomint chocomint;
    protected long ftime;

    public Menu(final Chocomint chocomint) {
        this.chocomint = chocomint;
    }

    public void drawMenuItems(int mouseX, int mouseY, float partialTicks) {}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        double dw = this.getAnimationTime(this.ftime, 2000.0D);
        int tw = (int) (dw * 200D);

        GL11.glPushMatrix();
        GlStateManager.translate(0, tw,0);
        drawRect(this.width / 7, 0, this.width - (this.width / 7), this.height, new Color(54,57,63).getRGB());
        drawMenuItems(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glPopMatrix();
    }
}
