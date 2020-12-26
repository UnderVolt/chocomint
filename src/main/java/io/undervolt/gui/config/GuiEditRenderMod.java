package io.undervolt.gui.config;

import io.undervolt.gui.menu.Menu;
import io.undervolt.mod.RenderMod;
import io.undervolt.utils.forge.GuiSlider;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class GuiEditRenderMod extends Menu {

    private final RenderMod renderMod;

    private GuiButton button1;
    private GuiSlider sliderScale;

    public GuiEditRenderMod(GuiScreen prev, RenderMod renderMod) {
        super(prev, "Edición de Mod: " + renderMod.getName(), 0);
        this.renderMod = renderMod;
    }

    @Override
    public void drawMenuItems(int mouseX, int mouseY, float partialTicks) {
        this.setPageSize(this.height);
        GL11.glPushMatrix();
        GL11.glColor3f(255,255,255);
        GL11.glTranslatef((this.width / 2) - ((this.renderMod.width * this.renderMod.scale) / 2), 30, 0);
        GL11.glScalef(renderMod.scale, renderMod.scale, 0);
        this.renderMod.render();
        this.chocomint.getRenderUtils().drawRoundedRect(-2, -2, renderMod.width + 4, renderMod.height + 4, 1, new Color(255, 255, 255, 50).getRGB());
        GL11.glPopMatrix();
        drawString(this.fontRendererObj, renderMod.getName(), (int) ((this.width / 2) - ((this.renderMod.width * this.renderMod.scale) / 2) + (- 2 * this.renderMod.scale)),
                (int) (30 + (renderMod.height + 4) * renderMod.scale), Color.WHITE.getRGB());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.renderMod.scale = (sliderScale.getValueInt() / 100.0F);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 0) {
            this.renderMod.enabled = !this.renderMod.enabled;
            button1.displayString = "Enabled: " + (this.renderMod.enabled ? "\247aOn" : "\247cOff");
        }
    }

    @Override
    public void initGui() {
        this.buttonList.add(button1 = new GuiButton(0, this.width / 2 - 50, this.height / 4 + 20, 100, 20, "Enabled: " + (this.renderMod.enabled ? "\247aOn" : "\247cOff")));
        this.buttonList.add(sliderScale = new GuiSlider(2, this.width / 2 - 50, this.height / 4 + 45, 100, 20, "Scale: ", "%", 0.0D, 200.0D, this.renderMod.scale * 100.0D, false, true));
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        this.renderMod.saveConfig(this.chocomint.getLoader().selectedProfile);
        super.onGuiClosed();
    }
}
