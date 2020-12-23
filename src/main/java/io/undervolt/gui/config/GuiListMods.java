package io.undervolt.gui.config;

import io.undervolt.gui.menu.Menu;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiListMods extends Menu {

    private final Chocomint chocomint;

    public GuiListMods(GuiScreen prev, Chocomint chocomint) {
        super(prev, chocomint, "Mods disponibles", 0);
        this.chocomint = chocomint;
    }

    @Override
    public void drawMenuItems(int mouseX, int mouseY, float partialTicks) {
        this.setPageSize(Math.max(this.height, this.chocomint.getConfigurableManager().modList.size() * 22));
        if(this.chocomint.getConfigurableManager().modList.size() > 0) {
            AtomicInteger y = new AtomicInteger(41);
            this.chocomint.getConfigurableManager().modList.forEach(mod -> {
                drawString(this.fontRendererObj, mod.getName(), (this.width / 2) - 10 - this.mc.fontRendererObj.getStringWidth(mod.getName()), y.get(), Color.WHITE.getRGB());
                y.set(y.get() + 22);
            });
        } else {
            drawCenteredString(this.fontRendererObj, "¡No tenés ningún mod instalado!", this.width / 2, this.height / 2 - 4, Color.WHITE.getRGB());
        }
    }

    @Override
    public void initGui() {
        AtomicInteger y = new AtomicInteger(35);
        AtomicInteger i = new AtomicInteger(0);
        this.chocomint.getConfigurableManager().modList.forEach(mod -> {
            this.buttonList.add(new GuiButton(i.get(), this.width / 2 + 10, y.get(), 100, 20, "Enabled: " + (mod.enabled ? "\247aOn" : "\247cOff")));
            y.set(y.get() + 22);
            i.set(i.get() + 1);
        });
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id < 100) {
            this.chocomint.getConfigurableManager().modList.get(button.id).enabled = !this.chocomint.getConfigurableManager().modList.get(button.id).enabled;
            button.displayString = "Enabled: " + (this.chocomint.getConfigurableManager().modList.get(button.id).enabled ? "\247aOn" : "\247cOff");
            this.chocomint.getConfigurableManager().modList.get(button.id).saveConfig(this.chocomint.getLoader().selectedProfile);
        }
        super.actionPerformed(button);
    }
}
