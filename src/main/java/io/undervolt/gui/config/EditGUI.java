package io.undervolt.gui.config;

import io.undervolt.instance.Chocomint;
import io.undervolt.mod.RenderMod;
import io.undervolt.utils.config.ConfigurableManager;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class EditGUI extends GuiScreen {

    private final Chocomint chocomint;
    private final GuiScreen previousScreen;
    private final ConfigurableManager configurableManager;

    protected RenderMod draggedGui;
    protected int lastX;
    protected int lastY;

    public EditGUI(GuiScreen previousScreen, Chocomint chocomint) {
        this.chocomint = chocomint;
        this.previousScreen = previousScreen;
        this.configurableManager = chocomint.getConfigurableManager();
    }

    @Override
    public void drawScreen(int x, int y, float deltaTime) {
        this.configurableManager.renderModList.forEach(gui->{
            GL11.glPushMatrix();
            GL11.glColor3f(255,255,255);
            GL11.glTranslatef(gui.x, gui.y, 0);
            GL11.glScalef(gui.scale, gui.scale, 0);
            if(gui.enabled) {
                this.chocomint.getRenderUtils().drawRoundedRect(-2, -2, gui.width + 4, gui.height + 4, 1, new Color(255, 255, 255, 50).getRGB());
            }
            GL11.glPopMatrix();
            drawString(this.fontRendererObj, "[ ]" + gui.getName(), (int) (gui.x -(2 * gui.scale)),
                    (int) (gui.y + (gui.height + 4) * gui.scale), Color.WHITE.getRGB());
        });
        if (this.draggedGui != null) {
            this.draggedGui.x = this.draggedGui.x + x - this.lastX;
            this.draggedGui.y = this.draggedGui.y + y - this.lastY;
            fitGuiIntoScreen(this.draggedGui);
        }
        this.lastX = x;
        this.lastY = y;
        super.drawScreen(x, y, deltaTime);
    }

    @Override
    public void initGui() {
        this.configurableManager.renderModList.forEach(this::fitGuiIntoScreen);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);
        this.configurableManager.renderModList.forEach(gui->{
            if ((x >= gui.x) && (y >= gui.y) && (x <= gui.x + gui.width * gui.scale) && (y <= gui.y + gui.height * gui.scale)) {
                this.draggedGui = gui;
                this.lastX = x;
                this.lastY = y;
                gui.saveConfig(this.chocomint.getLoader().selectedProfile);
            }
        });
    }
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.draggedGui = null;
    }

    private void fitGuiIntoScreen(RenderMod gui) {
        gui.x = Math.min(this.width - (int)(gui.width * gui.scale), Math.max(0, gui.x));
        gui.y = Math.min(this.height - (int)(gui.height * gui.scale), Math.max(0, gui.y));
    }

    @Override
    public void onGuiClosed() {
        this.configurableManager.renderModList.forEach(gui->{
            gui.saveConfig(this.chocomint.getLoader().selectedProfile);
        });
    }
}
