package io.undervolt.gui;

import io.undervolt.instance.Chocomint;
import io.undervolt.utils.AnimationUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class Menu extends AnimationUI {

    private final Chocomint chocomint;
    protected long ftime;

    private double tw = Integer.MAX_VALUE;
    private double scroll = 0;

    public Menu(final Chocomint chocomint) {
        this.chocomint = chocomint;
        this.ftime = Minecraft.getSystemTime();
    }

    public void drawMenuItems(int mouseX, int mouseY, float partialTicks) {}

    private final ResourceLocation bracketRes = new ResourceLocation("/chocomint/ui/bracket-with-bg.png");

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if(tw != 0) {
            tw = (this.getAnimationTime(this.ftime, 3000.0D) * 200D);
            System.out.println("Set tw to " + tw);
        }

        drawDefaultBackground();
        drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 100).getRGB());

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GlStateManager.translate(0, tw,0);
        GlStateManager.translate(0, scroll, 0);
        GL11.glColor3f(255, 255, 255);
        this.mc.getTextureManager().bindTexture(this.bracketRes);
        drawModalRectWithCustomSizedTexture(40, 20, 0, 0, this.width - 80, this.height,this.width - 80, this.height);
        drawRect(40, 70, this.width - 80, 930, new Color(54,57,63).getRGB());
        drawMenuItems(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i < 0 && this.scroll < -3) this.scroll += 3;
        else this.scroll -= 3;
    }
}
