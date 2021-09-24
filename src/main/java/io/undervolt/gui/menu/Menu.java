package io.undervolt.gui.menu;

import io.undervolt.gui.GameBar;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.AnimationUI;
import io.undervolt.utils.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Menu extends AnimationUI {

    private final Chocomint chocomint;
    private final GuiScreen previous;
    protected long ftime;
    private final String menuName, menuIcon;

    private double tw = Integer.MAX_VALUE;
    protected int scroll = 0;
    private int pageSize;

    private final int position = 0;

    public Menu(final GuiScreen prev, final Chocomint chocomint, final String menuName, final int pageSize) {
        this.menuIcon = null;
        this.menuName = menuName;
        this.chocomint = chocomint;
        this.pageSize = pageSize;
        this.previous = prev;
    }

    public Menu(final GuiScreen prev, final Chocomint chocomint, final String menuName, final String menuIcon, final int pageSize) {
        this.menuName = menuName;
        this.menuIcon = menuIcon;
        this.chocomint = chocomint;
        this.pageSize = pageSize;
        this.previous = prev;
    }

    public void drawMenuItems(int mouseX, int mouseY, float partialTicks, int x, int scroll) {}

    private final ResourceLocation bracketRes = new ResourceLocation("/chocomint/ui/bracket.png");

    @Override
    public void initGui() {
        this.ftime = Minecraft.getSystemTime();
        this.chocomint.getGameBar().init(width, height);
        super.initGui();
    }

    protected int getContentWidth() {
        if(this.width < 500) {
            return this.width;
        } else {
            return 500;
        }
    }

    protected int getContentMargin() {
       if(this.getContentWidth() == this.width) {
           return 0;
       } else {
           return (this.width / 2) - 250;
       }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        int hue = 0;

        if(this.mc.theWorld == null && this.mc.thePlayer == null)
            previous.drawScreen(mouseX, mouseY, partialTicks);

        if(tw != 0) {
            tw = (this.getAnimationTime(this.ftime, 4000.0D) * height);
        }

        if(tw/100 > 1) {
            hue = 130 / ((int) tw / 100);
        } else {
            hue = 130;
        }

        drawRect(0, 0, width, height, new Color(0, 0, 0, hue).getRGB());

        GL11.glPushMatrix();

        GlStateManager.translate(0, tw,0);

        GL11.glColor3f(255, 255, 255);

        GL11.glPushMatrix();
        GlStateManager.translate(this.getContentMargin(), 0, 0);
        GL11.glRotatef(90, 0, 0, 1);
        drawGradientRect(0, 0, height, 10, new Color(0, 0, 0, 100).getRGB(), 0);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GlStateManager.translate(this.getContentMargin() + this.getContentWidth() + 10, 0, 0);
        GL11.glRotatef(90, 0, 0, 1);
        drawGradientRect(0, 0, height, 10, 0, new Color(0, 0, 0, 100).getRGB());
        GL11.glPopMatrix();

        this.mc.getTextureManager().bindTexture(this.bracketRes);
        drawModalRectWithCustomSizedTexture(this.getContentMargin(), position - 48, 0, 0, this.getContentWidth(), 50, this.getContentWidth(),50);
        drawRect(this.getContentMargin(), position, this.getContentMargin() + this.getContentWidth(), pageSize, new Color(39, 39, 45).getRGB());
        drawRect(this.getContentMargin(), position, this.getContentMargin() + this.getContentWidth(), 50, new Color(54,57,63).getRGB());

        GL11.glColor3f(255,255,255);
        if(this.menuIcon != null) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("/chocomint/icon/" + menuIcon + ".png"));
            drawModalRectWithCustomSizedTexture(this.getContentMargin() + 5, position + 28, 0, 0, 20, 20, 20,20);
            this.fontRendererObj.drawString(this.menuName, this.getContentMargin() + 24, position + 36, Color.white.getRGB());
        } else
            this.fontRendererObj.drawString(this.menuName, this.getContentMargin() + 5, position + 36, Color.white.getRGB());
        drawMenuItems(mouseX, mouseY, partialTicks, this.getContentMargin(), this.scroll);

        super.drawScreen(mouseX, mouseY, partialTicks);

        GL11.glPopMatrix();

        this.chocomint.getGameBar().draw(mouseX, mouseY, partialTicks, width, height);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i < 0 && !(this.scroll <= 0)) this.scroll -=8;
        else if (i > 0  && (this.scroll <= this.pageSize - this.height)) this.scroll += 8;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.chocomint.getGameBar().mouseClicked(mouseX, mouseY, mouseButton, width, height);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 1) this.mc.displayGuiScreen(previous);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
