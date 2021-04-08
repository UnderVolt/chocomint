package io.undervolt.gui.menu;

import io.undervolt.api.animation.AnimationRender;
import io.undervolt.api.animation.AnimationScreen;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class Menu extends AnimationScreen {

    private final Chocomint chocomint;
    private final GuiScreen previous;
    protected long ftime;
    private final String menuName;

    private double tw = Integer.MAX_VALUE;
    protected int scroll = 0;
    private int pageSize;

    private final MenuAnimations menuAnimations;

    private final int position = 0;

    public Menu(final GuiScreen prev, final Chocomint chocomint, final String menuName, final int pageSize) {
        //super(prev, chocomint);
        this.menuName = menuName;
        this.chocomint = chocomint;
        this.pageSize = pageSize;
        this.previous = prev;
        this.menuAnimations = new MenuAnimations();
    }

    public void drawMenuItems(int mouseX, int mouseY, float partialTicks, int x, int scroll) {}

    private final ResourceLocation bracketRes = new ResourceLocation("/chocomint/ui/bracket.png");

    @Override
    public void initGui() {
        this.ftime = Minecraft.getSystemTime();
        this.buttonList.add(new TexturedMenuInterfaceButton(100, 0, 0, 20, 20, "back"));
        this.menuAnimations.init();
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

        this.menuAnimations.drawScreen(mouseX, mouseY, partialTicks);

        if(menuAnimations.deltaTime == 1) {
            drawDefaultBackground();
            drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 100).getRGB());
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);

        GlStateManager.translate(0, this.height * (1-menuAnimations.deltaTime),0);

        GL11.glColor3f(255, 255, 255);

        this.mc.getTextureManager().bindTexture(this.bracketRes);

        drawModalRectWithCustomSizedTexture(-5, position - 48, 0, 0, this.width + 10, 50, this.width + 10,50);
        drawRect(0, position, this.width, pageSize, new Color(32,34,37).getRGB());
        drawGradientRect(0, position, this.width, 250, Color.BLACK.getRGB(), 0);
        drawRect(this.getContentMargin(), position, this.getContentMargin() + this.getContentWidth(), pageSize, new Color(39, 39, 45).getRGB());
        drawRect(0, position, this.width, 20, new Color(54,57,63).getRGB());

        this.fontRendererObj.drawString(this.menuName, 24, position + 7, Color.white.getRGB());
        drawMenuItems(mouseX, mouseY, partialTicks, this.getContentMargin(), this.scroll);

        if(menuAnimations.deltaTime == 1)
            super.drawScreen(mouseX, mouseY, partialTicks);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i < 0 && !(this.scroll <= 0)) this.scroll -=8;
        else if (i > 0  && (this.scroll <= this.pageSize - this.height)) this.scroll += 8;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 100) this.mc.displayGuiScreen(previous);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 1) this.mc.displayGuiScreen(previous);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
