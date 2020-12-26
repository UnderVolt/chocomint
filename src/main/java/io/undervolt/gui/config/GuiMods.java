package io.undervolt.gui.config;

import io.undervolt.gui.TextureGameBarButton;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.config.ProfileLoader;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiMods extends EditGUI {

    private boolean show;
    private final Chocomint chocomint;
    private GuiScreen previousScreen;

    /**
     * Constructor
     *
     * @param previousScreen
     * @param chocomint
     */
    public GuiMods(GuiScreen previousScreen, Chocomint chocomint) {
        super(previousScreen, chocomint);
        this.chocomint = chocomint;
        this.previousScreen = previousScreen;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new TextureGameBarButton(650847, this.width / 2 + 2,this.height / 2 + 40, 20, 20, "settings"));
        this.buttonList.add(new TextureGameBarButton(650849, this.width / 2 - 22,this.height / 2 + 40, 20, 20, "list"));
        AtomicInteger i = new AtomicInteger();
        AtomicInteger id = new AtomicInteger(1);
        this.chocomint.getLoader().availableProfiles.forEach(profile -> {
            this.buttonList.add(new GuiButton(id.get(),  this.width - 100, i.get(), 100, 20, profile.getName()));
            i.set(i.get() + 20);
            id.set(id.get() + 1);
        });

        this.buttonList.add(new GuiButton(650846,  this.width - 100, i.get(), 100, 20, "Agregar Perfil"));
        super.initGui();
    }

    @Override
    public void drawScreen(int x, int y, float partial) {
        super.drawScreen(x, y, partial);
        GL11.glColor3f(255, 255, 255);
        drawRect(this.width / 2 - 20, this.height / 2 - 20, this.width / 2, this.height / 2 + 20, new Color(65, 44, 25).getRGB());
        drawRect(this.width / 2, this.height / 2 - 20, this.width / 2 + 20, this.height / 2 + 20, new Color(63, 222, 160).getRGB());
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 650846) {
            try {
                ProfileLoader.Profile newProf = this.chocomint.getLoader().newProfile("Perfil " + new Random().nextInt(500));
                this.chocomint.getConfigurableManager().configurableList.forEach(configurable -> configurable.saveConfig(newProf));
                this.mc.displayGuiScreen(new GuiMods(this.previousScreen, this.chocomint));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.chocomint.getLoader().setSelectedProfile(this.chocomint.getLoader().availableProfiles.get(button.id - 1));
            this.chocomint.getConfigurableManager().reloadConfig(this.chocomint.getLoader().availableProfiles.get(button.id - 1));
        }
        super.actionPerformed(button);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);
        this.chocomint.getConfigurableManager().renderModList.forEach(gui -> {
            int buttonXpos = (int) (gui.x -(2 * gui.scale));
            int buttonYpos = (int) (gui.y + (gui.height + 4) * gui.scale);

            if((x >= buttonXpos) && (y >= buttonYpos) && (x <= buttonXpos + fontRendererObj.getStringWidth("[ ]")) && (y <= buttonYpos + fontRendererObj.FONT_HEIGHT)) {
                this.mc.displayGuiScreen(new GuiEditRenderMod(this, gui));
            }
        });
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
}
