package io.undervolt.gui.menu;

import io.undervolt.bridge.GameBridge;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.AnimationUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class Menu extends AnimationUI {

    protected final Chocomint chocomint;
    private final GuiScreen previous;
    protected long ftime;
    private final String menuName;

    private double tw = Integer.MAX_VALUE;
    private double scroll = 0;
    private int pageSize;

    private int position = 0;

    public Menu(final GuiScreen prev, final String menuName, final int pageSize) {
        this.menuName = menuName;
        this.chocomint = GameBridge.getChocomint();
        this.pageSize = pageSize;
        this.previous = prev;
    }

    public void drawMenuItems(int mouseX, int mouseY, float partialTicks) {}

    private final ResourceLocation bracketRes = new ResourceLocation("/chocomint/ui/bracket.png");

    @Override
    public void initGui() {
        this.ftime = Minecraft.getSystemTime();
        this.buttonList.add(new TexturedMenuInterfaceButton(100, 0, 0, 20, 20, "back"));
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if(tw != 0) {
            tw = (this.getAnimationTime(this.ftime, 3000.0D) * 400D);
            System.out.println("Set tw to " + tw);
        }

        if(tw == 0) {
            drawDefaultBackground();
            drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 100).getRGB());
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);

        GlStateManager.translate(0, tw,0);

        GL11.glColor3f(255, 255, 255);

        this.mc.getTextureManager().bindTexture(this.bracketRes);

        drawModalRectWithCustomSizedTexture(-5, position - 50, 0, 0, this.width + 10, 50, this.width + 10,50);
        drawRect(0, position, this.width, pageSize, new Color(39, 39, 45).getRGB());
        drawRect(0, position, this.width, 20, new Color(54,57,63).getRGB());

        this.fontRendererObj.drawString(this.menuName, 24, position + 7, Color.white.getRGB());

        GL11.glPushMatrix();
        GlStateManager.translate(0, scroll, 0);
        drawMenuItems(mouseX, mouseY, partialTicks);
        GL11.glPopMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i < 0 && this.scroll < 0) this.scroll += 2.3;
        else if (i > 0 && this.scroll > (this.width - this.pageSize)) this.scroll -=2.3;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 100) {
            this.mc.displayGuiScreen(null);

            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 1) this.mc.displayGuiScreen(previous);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
